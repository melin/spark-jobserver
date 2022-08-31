package io.github.melin.spark.jobserver.driver;

import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import io.github.melin.spark.jobserver.core.enums.JobType;
import io.github.melin.spark.jobserver.core.util.LogRecord;
import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.driver.task.SparkJarTask;
import io.github.melin.spark.jobserver.driver.task.SparkPythonTask;
import io.github.melin.spark.jobserver.driver.task.SparkSqlTask;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import com.gitee.melin.bee.core.support.Result;
import com.google.common.collect.Maps;
import io.github.melin.spark.jobserver.core.util.TaskStatusFlag;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * huaixin 2022/4/11 18:22
 */
@RestController
public class DriverRest {

    private static final Logger LOG = LoggerFactory.getLogger(DriverRest.class);

    @Autowired
    private SparkDriverContext sparkDriverContext;

    @Autowired
    private SparkSqlTask sparkSqlTask;

    @Autowired
    private SparkJarTask sparkJarTask;

    @Autowired
    private SparkPythonTask sparkPythonTask;

    @RequestMapping(value = "/ok", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ok() {
        return "ok";
    }

    /**
     * 查询job 是否在运行
     *
     * @param instanceCode
     * @return
     */
    @RequestMapping("/sparkDriver/isJobRunning")
    public boolean isJobRunning(String instanceCode) {
        LOG.info("isJobRunning, instance status: {}, code: {}",
                sparkDriverContext.getStatus(), InstanceContext.getInstanceCode());

        if (sparkDriverContext.getStatus() == DriverStatus.RUNNING
                && instanceCode.equalsIgnoreCase(InstanceContext.getInstanceCode())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * control端抽取日志接口
     */
    @RequestMapping("/sparkDriver/getServerLog")
    public List<LogRecord> getServerLog(String instanceCode) {
        return LogUtils.getMessage(instanceCode);
    }

    /**
     * 提交spark任务的接口
     */
    @PostMapping("/sparkDriver/runSparkJob")
    public Result<String> runSparkJob(@RequestBody InstanceDto instanceDto) {
        LogUtils.clearLog(instanceDto.getInstanceCode());

        InstanceContext.setInstanceType(instanceDto.getInstanceType());
        InstanceContext.setAccessKey(instanceDto.getAccessKey());
        InstanceContext.setInstanceCode(instanceDto.getInstanceCode());
        InstanceContext.setAccessKey(instanceDto.getAccessKey());
        LOG.info("spark dirver received job");

        LogUtils.info("当前 yarn queue: {}, ApplicationId: {}, shareDriver: {}",
                instanceDto.getYarnQueue(), SparkEnv.getApplicationId(), String.valueOf(instanceDto.isShareDriver()));

        LOG.info("Spark task: {} begined, submit from {}",
                instanceDto.getInstanceCode(), instanceDto.getSparkJobServerUrl());

        JobType jobType = instanceDto.getJobType();
        if (JobType.SPARK_SQL == jobType) {
            sparkDriverContext.startDriver();
            return sparkSqlTask.runTask(instanceDto);
        } else if (JobType.SPARK_JAR == jobType) {
            sparkDriverContext.startDriver();
            return sparkJarTask.runTask(instanceDto);
        } else if (JobType.SPARK_PYTHON == jobType) {
            sparkDriverContext.startDriver();
            return sparkPythonTask.runTask(instanceDto);
        } else {
            return Result.failureResult("不支持的 jobType: " + jobType);
        }
    }

    @RequestMapping("/sparkDriver/getDriverResource")
    public Result<Map<String, Long>> getAppResource() {
        try {
            if (SparkEnv.getSparkSession() != null) {
                int executorNums = SparkEnv.getSparkSession().sparkContext().getExecutorIds().size();
                SparkConf sparkConf = SparkEnv.getSparkSession().sparkContext().getConf();
                long executorCores = sparkConf.getLong("spark.executor.cores", 1);
                long driverCores = sparkConf.getLong("spark.driver.cores", 1);
                long cores = driverCores + executorCores * executorNums;

                long executorMemory = sparkConf.getSizeAsMb("spark.executor.memory", "10240");
                long driverMemory = sparkConf.getSizeAsMb("spark.driver.memory", "5120");
                long memorys = driverMemory + executorMemory * executorNums;

                Map<String, Long> data = Maps.newHashMap();
                data.put("cores", cores);
                data.put("memorys", memorys);
                return Result.successDataResult(data);
            } else {
                return Result.failureResult("spark session is null");
            }
        } catch (Exception e) {
            LOG.error("query driver resource" + e.getMessage());
            return Result.failureResult("query driver resource" + e.getMessage());
        }
    }

    @RequestMapping("/sparkDriver/killJob")
    public Result<String> killJob(String instanceCode) {
        if (sparkDriverContext.getStatus() == DriverStatus.IDLE) {
            return Result.successMessageResult("jobserver idle status");
        }

        String currrentInstanceCode = InstanceContext.getInstanceCode();
        if (StringUtils.isNotBlank(currrentInstanceCode)
                && !currrentInstanceCode.equals(instanceCode)) {
            return Result.successMessageResult("current instanceCode: " + currrentInstanceCode);
        }

        LOG.info("prepare to kill job {}", instanceCode);
        LogUtils.warn("task {} was canceled", instanceCode);

        LogUtils.clearLog(instanceCode);
        LogUtils.sendTaskStatusFlag(TaskStatusFlag.TASK_STOP_FLAG);

        try {
            sparkDriverContext.setUserStopTask(true);
            SparkEnv.getSparkSession().sparkContext().cancelAllJobs();
            if (sparkPythonTask.getPythonPid() > 0) {
                String command = "kill -9 " + sparkPythonTask.getPythonPid();
                Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
            }

            JobType jobType = InstanceContext.getJobType();
            if (JobType.SPARK_SQL == jobType) {
                sparkSqlTask.killJob(instanceCode);
            } else if (JobType.SPARK_JAR == jobType) {
                sparkJarTask.killJob(instanceCode);
            } else if (JobType.SPARK_PYTHON == jobType) {
                sparkPythonTask.killJob(instanceCode);
            }

            LOG.info("driver has been reset!");
            return Result.successMessageResult("driver has been reset!");
        } catch (Exception e) {
            LOG.error("reset driver error: " + e.getMessage());
            return Result.failureResult("reset driver error: " + e.getMessage());
        }
    }

    @RequestMapping("/sparkDriver/downloadYarnLog")
    public void downloadYanLog(HttpServletResponse response) {
        FileInputStream inputStream = null;
        BufferedOutputStream out = null;
        try {
            String localLogDir = System.getProperty("spark.yarn.app.container.log.dir");
            String filePath = localLogDir + "/stderr";
            File logFile = new File(filePath);
            if (logFile.exists()) {
                inputStream = new FileInputStream(logFile);

                response.reset();
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition", "attachment;filename=spark.log");
                response.addHeader("Content-Length", "" + logFile.length());
                out = new BufferedOutputStream(response.getOutputStream());

                IOUtils.copyLarge(inputStream, out);
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(out);
        }
    }
}

package io.github.melin.spark.jobserver.logs;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.support.DriverClientService;
import io.github.melin.spark.jobserver.support.YarnClientService;
import io.github.melin.spark.jobserver.util.DateUtils;
import io.github.melin.spark.jobserver.api.LogLevel;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.util.LogRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.*;
import static io.github.melin.spark.jobserver.core.enums.JobType.*;
import static io.github.melin.spark.jobserver.core.util.TaskStatusFlag.*;

/**
 * Created by admin on 2017/7/1.
 */
public class SparkTaskLogThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger("jobinstancelogs");

    private final JobInstanceService instanceService;

    private final YarnClientService yarnClientService;

    private final SparkLogService sparkLogService;

    private final DriverClientService driverClient;

    private final LogTaskDto logTaskDto;

    private final String instanceLogPath;

    private boolean driverRestarted = false;

    public SparkTaskLogThread(ApplicationContext applicationContext, LogTaskDto logTaskDto) {
        this.instanceService = applicationContext.getBean(JobInstanceService.class);
        this.yarnClientService = applicationContext.getBean(YarnClientService.class);
        this.sparkLogService = applicationContext.getBean(SparkLogService.class);
        this.driverClient = applicationContext.getBean(DriverClientService.class);
        ConfigProperties configProperties = applicationContext.getBean(ConfigProperties.class);
        this.logTaskDto = logTaskDto;
        this.instanceLogPath = configProperties.getInstanceLogPath();

        LOGGER.info("build log thread: {}", logTaskDto);
    }

    @Override
    public void run() {
        final JobType jobType = logTaskDto.getJobType();
        final InstanceType instanceType = logTaskDto.getInstanceType();
        final String instanceCode = logTaskDto.getInstanceCode();
        final String sparkDriverUrl = logTaskDto.getSparkDriverUrl();
        final String clusterCode = logTaskDto.getClusterCode();
        final String applicationId = logTaskDto.getApplicationId();
        final String scheduleDate = DateUtils.formateDate(logTaskDto.getScheduleTime());

        try {
            String path = instanceLogPath + "/" + scheduleDate + "/" + instanceCode + ".log";
            MDC.put("logFileName", path);
            LOGGER.info("Log Path: {}", path);

            LogRecord logRecord = null;
            int checkInstanceStatusCount = 0;

            OUT:
            while (true) {
                int msgCount = 0;
                List<LogRecord> logs = driverClient.getServerLog(sparkDriverUrl, instanceCode);
                if (logs != null) {
                    if (driverRestarted) {
                        driverRestarted = false;
                    }

                    for (LogRecord log : logs) {
                        if (TASK_ERROR_FLAG == log.getFlag() || TASK_END_FLAG == log.getFlag()
                                || TASK_STOP_FLAG == log.getFlag() || STAGE_ERROR_FLAG == log.getFlag()) {
                            logRecord = log;
                            break OUT;
                        } else {
                            msgCount++;
                            String msg = log.getMessage().replaceAll("\\<.*?>", ""); // 去掉html标签
                            if (log.getLevel() == LogLevel.WARN) {
                                LOGGER.warn(msg);
                            } else if (log.getLevel() == LogLevel.ERROR) {
                                LOGGER.error(msg);
                            } else if (log.getLevel() == LogLevel.STDOUT) {
                                LOGGER.info("\n" + msg);
                            } else {
                                LOGGER.info(msg);
                            }
                        }
                    }
                } else {
                    boolean isRun = driverClient.isSparkJobRunning(sparkDriverUrl, instanceCode, applicationId);
                    if (!isRun) {
                        LOGGER.info("{} spark job not running {}", instanceCode, sparkDriverUrl);
                        if (checkInstanceStatusCount >= 2) {
                            if (JobType.isBatchJob(jobType)) {
                                checkInstanceStatus(instanceCode, applicationId);
                            } else {
                                //@TODO 支持流任务
                            }
                            break;
                        } else {
                            TimeUnit.SECONDS.sleep(3);
                            checkInstanceStatusCount++;
                            continue;
                        }
                    }
                }
                if (msgCount > 10) {
                    TimeUnit.MILLISECONDS.sleep(50);
                } else {
                    TimeUnit.MILLISECONDS.sleep(1100 - msgCount * 100L);
                }
            }

            if (logRecord != null && logRecord.getFlag() == TASK_ERROR_FLAG) {
                LOGGER.error(logRecord.getMessage());
            } else if (logRecord != null && logRecord.getFlag() == STAGE_ERROR_FLAG) {
                instanceService.updateJobStatusByCode(instanceCode, FAILED);
            } else if (logRecord != null && logRecord.getFlag() == TASK_STOP_FLAG) {
                LOGGER.info("Instance {} is stoped by user", instanceCode);
            } else if (logRecord != null && logRecord.getFlag() == TASK_END_FLAG) {
                LOGGER.info("Instance {} finished", instanceCode);
            } else if (logRecord == null) {
                YarnApplicationState state = yarnClientService.getApplicationStatus(clusterCode, applicationId);
                LOGGER.error("Server {} is not normal, current status:{}", applicationId, state);

                String message = "";
                if (SPARK_PYTHON == jobType) {
                    message = "Spark driver 可能因为内存不足退出运行，如果代码中使用dataframe collect 或 toPandas，请优先使用分布式算法，或调整driver内存";
                } else {
                    message = "Spark driver 可能因为内存不足退出运行，请调整driver内存";
                }

                LOGGER.error("jobtype: {}, task: {}, run fail: {}", jobType, instanceCode, message);

                if (JobType.isBatchJob(jobType)) {
                    updateInstanceStatus(logTaskDto, instanceType, message);
                } else {
                    //@TODO 支持流任务
                }
            }
        } catch (Exception e) {
            LOGGER.error("请求日志失败：{}", e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        } finally {
            sparkLogService.removeLogThread(instanceCode);
        }
    }

    /**
     * driver 异常终止，更新实例状态。主要判断是否重试运行
     */
    private void updateInstanceStatus(LogTaskDto logTaskDto, InstanceType instanceType, String message) {
        String instanceCode = logTaskDto.getInstanceCode();
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
        if (FINISHED == instance.getStatus()) {
            LOGGER.info("作业运行完成");
            return;
        }

        if (InstanceType.DEV == instanceType) {
            LOGGER.error("task {} failed", instanceCode);
            instanceService.updateJobStatusByCode(instanceCode, FAILED);
        } else {
            int retryCount = instance.getRetryCount();
            if (retryCount >= 1) {
                instanceService.updateJobStatusByCode(instanceCode, FAILED);
            } else {
                instance.setStatus(WAITING);
                instance.setRetryCount(retryCount + 1);
                instanceService.updateEntity(instance);
                LOGGER.error("retry task {}", instanceCode);
            }
        }
    }

    /**
     * 可能存在实例状态为运行，jobserver 已经失败退出的场景，更新实例状态为失败
     */
    private void checkInstanceStatus(String instanceCode, String appId) {
        JobInstance jobInstance = instanceService.queryJobInstanceByCode(instanceCode);
        if (jobInstance.getStatus() == RUNNING) { //运行状态
            YarnApplicationState state = yarnClientService.getApplicationStatus(jobInstance.getClusterCode(), appId);
            if (YarnApplicationState.RUNNING != state && YarnApplicationState.ACCEPTED != state) {
                jobInstance.setStatus(FAILED); //失败状态
                instanceService.updateEntity(jobInstance);
            } else {
                yarnClientService.killApplicationOnYarn(jobInstance.getClusterCode(), appId);
            }
        }
    }

}

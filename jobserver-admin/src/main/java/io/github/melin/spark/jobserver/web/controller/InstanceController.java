package io.github.melin.spark.jobserver.web.controller;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.util.DateUtils;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.util.RandomUniqueIdGenerator;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import io.github.melin.spark.jobserver.core.service.JobInstanceContentService;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.gitee.melin.bee.core.support.Pagination;
import com.gitee.melin.bee.core.support.Result;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import io.github.melin.spark.jobserver.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class InstanceController {

    private static final Logger LOG = LoggerFactory.getLogger(InstanceController.class);

    @Autowired
    private JobInstanceService instanceService;

    @Autowired
    private JobInstanceContentService instanceContentService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private ConfigProperties configProperties;

    @RequestMapping("/instance")
    public String home(ModelMap model) {
        List<String> clients = instanceService.findByCriterion(
                Projections.distinct(Projections.property("clientName")));

        List<Cluster> clusters = clusterService.queryValidClusters();

        model.addAttribute("clients", clients);
        model.addAttribute("clusters", clusters);
        return "instance";
    }

    @PostMapping("/instance/saveJobInstance")
    @ResponseBody
    @Transactional
    public Result<Void> saveJobInstance(Long id, JobType jobType, String name, String jobText,
                                        String clusterCode, String jobConfig, boolean isRun) {
        if (id == null) {
            String instanceCode = RandomUniqueIdGenerator.getNewString(32);
            JobInstance.Builder builder = JobInstance.builder()
                    .setClusterCode(clusterCode)
                    .setCode(instanceCode)
                    .setName(name)
                    .setJobType(jobType)
                    .setInstanceType(InstanceType.API)
                    .setOwner("jobserver")
                    .setMaxRetryCount(0)
                    .setStatus(InstanceStatus.WAITING)
                    .setScheduleTime(Instant.now())
                    .setClientName("jobserver")
                    .setCreater("jobserver")
                    .setDependentCode(Constant.ROOT_SCHEDULE_NODE)
                    .setGmtCreated(Instant.now());

            instanceService.insertEntity(builder.build());
            instanceService.saveJobText(instanceCode, jobText, jobConfig);
        } else {
            JobInstance instance = instanceService.getEntity(id);
            instance.setJobType(jobType);
            instance.setName(name);
            instance.setGmtModified(Instant.now());
            if (isRun) {
                instance.setStatus(InstanceStatus.WAITING);
            }
            instanceService.updateEntity(instance);

            JobInstanceContent instanceContent =
                    instanceContentService.queryByNamedParam("code", instance.getCode());

            instanceContent.setJobConfig(jobConfig);
            instanceContent.setJobText(jobText);
            instanceContentService.updateEntity(instanceContent);
        }

        return Result.successResult();
    }

    @RequestMapping("/instance/queryInstance")
    @ResponseBody
    public Result<JobInstance> queryInstance(Long instanceId) {
        JobInstance instance = instanceService.getEntity(instanceId);
        return Result.successDataResult(instance);
    }

    @RequestMapping("/instance/queryInstanceContent")
    @ResponseBody
    public Result<JobInstanceContent> queryInstanceContent(String code) {
        JobInstanceContent instance = instanceContentService.queryByNamedParam("code", code);
        return Result.successDataResult(instance);
    }

    @RequestMapping("/instance/queryInstances")
    @ResponseBody
    public Pagination<JobInstance> queryInstances(String instanceCode, String instanceName,
                                                  InstanceStatus status, String clientName,
                                               int page, int limit, HttpServletRequest request) {
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        Order order1 = Order.desc("gmtCreated");
        if (StringUtils.isNotEmpty(sort)) {
            if ("asc".equals(order)) {
                order1 = Order.asc(sort);
            } else {
                order1 = Order.desc(sort);
            }
        }

        List<String> params = Lists.newArrayList();
        List<Object> values = Lists.newArrayList();
        if (StringUtils.isNotBlank(instanceCode)) {
            params.add("code");
            values.add(StringUtils.trim(instanceCode));
        }

        if (StringUtils.isNotBlank(instanceName)) {
            params.add("name");
            values.add(Restrictions.like("name", StringUtils.trim(instanceName), MatchMode.ANYWHERE));
        }

        if (StringUtils.isNotBlank(clientName)) {
            params.add("clientName");
            values.add(StringUtils.trim(clientName));
        }

        if (status != null) {
            params.add("status");
            values.add(status);
        }
        return instanceService.findPageByNamedParamAndOrder(params, values,
                Lists.newArrayList(order1), page, limit);
    }

    @RequestMapping("/instance/queryJobText")
    @ResponseBody
    public Result<String> queryJobText(String instanceCode) {
        String jobText = instanceContentService.queryByCriterions(Projections.property("jobText"),
                Restrictions.eq("code", instanceCode));
        return Result.successDataResult(jobText);
    }

    @RequestMapping("/instance/queryJobConfig")
    @ResponseBody
    public Result<String> queryJobConfig(String instanceCode) {
        String jobConfig = instanceContentService.queryByCriterions(Projections.property("jobConfig"),
                Restrictions.eq("code", instanceCode));

        if (jobConfig == null) {
            jobConfig = "";
        }
        return Result.successDataResult(jobConfig);
    }

    @PostMapping("/instance/saveJobConfig")
    @ResponseBody
    public Result<String> saveJobConfig(String instanceCode, String jobConfig) {
        try {
            JobServerUtils.validateJobConfig(jobConfig);
            JobInstanceContent instanceContent = instanceContentService.queryJobInstanceContentByCode(instanceCode);
            instanceContent.setJobConfig(jobConfig);
            instanceContentService.updateEntity(instanceContent);
            return Result.successDataResult(jobConfig);
        } catch (Exception e) {
            return Result.failureResult(e.getMessage());
        }
    }

    @RequestMapping("/instance/queryInstanceLog")
    @ResponseBody
    public Result<String> queryInstanceLog(String scheduleDate, String instanceCode) {
        String instanceLogPath = configProperties.getInstanceLogPath();
        String logFile = instanceLogPath + "/" + scheduleDate + "/" + instanceCode + ".log";
        File file = new File(logFile);

        if (!file.exists()) {
            String msg = "访问文件不存在：" + logFile;
            LOG.error(msg);

            return Result.failureResult(msg);
        }

        try {
            LinkedList<String> lines = JobServerUtils.readLogFile(file);
            return Result.successDataResult(StringUtils.join(lines, "\n"));
        } catch (Exception e) {
            LOG.error("访问文件失败：" + logFile, e);
            return Result.failureResult(e.getMessage());
        }
    }

    @RequestMapping("/instance/downloadInstanceLog")
    public void downloadInstanceLog(String scheduleDate, String instanceCode, HttpServletResponse response) {
        String instanceLogPath = configProperties.getInstanceLogPath();
        String logFile = instanceLogPath + "/" + scheduleDate + "/" + instanceCode + ".log";
        File file = new File(logFile);
        outputFile(response, file);
    }

    @RequestMapping("/instance/batchDownloadLogs")
    public void batchDownloadLogs(String[] codes, HttpServletResponse response) throws IOException {
        String fileName = "/tmp/logs-" + System.currentTimeMillis() + ".zip";
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            for (String instanceCode : codes) {
                JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
                if (instance != null) {
                    String instanceLogPath = configProperties.getInstanceLogPath();
                    String scheduleDate = DateUtils.formateDate(instance.getScheduleTime());
                    String logFile = instanceLogPath + "/" + scheduleDate + "/" + instanceCode + ".log";
                    File fileToZip = new File(logFile);

                    if (fileToZip.exists()) {
                        try (FileInputStream fis = new FileInputStream(fileToZip)) {
                            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                            zipOut.putNextEntry(zipEntry);
                            ByteStreams.copy(fis, zipOut);
                        }
                    }
                }
            }
        }

        File file = new File(fileName);
        outputFile(response, file);
    }

    private void outputFile(HttpServletResponse response, File file) {
        response.reset();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
        response.addHeader("Content-Length", "" + file.length());

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[1024 * 1024 * 2];
            int i;
            while ((i = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            out.flush();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @PostMapping("/instance/restart")
    @ResponseBody
    public Result<String> restartInstance(String instanceCode) throws Exception {
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
        if (instance != null) {
            instance.setGmtModified(Instant.now());
            instance.setStatus(InstanceStatus.WAITING);
            instance.setRunTimes(0L);
            instanceService.updateEntity(instance);
        }
        return Result.successResult();
    }

    @PostMapping("/instance/delete")
    @ResponseBody
    public Result<String> deleteInstance(String instanceCode) throws Exception {
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
        if (instance != null) {
            instanceService.deleteEntity(instance);

            String logFile = configProperties.getInstanceLogPath();
            JobServerUtils.deleteLogFile(logFile, instance);
            LOG.info("delete instance {} log file: {}", instanceCode, logFile);
        }

        return Result.successResult();
    }

    @PostMapping("/instance/killJob")
    @ResponseBody
    public Result<String> killJob(String instanceCode) {
        LOG.info("kill job {}", instanceCode);
        if (StringUtils.isBlank(instanceCode)) {
            return Result.failureResult("instanceCode 不能为空");
        }

        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
        if (instance != null) {
            instanceService.updateJobStatusByCode(instanceCode, InstanceStatus.KILLED);
            String appId = instance.getApplicationId();
            killDriver(appId, instanceCode);
            return Result.successResult();
        }

        return Result.failureResult("can not find job instance: " + instanceCode);
    }

    @PostMapping("/instance/queryInstanceStatistics")
    @ResponseBody
    public Result<Map<String, Long>> queryInstanceStatistics() {
        Map<String, Long> data = instanceService.queryInstanceStatistics();
        return Result.successDataResult(data);
    }

    private void killDriver(String appId, String instanceCode) {
        if (StringUtils.isNotBlank(appId)) {
            String driverUrl = driverService.queryDriverAddressByAppId(appId);
            if (StringUtils.isNotBlank(driverUrl)) {
                driverUrl = driverUrl + "/sparkDriver/killJob";
                try {
                    restTemplate.postForObject(driverUrl + "?instanceCode=" + instanceCode,
                            null, String.class);
                } catch (Exception e) {
                    LOG.error("reset server: {} , url: {}, error: \n{}",
                            appId, driverUrl, ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }
}

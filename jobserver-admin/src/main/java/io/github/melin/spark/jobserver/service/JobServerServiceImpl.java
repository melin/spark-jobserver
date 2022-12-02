package io.github.melin.spark.jobserver.service;

import com.gitee.melin.bee.util.MapperUtils;
import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.util.DateUtils;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.util.RandomUniqueIdGenerator;
import io.github.melin.spark.jobserver.rest.dto.JobSubmitRequet;
import io.github.melin.spark.jobserver.rest.dto.InstanceInfo;
import io.github.melin.spark.jobserver.api.SparkJobServerException;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import io.github.melin.spark.jobserver.core.service.JobInstanceContentService;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.gitee.melin.bee.core.support.Result;
import com.gitee.melin.bee.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.github.melin.spark.jobserver.util.Constant.ROOT_SCHEDULE_NODE;

/**
 * huaixin 2022/3/28 4:24 PM
 */
@Transactional
@Service
public class JobServerServiceImpl implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(JobServerServiceImpl.class);

    @Autowired
    private JobInstanceService instanceService;

    @Autowired
    private JobInstanceContentService instanceContentService;

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private ConfigProperties configProperties;

    private RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        restTemplate = new RestTemplate();
    }

    public String submitJobInstance(JobSubmitRequet request) {
        LOG.info("submit job: {}", MapperUtils.toJSONString(request));

        String jobText = request.getJobText();
        if (StringUtils.isBlank(jobText)) {
            throw new SparkJobServerException("jobText can not blank");
        }

        String jobConfig = request.getJobConfig();
        JobServerUtils.validateJobConfig(jobConfig);

        String instanceCode = StringUtils.isNotBlank(request.getInstanceCode()) ? request.getInstanceCode() :
                RandomUniqueIdGenerator.getNewString(32);

        JobInstance.Builder builder = JobInstance.builder()
                .setCode(instanceCode)
                .setName(request.getJobName())
                .setJobType(request.getJobType())
                .setInstanceType(request.getInstanceType())
                .setOwner(request.getOwner())
                .setClusterCode(request.getClusterCode())
                .setMaxRetryCount(request.getMaxRetryCount())
                .setStatus(InstanceStatus.WAITING)
                .setScheduleTime(Instant.ofEpochSecond(request.getScheduleTime()))
                .setClientName(request.getClientName())
                .setCreater(request.getOwner())
                .setGmtCreated(Instant.now());

        if (request.getDependentCodes() == null || request.getDependentCodes().length == 0) {
            builder.setDependentCode(ROOT_SCHEDULE_NODE);
            instanceService.insertEntity(builder.build());
        } else {
            instanceService.insertEntity(builder.build());
            instanceService.saveInstanceDependent(instanceCode, request.getDependentCodes());
        }

        instanceService.saveJobText(instanceCode, jobText, jobConfig);
        return instanceCode;
    }

    public InstanceInfo queryInstanceStatus(String instanceCode) {
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);

        InstanceInfo instanceInfo = new InstanceInfo();
        instanceInfo.setInstanceCode(instanceCode);
        if (instance != null) {
            instanceInfo.setStatus(instance.getStatus());
            if (StringUtils.isNotBlank(instance.getApplicationId())) {
                instanceInfo.setApplicationId(instance.getApplicationId());
            }

            JobInstanceContent instanceContent = instanceContentService.queryJobInstanceContentByCode(instanceCode);
            if (instanceContent != null && instanceContent.getErrorMsg() != null) {
                instanceInfo.setErrorMsg(instanceContent.getErrorMsg());
            }
        } else {
            throw new SparkJobServerException("instanceCode " + instanceCode + " not exists");
        }

        return instanceInfo;
    }

    public List<InstanceInfo> batchQueryInstanceStatus(String[] instanceCodes) {
        List<InstanceInfo> infos = new ArrayList<>();
        for (String instanceCode : instanceCodes) {
            JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
            if (instance != null) {
                InstanceInfo info = new InstanceInfo();
                info.setInstanceCode(instanceCode);
                info.setStatus(instance.getStatus());
                if (StringUtils.isNotBlank(instance.getApplicationId())) {
                    info.setApplicationId(instance.getApplicationId());
                }

                infos.add(info);
            } else {
                LOG.info("instanceCode {} not exists", instanceCode);
            }
        }

        return infos;
    }

    public String queryInstanceLog(String instanceCode) {
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);
        final String scheduleDate = DateUtils.formatDate(instance.getScheduleTime());

        String logFile = configProperties.getInstanceLogPath() + "/" + scheduleDate + "/" + instanceCode + ".log";
        File file = new File(logFile);

        if (!file.exists()) {
            return "访问文件不存在：" + logFile;
        } else {
            try {
                LinkedList<String> lines = JobServerUtils.readLogFile(file);
                return StringUtils.join(lines, "\n");
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return "访问文件失败：" + logFile;
            }
        }
    }

    public String stopInstance(String instanceCode) {
        JobInstance instance = instanceService.queryJobInstanceByCode(instanceCode);

        if (instance != null) {
            InstanceStatus status = instance.getStatus();
            if (InstanceStatus.RUNNING == status || InstanceStatus.WAITING == status) {
                instanceService.updateJobStatusByCode(instanceCode, InstanceStatus.KILLED);
                String applicationId = instance.getApplicationId();
                killDriver(applicationId, instanceCode);
            } else {
                return "current instance status: " + status + ", can not stop";
            }
        } else {
            return "instance code not exist: " + instanceCode;
        }

        return null;
    }

    private void killDriver(String appId, String instanceCode) {
        if (StringUtils.isNotBlank(appId)) {
            String driverUrl = driverService.queryDriverAddressByAppId(appId);
            if (StringUtils.isNotBlank(driverUrl)) {
                driverUrl = driverUrl + "/sparkDriver/killJob";
                try {
                    Result<String> result = RestTemplateUtils.postEntry(restTemplate, driverUrl, "instanceCode", instanceCode);

                    if (!result.isSuccess()) {
                        LOG.error("kill driver failed: {} , url: {}, error: \n{}", appId, driverUrl, result.getData());
                    }
                } catch (Exception e) {
                    LOG.error("kill driver failed: {} , url: {}, error: \n{}", appId, driverUrl, ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }
}

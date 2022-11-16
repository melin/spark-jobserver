package io.github.melin.spark.jobserver.support;

import io.github.melin.spark.jobserver.SparkJobServerConf;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.exception.SwitchYarnQueueException;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by admin on 2018/12/24.
 */
@Service
@DependsOn("clusterManager")
public class YarnClientService {
    private static final Logger LOG = LoggerFactory.getLogger(YarnClientService.class);

    private static final Logger SERVER_LOGGER = LoggerFactory.getLogger("serverMetricsMsg");

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private ClusterConfig clusterConfig;

    public YarnClient createYarnClient(String clusterCode) {
        Configuration configuration = clusterManager.getHadoopConf(clusterCode);
        YarnClient innerClient = YarnClient.createYarnClient();
        innerClient.init(configuration);
        innerClient.start();
        return innerClient;
    }

    @Transactional
    public String killApplication(String clusterCode, String appId) {
        try {
            SERVER_LOGGER.info("Prepare to kill server: " + appId);
            String driverUrl = driverService.queryDriverAddressByAppId(appId);
            if (StringUtils.isBlank(driverUrl)) {
                SERVER_LOGGER.error("no valid server: " + appId);
                return "error: no valid server";
            }

            killApplicationOnYarn(clusterCode, appId);
            return "success";
        } catch (Exception e) {
            SERVER_LOGGER.error("kill application error: " + appId, e);
            return e.getMessage();
        }
    }

    @Transactional
    public void killApplicationOnYarn(String clusterCode, String applicationId) {
        try {
            this.killYarnApp(clusterCode, applicationId);
        } catch (Throwable e) {
            SERVER_LOGGER.info("YarnClient error " + applicationId, e);
        } finally {
            driverService.deleteJobServerByAppId(applicationId);
        }
    }

    @Transactional
    public void closeJobServer(String clusterCode, String appId, boolean shareDriver) {
        LOG.warn("close jobserver: {}, shareDriver: {}", appId, shareDriver);
        driverService.updateDriverStatusIdle(appId);
        if (shareDriver) {
            SparkDriver driver = driverService.queryDriverByAppId(appId);
            if (driver != null) {
                int runCount = driver.getInstanceCount();
                int maxInstanceCount = clusterConfig.getInt(clusterCode, SparkJobServerConf.JOBSERVER_DRIVER_RUN_MAX_INSTANCE_COUNT);
                LOG.info("当前driver {} 超过运行次数 {}/{}", appId, runCount, maxInstanceCount);
                if (runCount >= maxInstanceCount) {
                    this.killApplication(clusterCode, appId);
                }
            } else {
                this.killApplication(clusterCode, appId);
            }
        } else {
            this.killApplication(clusterCode, appId);
        }
    }

    public YarnApplicationState getApplicationStatus(String clusterCode, String appId) {
        try {
            ApplicationReport report = this.getYarnApplicationReport(clusterCode, appId);
            if (report != null) {
                return report.getYarnApplicationState();
            } else {
                return null;
            }
        } catch (Exception e) {
            if (e.getCause() instanceof ApplicationNotFoundException) {
                driverService.deleteJobServerByAppId(appId);
                LOG.warn("applicationId {} not exists", appId);
            } else {
                LOG.error("获取yarn state 失败：" + appId, e);
            }

            return null;
        }
    }

    public ApplicationReport getYarnApplicationReport(String clusterCode, String appId) {
        return clusterManager.runSecured(clusterCode, () -> {
            YarnClient yarnClient = createYarnClient(clusterCode);
            ApplicationId applicationId = ConverterUtils.toApplicationId(appId);
            return yarnClient.getApplicationReport(applicationId);
        });
    }

    public void moveApplicationAcrossQueues(String clusterCode, String appId, String yarnQueue) {
        clusterManager.runSecured(clusterCode, () -> {
            ApplicationId applicationId = ConverterUtils.toApplicationId(appId);

            List<String> queues = Lists.newArrayList();
            YarnClient yarnClient = createYarnClient(clusterCode);
            for (QueueInfo queueInfo : yarnClient.getAllQueues()) {
                queues.add(queueInfo.getQueueName());
            }

            if (!queues.contains(yarnQueue)) {
                String msg = "当前队列不存在: " + yarnQueue + ", 集群所有yarn 队列: " + StringUtils.join(queues, ",");
                throw new SwitchYarnQueueException(msg);
            }

            yarnClient.moveApplicationAcrossQueues(applicationId, yarnQueue);
            return null;
        });

        driverService.updateYarnQueue(appId, yarnQueue);
    }

    public void killYarnApp(String clusterCode, String appId) throws Exception {
        if (StringUtils.isBlank(appId)) {
            LOG.info("kill yarn app exception: appId is empty");
            return;
        }

        ApplicationId applicationId = ConverterUtils.toApplicationId(appId);

        clusterManager.runSecured(clusterCode, () -> {
            YarnClient yarnClient = createYarnClient(clusterCode);
            ApplicationReport applicationReport = yarnClient.getApplicationReport(applicationId);
            YarnApplicationState state = applicationReport.getYarnApplicationState();
            if (state == YarnApplicationState.RUNNING || state == YarnApplicationState.ACCEPTED) {
                LOG.info("app: {} current status: {}, will kill", appId, state.name());
                yarnClient.killApplication(applicationId);
            }
            return null;
        });
    }
}

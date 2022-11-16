package io.github.melin.spark.jobserver.deployment;

import io.github.melin.spark.jobserver.deployment.dto.DriverInfo;
import io.github.melin.spark.jobserver.deployment.dto.JobInstanceInfo;
import io.github.melin.spark.jobserver.support.ClusterConfig;
import io.github.melin.spark.jobserver.support.ClusterManager;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.web.controller.DriverController;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.ComputeType;
import io.github.melin.spark.jobserver.core.exception.SubmitTimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.spark.launcher.SparkAppHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.github.melin.spark.jobserver.SparkJobServerConf.*;
import static io.github.melin.spark.jobserver.core.enums.DriverInstance.NEW_INSTANCE;
import static io.github.melin.spark.jobserver.core.enums.DriverInstance.SHARE_INSTANCE;

/**
 * huaixin 2022/4/14 21:09
 */
@Service
public class YarnSparkDriverSubmit extends AbstractSubmitService {

    private static final Logger LOG = LoggerFactory.getLogger(YarnSparkDriverSubmit.class);

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private ClusterConfig clusterConfig;

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Value("${spring.profiles.active}")
    protected String profiles;

    @Override
    public DriverInfo allocateDriver(JobInstanceInfo job, ComputeType computeType, boolean shareDriver) {
        String clusterCode = job.getClusterCode();
        int maxInstanceCount = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_RUN_MAX_INSTANCE_COUNT);
        long minDriverId = clusterConfig.getLong(clusterCode, JOBSERVER_DRIVER_MIN_PRIMARY_ID);
        List<SparkDriver> drivers = driverService.queryAvailableApplication(maxInstanceCount, minDriverId);
        if (drivers.size() > 0) {
            for (SparkDriver driver : drivers) {
                int version = driver.getVersion();
                int batch = driverService.updateServerLocked(driver.getApplicationId(), version);
                if (batch <= 0) {
                    continue;
                }
                String driverAddress = driver.getSparkDriverUrl();
                DriverInfo driverInfo = new DriverInfo(SHARE_INSTANCE, driver.getApplicationId(), driver.getId());
                driverInfo.setDriverAddress(driverAddress);
                driverInfo.setYarnQueue(driver.getYarnQueue());
                return driverInfo;
            }
        }

        while (!redisLeaderElection.trylock()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception ignored) {}
        }

        try {
            //未分配到server的请求重新申请server
            checkLocalAvailableMemory();
            checkMaxDriverCount(clusterCode);
            clusterManager.checkYarnResourceLimit(job.getClusterCode());

            Long driverId = initSparkDriver(job.getClusterCode(), shareDriver);
            DriverInfo driverInfo = new DriverInfo(NEW_INSTANCE, driverId);

            String yarnQueue = clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_YAEN_QUEUE_NAME);
            driverInfo.setYarnQueue(yarnQueue);
            return driverInfo;
        } finally {
            redisLeaderElection.deletelock();
        }
    }

    public void buildJobServer(Cluster cluster) {
        Long driverId = null;
        SparkAppHandle sparkAppHandle = null;
        String clusterCode = cluster.getCode();
        try {
            //未分配到server的请求重新申请server
            checkLocalAvailableMemory();
            checkMaxDriverCount(clusterCode);
            clusterManager.checkYarnResourceLimit(clusterCode);

            JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
            String yarnQueue = clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_YAEN_QUEUE_NAME);
            driverId = initSparkDriver(clusterCode, true);
            LOG.info("预启动 driver Id: {}", driverId);

            sparkAppHandle = startApplication(jobInstanceInfo, cluster.getCode(), driverId, yarnQueue);
            long appSubmitTime = System.currentTimeMillis();
            SparkAppHandle.State state = sparkAppHandle.getState();
            String applicationId = "";
            boolean updateApplicationId = false;
            int driverSubmitTimeOut = config.getDriverSubmitTimeOutSeconds();
            while (state != SparkAppHandle.State.RUNNING) {
                applicationId = sparkAppHandle.getAppId();
                if (System.currentTimeMillis() - appSubmitTime > (driverSubmitTimeOut * 1000L)) {
                    sparkAppHandle.kill();
                    if (StringUtils.isNotBlank(applicationId)) {
                        yarnClientService.killYarnApp(cluster.getCode(), applicationId);
                    }
                    throw new SubmitTimeoutException(", Submit to yarn timeout(" + driverSubmitTimeOut +
                            "s), applicationId: " + applicationId);
                }
                if (sparkAppHandle.getState() == SparkAppHandle.State.KILLED) {
                    throw new RuntimeException("Submit to yarn failed , yarn status: " + state.name());
                }

                if (StringUtils.isBlank(applicationId)) {
                    if (sparkAppHandle.getState() == SparkAppHandle.State.FAILED) {
                        if (sparkAppHandle.getError().isPresent()) {
                            String error = ExceptionUtils.getStackTrace(sparkAppHandle.getError().get());
                            throw new RuntimeException("启动 jobserver 失败, Spark App Status："
                                    + sparkAppHandle.getState().name() + ", 失败原因: " + error);
                        } else {
                            throw new RuntimeException("启动 jobserver 失败, Spark App Status："
                                    + sparkAppHandle.getState().name());
                        }
                    }
                } else {
                    //启动jobserver 失败
                    ApplicationReport applicationReport = yarnClientService.getYarnApplicationReport(cluster.getCode(), applicationId);
                    if (applicationReport.getYarnApplicationState() == YarnApplicationState.FAILED) {
                        if (sparkAppHandle.getError().isPresent()) {
                            Throwable error = sparkAppHandle.getError().get();
                            throw new RuntimeException("预启动 jobserver 失败, applicationId " + applicationId, error);
                        } else {
                            throw new RuntimeException("预启动 jobserver 失败, applicationId " + applicationId);
                        }
                    }
                }

                if (!updateApplicationId && StringUtils.isNotBlank(applicationId)) {
                    SparkDriver driver = driverService.getEntity(driverId);
                    if (driver != null) {
                        driver.setApplicationId(applicationId);
                        driverService.updateEntity(driver);
                    }
                    updateApplicationId = true;
                }

                Thread.sleep(3000);
                state = sparkAppHandle.getState();
            }

            applicationId = sparkAppHandle.getAppId();
            LOG.info("start share jobserver: {}", applicationId);

            DriverController.sparkLauncherFailedMsg = "";
        } catch (Exception e) {
            LOG.info("启动jobserver 失败" + e.getMessage(), e);
            if (driverId != null) {
                LOG.info("delete driverId: {}", driverId);
                driverService.deleteEntity(driverId);
            }

            DriverController.sparkLauncherFailedMsg = "启动jobserver 失败: " + e.getMessage();

            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception ignored) {}
        } finally {
            if (sparkAppHandle != null) {
                sparkAppHandle.kill();
            }
        }
    }
}

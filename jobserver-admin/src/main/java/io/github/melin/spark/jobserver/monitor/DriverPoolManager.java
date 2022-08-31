package io.github.melin.spark.jobserver.monitor;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.deployment.YarnDriverSubmit;
import io.github.melin.spark.jobserver.support.ClusterConfig;
import io.github.melin.spark.jobserver.support.YarnClientService;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.gitee.melin.bee.util.ThreadUtils;
import io.github.melin.spark.jobserver.support.leader.LeaderTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.melin.spark.jobserver.SparkJobServerConf.*;

/**
 * jobserver pool 大小控制
 *
 * @author melin 2021/9/19 10:19 下午
 */
@Component
public class DriverPoolManager implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger("serverMinitor");

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private YarnClientService yarnClientService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ClusterConfig clusterConfig;

    @Autowired
    private YarnDriverSubmit yarnDriverSubmit;

    private final ScheduledExecutorService scheduledExecutorService =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("check-yarn-app");

    @Override
    public void afterPropertiesSet() throws Exception {
        redisLeaderElection.buildLeader(LeaderTypeEnum.DRIVER_POOL_MANAGER);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (redisLeaderElection.checkLeader(LeaderTypeEnum.DRIVER_POOL_MANAGER)) {
                List<Cluster> clusters = clusterService.findByNamedParam("status", 1);
                for (Cluster cluster : clusters) {
                    LOG.info("monitor driver pool");

                    stopMaxIdleJobserver(cluster);
                    startMinJobServer(cluster);
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 1. 当超过空闲时间，停止jobserver，保持jobserver.idle.min.count 数量.
     * 2. jobserver运行jobserver.run.max.instance.count 实例数量，停止jobserver
     */
    private void stopMaxIdleJobserver(Cluster cluster) {
        try {
            String clusterCode = cluster.getCode();
            List<SparkDriver> allIdleDrivers = driverService.queryAllIdleDrivers(clusterCode);
            int driverMinCount = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_MIN_COUNT);
            int driverMaxCount = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_MAX_COUNT);
            int removed = allIdleDrivers.size() - driverMaxCount;
            Instant current = Instant.now();

            // 删除超过空闲时间的driver
            for (int i = 0; i < removed; i++) {
                SparkDriver driver = allIdleDrivers.get(i);
                Instant gmtModified = driver.getGmtModified();
                long idleSeconds = current.getEpochSecond() - gmtModified.getEpochSecond();
                int maxIdleTimeSeconds = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_MAX_IDLE_TIME_SECONDS);
                String appId = driver.getApplicationId();
                if (idleSeconds > maxIdleTimeSeconds) {
                    yarnClientService.killApplication(clusterCode, appId);
                    LOG.info("driver 最小空闲个数: {}, {} 超过最大空闲时间 {}，将被终止", driverMinCount, appId, maxIdleTimeSeconds);
                }
            }

            // 删除超过运行次数的driver
            int maxInstanceCount = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_RUN_MAX_INSTANCE_COUNT);
            for (SparkDriver driver : allIdleDrivers) {
                if (driver.getInstanceCount() >= maxInstanceCount) {
                    String appId = driver.getApplicationId();
                    driverService.deleteJobServerByAppId(appId);
                    yarnClientService.killApplication(clusterCode, appId);
                    LOG.info("driver {} 运行次数超过最大次数: {}", appId, maxInstanceCount);
                }
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * 预启动 jobserver
     */
    private void startMinJobServer(Cluster cluster) {
        try {
            int minDriverCount = clusterConfig.getInt(cluster.getCode(), JOBSERVER_DRIVER_MIN_COUNT);
            long driverCount = driverService.queryCount();
            while (minDriverCount > driverCount) {
                yarnDriverSubmit.buildJobServer(cluster);
                driverCount = driverService.queryCount();
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage());
        }
    }
}

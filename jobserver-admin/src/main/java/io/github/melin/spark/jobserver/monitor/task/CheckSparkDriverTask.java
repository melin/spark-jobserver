package io.github.melin.spark.jobserver.monitor.task;

import io.github.melin.spark.jobserver.SparkJobServerConf;
import io.github.melin.spark.jobserver.support.ClusterConfig;
import io.github.melin.spark.jobserver.support.ClusterManager;
import io.github.melin.spark.jobserver.support.YarnClientService;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.util.DateUtils;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import io.github.melin.spark.jobserver.support.leader.LeaderTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static io.github.melin.spark.jobserver.core.enums.DriverStatus.*;
import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.FAILED;

/**
 * huaixin 2022/3/19 3:52 PM
 */
@Service
public class CheckSparkDriverTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger("serverMinitor");

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    private YarnClientService yarnClientService;

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private ClusterManager clusterManager;

    @Autowired
    private JobInstanceService instanceService;

    @Autowired
    private ClusterConfig clusterConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.profiles.active}")
    protected String profiles;

    @Override
    public void run() {
        if (!redisLeaderElection.checkLeader(LeaderTypeEnum.DRIVER_POOL_MONITOR)) {
            return;
        }

        LOG.info("check spark driver");

        // 清理yarn 上在运行app，但系统已经关闭的driver
        try {
            String appNamePrefix = JobServerUtils.appNamePrefix(profiles);
            clusterManager.getCluerCodes().forEach(clusterCode -> {
                clusterManager.runSecured(clusterCode, () -> {
                    YarnClient yarnClient = yarnClientService.createYarnClient(clusterCode);
                    if (yarnClient != null) {
                        try {
                            yarnClient.getApplications(EnumSet.of(YarnApplicationState.RUNNING, YarnApplicationState.ACCEPTED)).forEach(applicationReport -> {
                                String appId = applicationReport.getApplicationId().toString();
                                String appName = applicationReport.getName();
                                String appState = applicationReport.getYarnApplicationState().name();
                                long createTime = applicationReport.getStartTime();

                                if (StringUtils.startsWith(appName, appNamePrefix)
                                        && (System.currentTimeMillis() - createTime) > (10 * 60 * 1000)) {
                                    SparkDriver driver = driverService.queryDriverByAppId(appId);

                                    if (driver == null) {
                                        try {
                                            ApplicationId applicationId = ConverterUtils.toApplicationId(appId);
                                            yarnClient.killApplication(applicationId);
                                            LOG.info("driver not exists, yarn app: {}, status: {},  killed successfully", appId, appState);
                                        } catch (Exception e) {
                                            LOG.info("yarn app: {}, status: {},  killed failure", appId, appState);
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);
                        } finally {
                            yarnClient.stop();
                        }
                    }

                    return null;
                });
            });
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        // 清理僵死 driver 记录
        try {
            final Instant oneHourBefore = Instant.now().minus(15, ChronoUnit.MINUTES);
            Criterion statusCriterion = Restrictions.in("status", INIT, LOCKED);
            Criterion oneHourBeforeCriterion = Restrictions.lt("gmtModified", oneHourBefore);
            List<SparkDriver> drivers = driverService.findByCriterions(statusCriterion, oneHourBeforeCriterion);

            for (SparkDriver driver : drivers) {
                driverService.deleteEntity(driver);
                String applicationId = driver.getApplicationId();
                if (StringUtils.isNotBlank(applicationId)) {
                    LOG.warn("[DriverCheck] delete driver: {}, status: {}, gmtModified: {}",
                            applicationId, driver.getStatus().getName(), DateUtils.formateDateTime(driver.getGmtModified()));
                }
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        // 清理driver 记录存在，但applicationId 已经 终止 或 完成
        try {
            List<SparkDriver> drivers = driverService.findAllEntity();
            for (SparkDriver driver : drivers) {
                String applicationId = driver.getApplicationId();
                if (StringUtils.isNotBlank(applicationId)) {
                    YarnApplicationState state = yarnClientService.getApplicationStatus(driver.getClusterCode(), applicationId);
                    if (YarnApplicationState.FINISHED == state || YarnApplicationState.FAILED == state
                            || YarnApplicationState.KILLED == state) {
                        LOG.warn("[DriverCheck] delete driver {} applicationId: {}", driver.getId(), applicationId);
                        driverService.deleteEntity(driver);
                    }
                }
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        // 修复 jobserver 完成状态，如果长期处于完成状态，关闭 driver
        try {
            final Instant threeMinBefore = Instant.now().minus(3, ChronoUnit.MINUTES);
            Criterion threeMinBeforeCrt = Restrictions.lt("gmtModified", threeMinBefore);
            Criterion statusCrt = Restrictions.eq("status", FINISHED);
            List<SparkDriver> drivers = driverService.findByCriterions(statusCrt, threeMinBeforeCrt);

            for (SparkDriver driver : drivers) {
                String applicationId = driver.getApplicationId();
                String clusterCode = driver.getClusterCode();

                yarnClientService.closeJobServer(clusterCode, applicationId, driver.isShareDriver());
                LOG.warn("[DriverCheck]修复 jobserver 完成状态: {}, gmtModified: {}",
                        applicationId, DateUtils.formateDateTime(driver.getGmtModified()));
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        // 非共享 driver，超过一定时间没有关闭，定期清理
        try {
            final Instant threeMinBefore = Instant.now().minus(3, ChronoUnit.MINUTES);
            Criterion threeMinBeforeCriterion = Restrictions.lt("gmtModified", threeMinBefore);
            List<SparkDriver> drivers = driverService.findByNamedParam(
                    "status", IDLE, "shareDriver", false,
                    "gmtModified", threeMinBeforeCriterion);

            for (SparkDriver driver : drivers) {
                String applicationId = driver.getApplicationId();
                String clusterCode = driver.getClusterCode();

                yarnClientService.closeJobServer(clusterCode, applicationId, false);
                LOG.warn("[DriverCheck]关闭非共享jobserver: {}, gmtModified: {}",
                        applicationId, DateUtils.formateDateTime(driver.getGmtModified()));
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        try {
            clusterManager.getCluerCodes().forEach(clusterCode -> {
                // 关闭老版本jobserver
                long minDriverId = clusterConfig.getLong(clusterCode, SparkJobServerConf.JOBSERVER_DRIVER_MIN_PRIMARY_ID);
                if (minDriverId > 0) {
                    Criterion idCriterion = Restrictions.lt("id", minDriverId);
                    List<SparkDriver> drivers = driverService.findByNamedParam("status", IDLE,
                            "id", idCriterion, "clusterCode", clusterCode);

                    for (SparkDriver driver : drivers) {
                        String applicationId = driver.getApplicationId();
                        yarnClientService.closeJobServer(clusterCode, applicationId, true);

                        LOG.warn("[DriverCheck]关闭老版本jobserver: {}", applicationId);
                    }
                }
            });
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }

        //作业实例为运行 或 LOCKED 状态，jobserver 已经关闭，设置实例为失败状态
        try {
            String sql = "select * from (SELECT a.code, b.application_id FROM (select id, code, application_id " +
                    "from sjs_job_instance where instance_type<>'DEV' and status in ('RUNNING', 'LOCKED') and application_id is not null) a \n" +
                    "left outer join sjs_spark_driver b on a.application_id = b.application_id\n" +
                    ") c where c.application_id is null";

            List<Map<String, Object>> jobServers = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> map : jobServers) {
                String code = (String) map.get("code");
                String appId = (String) map.get("application_id");

                LOG.warn("[DriverCheck]instance {} 为运行状态，jobserver {} 已经关闭", code, appId);
                instanceService.updateJobStatusByCode(code, FAILED);
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }
    }
}

package io.github.melin.spark.jobserver.core.service;

import io.github.melin.spark.jobserver.core.dao.SparkDriverDao;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import com.gitee.melin.bee.core.hibernate5.HibernateBaseDao;
import com.gitee.melin.bee.core.service.BaseServiceImpl;
import com.gitee.melin.bee.util.NetUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static io.github.melin.spark.jobserver.core.enums.DriverType.DRIVER_SERVER;

@Service
@Transactional
public class SparkDriverService extends BaseServiceImpl<SparkDriver, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(SparkDriverService.class);

    @Autowired
    private SparkDriverDao sparkDriverDao;

    private String hostName = "";

    public SparkDriverService() {
        hostName = NetUtils.getLocalHost();
    }

    @Override
    public HibernateBaseDao<SparkDriver, Long> getHibernateBaseDao() {
        return sparkDriverDao;
    }

    @Transactional(readOnly = true)
    public SparkDriver queryDriverByAppId(String applicationId) {
        return this.queryByNamedParam("applicationId", applicationId);
    }

    @Transactional(readOnly = true)
    public String queryDriverAddressByAppId(String applicationId) {
        SparkDriver driver = this.queryDriverByAppId(applicationId);
        if (driver != null) {
            return driver.getSparkDriverUrl();
        } else {
            return null;
        }
    }

    @Transactional
    public void updateServerRunning(SparkDriver driver) {
        driver.setStatus(DriverStatus.RUNNING);
        driver.setGmtModified(Instant.now());
        driver.setInstanceCount(driver.getInstanceCount() + 1);
        this.updateEntity(driver);
    }

    @Transactional
    public void updateServerFinished(String appId) {
        try {
            String hql = "update SparkDriver set status=:afterStatus, gmtModified=:gmtModified " +
                    "where status=:beforeStatus and applicationId=:appId";

            int batch = this.deleteOrUpdateByHQL(hql,
                    new String[]{"afterStatus", "gmtModified", "beforeStatus", "appId"},
                    new Object[]{DriverStatus.FINISHED, Instant.now(), DriverStatus.RUNNING, appId});
            if (batch == 1) {
                LOG.info("update driver: {} status finished", appId);
            }
        } catch (Exception e) {
            LOG.error("update driver locked error: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<SparkDriver> queryAllIdleDrivers(String clusterCode) {
        return findByNamedParamAndOrder(new String[] {"driverType", "status", "clusterCode"},
                new Object[]{"driverServer", DriverStatus.IDLE, clusterCode},
                Order.asc("gmtModified"));
    }

    @Transactional
    public void updateYarnQueue(String appId, String yarnQueue) {
        SparkDriver driver = queryDriverByAppId(appId);
        if (driver != null) {
            driver.setYarnQueue(yarnQueue);
            this.updateEntity(driver);
        }
    }

    @Transactional
    public void updateDriverStatusIdle(String appId) {
        SparkDriver driver = queryDriverByAppId(appId);
        if (driver != null) {
            driver.setStatus(DriverStatus.IDLE);
            driver.setGmtModified(Instant.now());
            this.updateEntity(driver);
            LOG.info("update driver: {} status finished", appId);
        }
    }

    @Transactional
    public void deleteJobServerByAppId(String appId) {
        SparkDriver driver = queryDriverByAppId(appId);
        if (driver != null) {
            LOG.info("delete driver: {}, appId: {}", driver.getId(), driver.getApplicationId());
            this.deleteEntity(driver);
        }
    }

    /**
     * 清空driver logThread hostName
     */
    @Transactional
    public void clearCurrentLogServer() {
        try {
            String hql = "update SparkDriver set logServer = null where logServer = :logServer";
            int count = this.deleteOrUpdateByHQL(hql, "logServer", hostName);
            LOG.info("清空log server count: {}", count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Transactional
    public boolean lockCurrentLogServer(String appId) {
        try {
            String hql = "update SparkDriver set logServer=:logServer where logServer is null and applicationId=:appId";
            int count = this.deleteOrUpdateByHQL(hql, new String[]{"logServer", "appId"},
                    new Object[]{hostName, appId});
            if (count > 0) {
                LOG.info("获取 log server applicationId: {}", appId);
                return true;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return false;
    }

    @Transactional
    public List<SparkDriver> queryEmptyLogServers() {
        Criterion logServerCtr = Restrictions.isNull("logServer");
        Criterion driverTypeCtr = Restrictions.eq("driverType", "driverServer");
        return findByCriterions(logServerCtr, driverTypeCtr);
    }

    @Transactional
    public int updateServerLocked(String applicationId, int version) {
        String hql = "update SparkDriver set status=:afterStatus, gmtModified=:gmtModified, version=:afterVersion " +
                "where status=:beforeStatus and applicationId=:applicationId and version =:beforeVersion";

        int batch = this.deleteOrUpdateByHQL(hql, new String[]{"afterStatus", "gmtModified", "afterVersion",
                        "beforeStatus", "applicationId", "beforeVersion"},
                new Object[]{DriverStatus.LOCKED, Instant.now(), version + 1, DriverStatus.IDLE, applicationId, version});
        if (batch == 1) {
            LOG.info("update driver: {} status locked", applicationId);
        }
        return batch;
    }

    @Transactional(readOnly = true)
    public List<SparkDriver> queryAvailableApplication(int maxInstanceCount, Long minJobserverId) {
        Criterion statusCrt = Restrictions.eq("status", DriverStatus.IDLE);
        Criterion shareDriverCrt = Restrictions.eq("shareDriver", true);
        Criterion instanceCountCrt = Restrictions.lt("instanceCount", maxInstanceCount);
        Criterion driverTypeCrt = Restrictions.eq("driverType", DRIVER_SERVER);

        if (minJobserverId > 0) {
            Criterion idCrt = Restrictions.gt("id", minJobserverId);
            return findByCriterions(Order.asc("gmtCreated"), statusCrt, shareDriverCrt,
                    instanceCountCrt, driverTypeCrt, idCrt);
        } else {
            return findByCriterions(Order.asc("gmtCreated"), statusCrt, shareDriverCrt,
                    instanceCountCrt, driverTypeCrt);
        }
    }
}

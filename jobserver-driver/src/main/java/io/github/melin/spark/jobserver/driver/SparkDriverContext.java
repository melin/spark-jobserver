package io.github.melin.spark.jobserver.driver;

import io.github.melin.spark.jobserver.core.dto.InstanceDto;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import io.github.melin.spark.jobserver.driver.listener.JobServerListener;
import io.github.melin.spark.jobserver.driver.listener.MetricsData;
import io.github.melin.spark.jobserver.driver.support.TaskLogThread;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import com.gitee.melin.bee.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * huaixin 2022/4/6 10:40 PM
 */
@Service
public class SparkDriverContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkDriverContext.class);

    private volatile DriverStatus status = DriverStatus.IDLE;

    /**
     * 是否用户终止作业
     */
    private boolean userStopTask = false;

    @Autowired
    private JobServerListener sparkListener;

    @Autowired
    private ServerPortService serverPortService;

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private TaskLogThread logThread;

    public void initSparkDriver(Long driverId) {
        SparkDriverEnv.getSparkContext().addSparkListener(sparkListener);
        SparkDriver driver = driverService.getEntity(driverId);
        if (driver == null) {
            throw new RuntimeException("No driver Id: " + driverId);
        }
        driver.setServerIp(NetUtils.getLocalHost());
        driver.setServerPort(serverPortService.getPort());
        driver.setStatus(DriverStatus.IDLE);
        String applicationId = SparkDriverEnv.getApplicationId();
        driver.setApplicationId(applicationId);
        driver.setCreater("admin");

        Instant nowDate = Instant.now();
        driver.setGmtCreated(nowDate);
        driver.setGmtModified(nowDate);
        driverService.updateEntity(driver);
    }

    public void startDriver() {
        LOGGER.info("startQueySparkStageLog");
        MetricsData.clearCaches();
        LogUtils.executorLogSet.clear();
        this.setUserStopTask(false);

        status = DriverStatus.RUNNING;
        logThread.startQueySparkStageLog();
    }

    public void stopDriver(InstanceDto instanceDto) {
        LOGGER.info("stopQueySparkStageLog");
        logThread.stopQueySparkStageLog();

        Long driverId = instanceDto.getDriverId();
        SparkDriver driver = driverService.getEntity(driverId);
        LOGGER.info("driver {} run task finished，update status idle", driver.getApplicationId());

        Instant nowDate = Instant.now();
        driver.setGmtModified(nowDate);
        status = DriverStatus.IDLE;
        driver.setStatus(status);
        driverService.updateEntity(driver);
    }

    public DriverStatus getStatus() {
        return status;
    }

    public boolean isUserStopTask() {
        return userStopTask;
    }

    public void setUserStopTask(boolean userStopTask) {
        this.userStopTask = userStopTask;
    }
}

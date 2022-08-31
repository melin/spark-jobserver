package io.github.melin.spark.jobserver.deployment.dto;

import io.github.melin.spark.jobserver.core.enums.DriverInstance;

/**
 * Created by admin on 2017/7/1.
 */
public class DriverInfo {
    private final DriverInstance driverInstance;

    private boolean shareDriver;

    private String applicationId;

    private Long driverId;

    private String message;

    private String driverAddress;

    private String yarnQueue;

    public DriverInfo(DriverInstance driverInstance) {
        this.driverInstance = driverInstance;
    }

    public DriverInfo(DriverInstance driverInstance, String message) {
        this.driverInstance = driverInstance;
        this.message = message;
    }

    public DriverInfo(DriverInstance driverInstance, Long driverId) {
        this.driverInstance = driverInstance;
        this.driverId = driverId;
    }

    public DriverInfo(DriverInstance driverInstance, String applicationId, Long driverId) {
        this.driverInstance = driverInstance;
        this.applicationId = applicationId;
        this.driverId = driverId;
    }

    public DriverInstance getDriverInstType() {
        return driverInstance;
    }

    public boolean isShareDriver() {
        return shareDriver;
    }

    public void setShareDriver(boolean shareDriver) {
        this.shareDriver = shareDriver;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getYarnQueue() {
        return yarnQueue;
    }

    public void setYarnQueue(String yarnQueue) {
        this.yarnQueue = yarnQueue;
    }
}

package io.github.melin.spark.jobserver.driver.model;

import io.github.melin.spark.jobserver.core.enums.DriverType;
import com.beust.jcommander.Parameter;

public class DriverParam {

    @Parameter(names = "-j", description = "driverId", required = true)
    private Long driverId;

    @Parameter(names = "-type", description = "driver type", required = true,
            converter = DriverTypeConverter.class)
    private DriverType driverType;

    @Parameter(names = "-conf", description = "driver config", required = true)
    private String config;

    @Parameter(names = "-k", description = "kerberos enabled", required = false, arity = 1)
    private boolean kerberosEnabled = false;

    @Parameter(names = "-ku", description = "kerberos user", required = false)
    private String kerberosUser;

    @Parameter(names = "-hive", description = "hive enabled", required = false)
    private boolean hiveEnable = false;

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public boolean isKerberosEnabled() {
        return kerberosEnabled;
    }

    public void setKerberosEnabled(boolean kerberosEnabled) {
        this.kerberosEnabled = kerberosEnabled;
    }

    public String getKerberosUser() {
        return kerberosUser;
    }

    public void setKerberosUser(String kerberosUser) {
        this.kerberosUser = kerberosUser;
    }

    public boolean isHiveEnable() {
        return hiveEnable;
    }

    public void setHiveEnable(boolean hiveEnable) {
        this.hiveEnable = hiveEnable;
    }
}

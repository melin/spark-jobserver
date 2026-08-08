package io.github.melin.spark.jobserver.core.util;

import io.github.melin.spark.jobserver.core.enums.ClusterType;
import java.util.Objects;
import org.springframework.util.Assert;

public class EngineId {
    private final String regionCode;

    private final ClusterType clusterType;

    private final String clusterCode;

    private final String engineCode;

    private final ClusterId clusterId;

    public static EngineId of(ClusterId clusterId, String engineCode) {
        return new EngineId(clusterId, engineCode);
    }

    public static EngineId of(String regionCode, ClusterType clusterType, String clusterCode, String engineCode) {
        return new EngineId(regionCode, clusterType, clusterCode, engineCode);
    }

    private EngineId(ClusterId clusterId, String engineCode) {
        Assert.notNull(clusterId, "clusterId is null");
        Assert.hasText(engineCode, "engineCode is blank");

        this.regionCode = clusterId.getRegionCode();
        this.clusterType = clusterId.getClusterType();
        this.clusterCode = clusterId.getClusterCode();
        this.engineCode = engineCode;
        this.clusterId = clusterId;
    }

    private EngineId(String regionCode, ClusterType clusterType, String clusterCode, String engineCode) {
        Assert.hasText(clusterCode, "clusterCode is blank");
        Assert.hasText(engineCode, "engineCode is blank");

        this.regionCode = regionCode;
        this.clusterType = clusterType;
        this.clusterCode = clusterCode;
        this.engineCode = engineCode;
        this.clusterId = ClusterId.of(regionCode, clusterType, clusterCode);
    }

    public String getEngineCode() {
        return engineCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getClusterCode() {
        return clusterCode;
    }

    public ClusterId getClusterId() {
        return clusterId;
    }

    public ClusterType getClusterType() {
        return clusterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngineId that = (EngineId) o;
        return Objects.equals(regionCode, that.regionCode)
                && Objects.equals(clusterCode, that.clusterCode)
                && Objects.equals(engineCode, that.engineCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionCode, clusterCode, engineCode);
    }

    @Override
    public String toString() {
        return "EngineId{" + regionCode + ", " + clusterCode + ", " + engineCode + "}";
    }
}

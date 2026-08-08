package io.github.melin.spark.jobserver.core.util;

import io.github.melin.spark.jobserver.core.enums.ClusterType;
import java.util.Objects;
import org.springframework.util.Assert;

public class ClusterId {
    private String regionCode;

    private ClusterType clusterType;

    private String clusterCode;

    public static ClusterId of(String regionCode, ClusterType clusterType, String clusterCode) {
        return new ClusterId(regionCode, clusterType, clusterCode);
    }

    private ClusterId(String regionCode, ClusterType clusterType, String clusterCode) {
        Assert.hasText(clusterCode, "clusterCode is blank");
        Assert.notNull(clusterType, "clusterType is null");

        this.regionCode = regionCode;
        this.clusterType = clusterType;
        this.clusterCode = clusterCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getClusterCode() {
        return clusterCode;
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
        ClusterId that = (ClusterId) o;
        return Objects.equals(regionCode, that.regionCode) && Objects.equals(clusterCode, that.clusterCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionCode, clusterCode);
    }

    @Override
    public String toString() {
        return "ClusterId{" + regionCode + ", " + clusterCode + "}";
    }
}

package io.github.melin.spark.jobserver.deployment.dto;

/**
 * @author melin 2021/7/20 1:11 下午
 */
public class YarnResource {

    private final int availableMemoryMB;

    private final int availableVirtualCores;

    public YarnResource(int availableMemoryMB, int availableVirtualCores) {
        this.availableMemoryMB = availableMemoryMB;
        this.availableVirtualCores = availableVirtualCores;
    }

    public int getAvailableMemoryMB() {
        return availableMemoryMB;
    }

    public int getAvailableVirtualCores() {
        return availableVirtualCores;
    }
}

package io.github.melin.spark.jobserver.driver.listener;

/**
 * Created by libinsong on 2018/8/18.
 * Server监听器
 */
public class ExecutorMetric {
    private String execId;

    private long jvmHeapMemory;

    private long jvmOffHeapMemory;

    private long onHeapExecutionMemory;

    private long offHeapExecutionMemory;

    private long onHeapStorageMemory;

    private long offHeapStorageMemory;

    private long onHeapUnifiedMemory;

    private long offHeapUnifiedMemory;

    private long directPoolMemory;

    private long mappedPoolMemory;

    private long processTreeJVMVMemory;

    private long processTreeJVMRSSMemory;

    private long processTreePythonVMemory;

    private long processTreePythonRSSMemory;

    private long processTreeOtherVMemory;

    private long processTreeOtherRSSMemory;

    public ExecutorMetric() {
    }

    public ExecutorMetric(String execId, long jvmHeapMemory, long jvmOffHeapMemory,
                          long onHeapExecutionMemory, long offHeapExecutionMemory,
                          long onHeapStorageMemory, long offHeapStorageMemory,
                          long onHeapUnifiedMemory, long offHeapUnifiedMemory,
                          long directPoolMemory, long mappedPoolMemory,
                          long processTreeJVMVMemory, long processTreeJVMRSSMemory,
                          long processTreePythonVMemory, long processTreePythonRSSMemory,
                          long processTreeOtherVMemory, long processTreeOtherRSSMemory) {
        this.execId = execId;
        this.jvmHeapMemory = jvmHeapMemory;
        this.jvmOffHeapMemory = jvmOffHeapMemory;
        this.onHeapExecutionMemory = onHeapExecutionMemory;
        this.offHeapExecutionMemory = offHeapExecutionMemory;
        this.onHeapStorageMemory = onHeapStorageMemory;
        this.offHeapStorageMemory = offHeapStorageMemory;
        this.onHeapUnifiedMemory = onHeapUnifiedMemory;
        this.offHeapUnifiedMemory = offHeapUnifiedMemory;
        this.directPoolMemory = directPoolMemory;
        this.mappedPoolMemory = mappedPoolMemory;
        this.processTreeJVMVMemory = processTreeJVMVMemory;
        this.processTreeJVMRSSMemory = processTreeJVMRSSMemory;
        this.processTreePythonVMemory = processTreePythonVMemory;
        this.processTreePythonRSSMemory = processTreePythonRSSMemory;
        this.processTreeOtherVMemory = processTreeOtherVMemory;
        this.processTreeOtherRSSMemory = processTreeOtherRSSMemory;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public long getJvmHeapMemory() {
        return jvmHeapMemory / 1024;
    }

    public void setJvmHeapMemory(long jvmHeapMemory) {
        if (this.jvmHeapMemory < jvmHeapMemory) {
            this.jvmHeapMemory = jvmHeapMemory;
        }
    }

    public long getJvmOffHeapMemory() {
        return jvmOffHeapMemory / 1024;
    }

    public void setJvmOffHeapMemory(long jvmOffHeapMemory) {
        if (this.jvmOffHeapMemory < jvmOffHeapMemory) {
            this.jvmOffHeapMemory = jvmOffHeapMemory;
        }
    }

    public long getOnHeapExecutionMemory() {
        return onHeapExecutionMemory / 1024;
    }

    public void setOnHeapExecutionMemory(long onHeapExecutionMemory) {
        if (this.onHeapExecutionMemory < onHeapExecutionMemory) {
            this.onHeapExecutionMemory = onHeapExecutionMemory;
        }
    }

    public long getOffHeapExecutionMemory() {
        return offHeapExecutionMemory / 1024;
    }

    public void setOffHeapExecutionMemory(long offHeapExecutionMemory) {
        if (this.offHeapExecutionMemory < offHeapExecutionMemory) {
            this.offHeapExecutionMemory = offHeapExecutionMemory;
        }
    }

    public long getOnHeapStorageMemory() {
        return onHeapStorageMemory / 1024;
    }

    public void setOnHeapStorageMemory(long onHeapStorageMemory) {
        if (this.onHeapStorageMemory < onHeapStorageMemory) {
            this.onHeapStorageMemory = onHeapStorageMemory;
        }
    }

    public long getOffHeapStorageMemory() {
        return offHeapStorageMemory / 1024;
    }

    public void setOffHeapStorageMemory(long offHeapStorageMemory) {
        if (this.offHeapStorageMemory < offHeapStorageMemory) {
            this.offHeapStorageMemory = offHeapStorageMemory;
        }
    }

    public long getOnHeapUnifiedMemory() {
        return onHeapUnifiedMemory / 1024;
    }

    public void setOnHeapUnifiedMemory(long onHeapUnifiedMemory) {
        if (this.onHeapUnifiedMemory < onHeapUnifiedMemory) {
            this.onHeapUnifiedMemory = onHeapUnifiedMemory;
        }
    }

    public long getOffHeapUnifiedMemory() {
        return offHeapUnifiedMemory / 1024;
    }

    public void setOffHeapUnifiedMemory(long offHeapUnifiedMemory) {
        if (this.offHeapUnifiedMemory < offHeapUnifiedMemory) {
            this.offHeapUnifiedMemory = offHeapUnifiedMemory;
        }
    }

    public long getDirectPoolMemory() {
        return directPoolMemory / 1024;
    }

    public void setDirectPoolMemory(long directPoolMemory) {
        if (this.directPoolMemory < directPoolMemory) {
            this.directPoolMemory = directPoolMemory;
        }
    }

    public long getMappedPoolMemory() {
        return mappedPoolMemory / 1024;
    }

    public void setMappedPoolMemory(long mappedPoolMemory) {
        if (this.mappedPoolMemory < mappedPoolMemory) {
            this.mappedPoolMemory = mappedPoolMemory;
        }
    }

    public long getProcessTreeJVMVMemory() {
        return processTreeJVMVMemory / 1024;
    }

    public void setProcessTreeJVMVMemory(long processTreeJVMVMemory) {
        if (this.processTreeJVMVMemory < processTreeJVMVMemory) {
            this.processTreeJVMVMemory = processTreeJVMVMemory;
        }
    }

    public long getProcessTreeJVMRSSMemory() {
        return processTreeJVMRSSMemory / 1024;
    }

    public void setProcessTreeJVMRSSMemory(long processTreeJVMRSSMemory) {
        if (this.processTreeJVMRSSMemory < processTreeJVMRSSMemory) {
            this.processTreeJVMRSSMemory = processTreeJVMRSSMemory;
        }
    }

    public long getProcessTreePythonVMemory() {
        return processTreePythonVMemory / 1024;
    }

    public void setProcessTreePythonVMemory(long processTreePythonVMemory) {
        if (this.processTreePythonVMemory < processTreePythonVMemory) {
            this.processTreePythonVMemory = processTreePythonVMemory;
        }
    }

    public long getProcessTreePythonRSSMemory() {
        return processTreePythonRSSMemory / 1024;
    }

    public void setProcessTreePythonRSSMemory(long processTreePythonRSSMemory) {
        if (this.processTreePythonRSSMemory < processTreePythonRSSMemory) {
            this.processTreePythonRSSMemory = processTreePythonRSSMemory;
        }
    }

    public long getProcessTreeOtherVMemory() {
        return processTreeOtherVMemory / 1024;
    }

    public void setProcessTreeOtherVMemory(long processTreeOtherVMemory) {
        if (this.processTreeOtherVMemory < processTreeOtherVMemory) {
            this.processTreeOtherVMemory = processTreeOtherVMemory;
        }
    }

    public long getProcessTreeOtherRSSMemory() {
        return processTreeOtherRSSMemory / 1024;
    }

    public void setProcessTreeOtherRSSMemory(long processTreeOtherRSSMemory) {
        if (this.processTreeOtherRSSMemory < processTreeOtherRSSMemory) {
            this.processTreeOtherRSSMemory = processTreeOtherRSSMemory;
        }
    }

    @Override
    public String toString() {
        return "ExecutorMetric{" +
                "execId='" + execId + '\'' +
                ", jvmHeapMemory=" + jvmHeapMemory +
                ", jvmOffHeapMemory=" + jvmOffHeapMemory +
                ", onHeapExecutionMemory=" + onHeapExecutionMemory +
                ", offHeapExecutionMemory=" + offHeapExecutionMemory +
                ", onHeapStorageMemory=" + onHeapStorageMemory +
                ", offHeapStorageMemory=" + offHeapStorageMemory +
                ", onHeapUnifiedMemory=" + onHeapUnifiedMemory +
                ", offHeapUnifiedMemory=" + offHeapUnifiedMemory +
                ", directPoolMemory=" + directPoolMemory +
                ", mappedPoolMemory=" + mappedPoolMemory +
                ", processTreeJVMVMemory=" + processTreeJVMVMemory +
                ", processTreeJVMRSSMemory=" + processTreeJVMRSSMemory +
                ", processTreePythonVMemory=" + processTreePythonVMemory +
                ", processTreePythonRSSMemory=" + processTreePythonRSSMemory +
                ", processTreeOtherVMemory=" + processTreeOtherVMemory +
                ", processTreeOtherRSSMemory=" + processTreeOtherRSSMemory +
                '}';
    }
}

package io.github.melin.spark.jobserver.driver.listener;

/**
 * Created by libinsong on 2018/8/18.
 * Server监听器
 */
public class DriverMetric {
    private long jvmHeapMemory;

    private long jvmOffHeapMemory;

    private long directPoolMemory;

    private long mappedPoolMemory;

    private long processTreeJVMVMemory;

    private long processTreeJVMRSSMemory;

    private long processTreePythonVMemory;

    private long processTreePythonRSSMemory;

    private long processTreeOtherVMemory;

    private long processTreeOtherRSSMemory;

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
        return "DriverMetric{" +
                "jvmHeapMemory=" + jvmHeapMemory +
                ", jvmOffHeapMemory=" + jvmOffHeapMemory +
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

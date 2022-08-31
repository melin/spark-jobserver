package io.github.melin.spark.jobserver.driver;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;

public class InstanceContext {

    /**
     * 用户ID
     */
    private static String accessKey;

    /**
     * job type
     */
    private static JobType jobType;

    /**
     * 实例code
     */
    private static String instanceCode = "";

    /**
     * 实例类型
     */
    private static InstanceType instanceType;

    /**
     * 作业入口类
     */
    private static String jobClassName = "";

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        InstanceContext.accessKey = accessKey;
    }

    public static JobType getJobType() {
        return jobType;
    }

    public static void setJobType(JobType jobType) {
        InstanceContext.jobType = jobType;
    }

    public static String getInstanceCode() {
        return instanceCode;
    }

    public static void setInstanceCode(String instanceCode) {
        InstanceContext.instanceCode = instanceCode;
    }

    public static InstanceType getInstanceType() {
        return instanceType;
    }

    public static void setInstanceType(InstanceType instanceType) {
        InstanceContext.instanceType = instanceType;
    }

    public static String getJobClassName() {
        return jobClassName;
    }

    public static void setJobClassName(String jobClassName) {
        InstanceContext.jobClassName = jobClassName;
    }
}

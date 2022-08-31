package io.github.melin.spark.jobserver.deployment.dto;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import lombok.*;
import lombok.experimental.Tolerate;

import java.time.Instant;
import java.util.List;

/**
 * huaixin 2022/4/3 2:46 PM
 */
@Data
@Builder(builderClassName = "Builder", setterPrefix = "set")
public class JobInstanceInfo {

    private String accessKey;

    private String clusterCode;

    private JobType jobType;

    private InstanceType instanceType;

    private String instanceCode;

    private String jobName;

    private String jobText;

    private String jobConfig;

    private String yarnQueue;

    private Instant scheduleTime;

    private String owner;

    /**
     * 用户作业设置有效参数
     */
    private List<JobParam> customParams;

    @Tolerate
    public JobInstanceInfo() {
    }
}

package io.github.melin.spark.jobserver.logs;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * huaixin 2022/4/14 10:09
 */
@Data
@Builder(builderClassName = "Builder", setterPrefix = "set")
public class LogTaskDto {

    private String clusterCode;

    // yarn applicationId
    private String applicationId;

    private String instanceCode;

    private JobType jobType;

    private InstanceType instanceType;

    private String owner;

    private boolean shareDriver; //driver 是否共享

    private String sparkDriverUrl;

    private Instant scheduleTime;
}

package io.github.melin.spark.jobserver.core.dto;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import lombok.Data;

/**
 * huaixin 2022/4/7 8:01 PM
 */
@Data
public class InstanceDto {

    private Long driverId;

    private String instanceCode;

    private String jobName;

    private String yarnQueue;

    private InstanceType instanceType;

    private String accessKey;

    private String sparkDriverUrl;

    private JobType jobType;

    private String jobText;

    private String jobConfig;

    private boolean shareDriver;
}

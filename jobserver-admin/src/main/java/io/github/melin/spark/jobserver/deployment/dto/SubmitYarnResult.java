package io.github.melin.spark.jobserver.deployment.dto;

import lombok.Data;

/**
 * Created by libinsong on 2020/7/22 1:04 下午
 */
@Data
public class SubmitYarnResult {

    private String applicationId;

    private String sparkDriverUrl;

    private String yarnQueue;

    public SubmitYarnResult(String applicationId, String sparkDriverUrl, String yarnQueue) {
        this.applicationId = applicationId;
        this.sparkDriverUrl = sparkDriverUrl;
        this.yarnQueue = yarnQueue;
    }
}

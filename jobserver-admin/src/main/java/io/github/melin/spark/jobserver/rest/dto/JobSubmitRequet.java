package io.github.melin.spark.jobserver.rest.dto;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobSubmitRequet {

    // 用户可以指定实例code，如果不设置系统生成32位随机数
    private String instanceCode;

    @NotEmpty
    private String jobText;

    // spark 参数，按照properties格式设置，校验参数key前缀: spark.
    private String jobConfig;

    @NotEmpty
    private String jobName;

    @NotNull
    private JobType jobType;

    @NotNull
    private InstanceType instanceType;

    @NotEmpty
    private String owner;

    private String clusterCode = "default";

    private Integer maxRetryCount = 1;

    // 秒
    private Long scheduleTime;

    private String[] dependentCodes;

    /**
     * sql 任务，select/show/explain等语句查询结果，通过resultCallbackUri 返回给客户端。
     * 如果任务中有多个select/show/explain 语句，每个语句触发一次调用。
     * 要求支持POST
     */
    private String resultCallbackUri;

    private String callbackUri;

    // 请求客户端名，确定后不要修改
    private String clientName;
}

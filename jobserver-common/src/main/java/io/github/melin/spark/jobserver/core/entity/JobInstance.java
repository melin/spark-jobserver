package io.github.melin.spark.jobserver.core.entity;

import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.core.enums.JobType;
import com.gitee.melin.bee.model.IEntity;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@ToString
@Builder(builderClassName = "Builder", setterPrefix = "set")
@Entity
@Table(name = "sjs_job_instance")
public class JobInstance implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "workspace", length = 45)
    private String workspace;

    @Column(name = "code", nullable = false, length = 45)
    private String code;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "cluster_code", length = 64)
    private String clusterCode;

    @Column(name = "yarn_queue", length = 128)
    private String yarnQueue;

    @Column(name = "dependent_code", length = 1024)
    private String dependentCode;

    @Column(name = "job_type", length = 32)
    @Type(type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClass",
                    value = "io.github.melin.spark.jobserver.core.enums.JobType")})
    private JobType jobType;

    @Column(name = "instance_type", nullable = false, length = 32)
    @Type(type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClass",
                    value = "io.github.melin.spark.jobserver.core.enums.InstanceType")})
    private InstanceType instanceType;

    @lombok.Builder.Default
    @Column(name = "version", nullable = false)
    private Integer version = 0;

    @Column(name = "status", nullable = false)
    @Type(type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClass",
                    value = "io.github.melin.spark.jobserver.core.enums.InstanceStatus")})
    private InstanceStatus status;

    @Column(name = "schedule_time")
    private Instant scheduleTime;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "owner", nullable = false, length = 64)
    private String owner;

    @Column(name = "runtimes")
    private Long runTimes;

    @lombok.Builder.Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @lombok.Builder.Default
    @Column(name = "max_retry_count")
    private Integer maxRetryCount = 0;

    @lombok.Builder.Default
    @Column(name = "failure_count")
    private Integer failureCount = 0;

    @Column(name = "application_id", length = 128)
    private String applicationId;

    @Column(name = "client_name", length = 64)
    private String clientName;

    @Column(name = "result_callback_uri", length = 64)
    private String resultCallbackUri;

    @lombok.Builder.Default
    @Column(name = "gmt_created", nullable = false)
    private Instant gmtCreated = Instant.now();

    @Column(name = "gmt_modified")
    private Instant gmtModified;

    @Column(name = "creater", nullable = false, length = 45)
    private String creater;

    @Column(name = "modifier", length = 45)
    private String modifier;

    @Tolerate
    public JobInstance() {
    }
}

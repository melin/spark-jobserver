package io.github.melin.spark.jobserver.rest.dto;

import io.github.melin.spark.jobserver.core.enums.InstanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InstanceInfo {

    private String instanceCode;

    private InstanceStatus status;

    private String applicationId;

    private String errorMsg;
}

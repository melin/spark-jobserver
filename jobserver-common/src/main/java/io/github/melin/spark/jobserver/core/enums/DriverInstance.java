package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DriverInstance {
    SHARE_INSTANCE(0),

    NEW_INSTANCE(1),

    ERROR_INSTANCE(2);

    private Integer value;

    DriverInstance(Integer value) {
        this.value = value;
    }

    @JsonValue
    public Integer getValue() {
        return value;
    }
}

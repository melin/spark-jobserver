package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

/**
 * huaixin 2022/4/6 10:44 PM
 */
@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum DriverStatus implements BaseStringEnum {

    INIT("初始化", "init"),

    IDLE("空闲", "idle"),

    RUNNING("运行", "running"),

    FINISHED("完成", "finished"),

    LOCKED("锁定", "locked");

    private final String name;

    private final String value;

    DriverStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}

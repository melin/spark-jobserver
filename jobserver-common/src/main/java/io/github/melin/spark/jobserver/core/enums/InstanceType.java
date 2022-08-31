package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

/**
 * Created by admin on 2017/5/21.
 */
@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum InstanceType implements BaseStringEnum {
    DEV,
    API,
    SCHEDULE;

    @JsonValue
    @Override
    public String getValue() {
        return this.name();
    }
}

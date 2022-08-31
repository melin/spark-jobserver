package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum StorageType implements BaseStringEnum {
    HDFS("hdfs"),
    OSS("oss"),
    OBS("obs"),
    COS("cos");

    private String name;

    private StorageType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonValue
    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum CloudType implements BaseStringEnum {
    PRIVATE("private", "private"),
    AWS("aws", "s3"),
    AZURE("azure", "abfs"),
    ALIYUN("aliyun", "oss"),
    TENCENT("tencent", "cos"),
    HUAWEI("huawei", "obs");

    private final String name;

    private final String schema;

    private CloudType(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    @JsonValue
    @Override
    public String getValue() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return name;
    }
}

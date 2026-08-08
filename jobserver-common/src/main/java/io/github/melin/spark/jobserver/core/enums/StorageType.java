package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum StorageType implements BaseStringEnum {
    HDFS("HDFS", "hdfs://"),
    S3("S3", "s3a://"),
    OSS("阿里云 OSS", "oss://"),
    COS("腾讯云 COS", "cosn://"),
    OBS("华为云 OBS", "obs://"),
    ABFS("Azure ABFS", "abfss://");

    private final String name;

    private final String schema;

    StorageType(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    @Override
    public String getValue() {
        return this.name();
    }

    public String getSchema() {
        return schema;
    }
}

package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum JobType implements BaseStringEnum {
    SPARK_SQL,
    SPARK_STREAM_SQL,
    SPARK_APP,
    SPARK_PYTHON;

    @JsonValue
    @Override
    public String getValue() {
        return this.name();
    }

    public static boolean isBatchJob(JobType jobType) {
        return SPARK_SQL == jobType || SPARK_APP == jobType || SPARK_PYTHON == jobType;
    }
}

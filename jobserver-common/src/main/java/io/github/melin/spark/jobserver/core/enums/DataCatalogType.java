package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum DataCatalogType implements BaseStringEnum {
    HIVE("Hive Catalog"),
    GLUE("Aws Glue Catalog"),
    DLF("Aliyun DLF Catalog"),
    ICEBERG_JDBC("Iceberg Jdbc Catalog"),
    ICEBERG_REST("Iceberg Rest Catalog"),
    ICEBERG_GLUE("Iceberg Glue Catalog"),
    PAIMON_JDBC("Paimon Jdbc Catalog"),
    PAIMON_GLUE("Paimon Glue Catalog");

    private final String name;

    private DataCatalogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return this.name();
    }

    @Override
    public String toString() {
        return this.name();
    }
}

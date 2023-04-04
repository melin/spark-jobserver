package io.github.melin.spark.jobserver.driver.util;

import com.gitee.melin.bee.core.jdbc.enums.DataSourceType;
import com.google.common.collect.Maps;
import io.github.melin.spark.jobserver.core.entity.DataConnector;
import io.github.melin.spark.jobserver.core.util.AESUtils;
import io.github.melin.spark.jobserver.driver.catalog.JDBCTableCatalog;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.connector.catalog.CatalogPlugin;
import org.apache.spark.sql.util.CaseInsensitiveStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CatalogUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogUtils.class);

    public static final String SESSION_CATALOG_NAME = "spark_catalog";

    public static CatalogPlugin buildJdbcCatalogPlugin(DataConnector connector,
                                                       String catalogName) {
        DataSourceType dataSourceType = connector.getDataSourceType();

        String user = connector.getUsername();
        String password = AESUtils.decrypt(connector.getPassword());
        String url = connector.getJdbcUrl();
        String driverClass = "";

        if (StringUtils.isBlank(user)) {
            throw new IllegalArgumentException(catalogName + " 数据源没有配置 username");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException(catalogName + " 数据源没有配置 password");
        }

        if (DataSourceType.MYSQL == dataSourceType || DataSourceType.TIDB == dataSourceType
                || DataSourceType.DORIS == dataSourceType || DataSourceType.STARROCKS == dataSourceType) {
            driverClass = "com.mysql.cj.jdbc.Driver";
        } else if (DataSourceType.ORACLE == dataSourceType) {
            driverClass = "oracle.jdbc.OracleDriver";
        } else if (DataSourceType.DB2 == dataSourceType) {
            driverClass = "com.ibm.db2.jcc.DB2Driver";
        } else if (DataSourceType.POSTGRESQL == dataSourceType || DataSourceType.GAUSS == dataSourceType) {
            driverClass = "org.postgresql.Driver";
        } else if (DataSourceType.SQLSERVER == dataSourceType) {
            driverClass = "org.postgresql.Driver";
        } else if (DataSourceType.HANA == dataSourceType) {
            driverClass = "com.sap.db.jdbc.Driver";
        } else if (DataSourceType.GREENPLUM == dataSourceType) {
            driverClass = "com.pivotal.jdbc.GreenplumDriver";
        }

        Map<String, String> options = Maps.newHashMap();
        options.put("user", user);
        options.put("password", password);
        options.put("url", url);
        options.put("driver", driverClass);

        CatalogPlugin catalog = new JDBCTableCatalog();
        catalog.initialize(catalogName, new CaseInsensitiveStringMap(options));

        LOG.info("init {} catalog: {}", dataSourceType.getValue(), catalogName);
        LogUtils.info("init {} catalog: {}", dataSourceType.getValue(), catalogName);

        return catalog;
    }
}

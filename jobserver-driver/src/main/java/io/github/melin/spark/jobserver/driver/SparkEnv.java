package io.github.melin.spark.jobserver.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hive.common.util.HiveVersionInfo;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivilegedExceptionAction;
import java.util.concurrent.CountDownLatch;

/**
 * huaixin 2022/4/6 7:05 PM
 */
public class SparkEnv {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkEnv.class);

    private static SparkSession sparkSession;

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void init(SparkConf sparkConf, boolean kerberosEnabled, boolean hiveEnabled) throws Exception {
        if (sparkSession == null) {
            if (kerberosEnabled) {
                UserGroupInformation.getLoginUser().doAs((PrivilegedExceptionAction<Object>) () -> {
                    initSpark(sparkConf, hiveEnabled);
                    return null;
                });
            } else {
                initSpark(sparkConf, hiveEnabled);
            }
        }
    }

    private static void initSpark(SparkConf sparkConf, boolean hiveEnabled) {
        SparkSession.Builder builder = SparkSession.builder().config(sparkConf).enableHiveSupport()
                .config("spark.sql.catalog.default_iceberg", "org.apache.iceberg.spark.SparkSessionCatalog")
                .appName("SparkSessionJob");

        if (hiveEnabled) {
            LOGGER.info("sparksession hive enabled");
            builder.enableHiveSupport();
        }

        sparkSession = builder.getOrCreate();
        sparkSession.conf().set("spark.sql.hive.version", HiveVersionInfo.getVersion());
        LOGGER.info("SparkSession inited");
    }

    public static String getApplicationId() {
        return sparkSession.sparkContext().applicationId();
    }

    public static Configuration hadoopConfiguration() {
        return SparkEnv.getSparkSession().sparkContext().hadoopConfiguration();
    }

    public static SparkSession getSparkSession() {
        return sparkSession;
    }

    public static SparkContext getSparkContext() {
        return sparkSession.sparkContext();
    }

    public static Dataset<Row> sql(String sql) {
        return sparkSession.sql(sql);
    }

    public static void setSparkSession(SparkSession sparkSession) {
        SparkEnv.sparkSession = sparkSession;
    }

    public static SparkConf sparkConf() {
        return sparkSession.sparkContext().conf();
    }

    public static void stopDriver() {
        if (sparkSession != null) {
            sparkSession.stop();
        }
        countDownLatch.countDown();
    }

    public static void waitDriver() throws InterruptedException {
        SparkEnv.countDownLatch.await();
    }
}

package io.github.melin.spark.jobserver.driver.support;

import io.github.melin.spark.jobserver.core.enums.InstanceType;
import io.github.melin.spark.jobserver.driver.InstanceContext;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author melin 2021/8/19 10:35 下午
 */
@Component
public class Py4jEndPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(Py4jEndPoint.class);

    public SparkSession getSparkSession() {
        return SparkDriverEnv.getSparkSession();
    }

    public JavaSparkContext getSparkContext() {
        return new JavaSparkContext(SparkDriverEnv.getSparkSession().sparkContext());
    }

    public SparkConf getSparkConf() {
        return SparkDriverEnv.getSparkSession().sparkContext().getConf();
    }

    public String getSparkConfig(String key, String defaultVale) {
        return SparkDriverEnv.getSparkSession().conf().get(key, defaultVale);
    }

    public void sendMsg(String msgType, Object message) {
        if (message == null) {
            return;
        }

        String msg = "";
        if (message instanceof Iterable || message.getClass().isArray()) {
            msg = StringUtils.join(message, ",");
        } else if (message instanceof Map<?, ?>) {
            msg = Joiner.on(",").withKeyValueSeparator("=").join((Map<?, ?>) message);
        } else {
            msg = message.toString();
        }

        if ("info".equalsIgnoreCase(msgType)) {
            LogUtils.info(msg);
        } else if ("warn".equalsIgnoreCase(msgType)) {
            LogUtils.warn(msg);
        } else if ("error".equalsIgnoreCase(msgType)) {
            LogUtils.error(msg);
        } else if ("stdout".equalsIgnoreCase(msgType)) {
            LogUtils.stdout(msg);
        }
    }

    public Boolean execDataFrameShowEnabled() {
        boolean enabled = SparkDriverEnv.getSparkSession().sparkContext()
                .getConf().getBoolean("spark.prod.dataframe.show.enabled", false);

        if (InstanceType.DEV == InstanceContext.getInstanceType() || enabled) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkTabExist(String databaseName, String tableName) {
        try {
            SparkDriverEnv.getSparkSession().catalog().getTable(databaseName, tableName);
            return true;
        } catch (AnalysisException e) {
            return false;
        }
    }
}

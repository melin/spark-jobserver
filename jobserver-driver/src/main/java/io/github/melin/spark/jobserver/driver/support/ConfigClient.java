package io.github.melin.spark.jobserver.driver.support;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.io.StringReader;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * jobserver.driver 前缀配置
 */
public class ConfigClient extends PropertiesConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigClient.class);

    private static ConfigClient configClient;

    private static final AtomicBoolean init = new AtomicBoolean(false);

    public static void init(String configText, SparkConf sparkConf) throws BeansException {
        LOGGER.info("初始化参数: \n" + configText);

        String profile = System.getProperty("spring.profiles.active");
        sparkConf.set("spark.spring.profile.active", profile);

        if (init.compareAndSet(false, true)) {
            try {
                configClient = new ConfigClient();
                loadConfig(configClient, configText);

                Iterator<String> iter = configClient.getKeys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    //如果集群中配置spark参数，就不使用dc config 中配置参数。
                    if (StringUtils.startsWithIgnoreCase(key, "spark.")) {
                        String value = configClient.getString(key);
                        if (!sparkConf.contains(key)) {
                            LOGGER.info("dc spark config {} = {}", key, value);
                            System.setProperty(key, value);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("不能重复初始化");
        }
    }

    private static void loadConfig(ConfigClient client, String configText) {
        PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();
        try {
            client.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
            layout.load(client, new StringReader(configText));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static ConfigClient getInstance() {
        return configClient;
    }
}

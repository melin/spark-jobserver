package io.github.melin.spark.jobserver.support;

import com.gitee.melin.bee.core.conf.ConfigEntry;
import io.github.melin.spark.jobserver.SparkJobServerConf;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ClusterConfig implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ClusterConfig.class);

    @Autowired
    private ClusterService clusterService;

    private ConcurrentHashMap<String, PropertiesConfiguration> configurationMap = new ConcurrentHashMap<>();

    private Map<String, String> configMap = new HashMap<>();

    private ScheduledExecutorService executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("load cluster config");

        loadConfig();

        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(() -> {
            loadConfig();
        }, 5, 5, TimeUnit.SECONDS);
    }

    private void loadConfig() {
        List<Cluster> clusters = clusterService.findByNamedParam("status", true);
        for (Cluster cluster : clusters) {
            String clusterCode = cluster.getCode();
            try {
                String config = cluster.getJobserverConfig();
                if (StringUtils.isEmpty(config)) {
                    continue;
                }

                String lastConfig = configMap.getOrDefault(clusterCode, null);
                if (StringUtils.isEmpty(lastConfig) || !StringUtils.equals(lastConfig, config)) {
                    Properties properties = new Properties();
                    properties.load(new StringReader(config));
                    Enumeration<?> enumeration = properties.propertyNames();
                    PropertiesConfiguration clusterConfig = new PropertiesConfiguration();
                    while (enumeration.hasMoreElements()) {
                        String key = (String) enumeration.nextElement();
                        clusterConfig.setProperty(key, properties.getProperty(key));
                    }
                    configurationMap.put(clusterCode, clusterConfig);
                    configMap.put(clusterCode, config);
                    logger.info("================load {} new properties===============:\n{}", clusterCode, config);
                }
            } catch (Exception e) {
                logger.error("update cluster {} config error:{}", clusterCode, e.getMessage());
            }
        }
    }

    public String getValue(String clusterCode, ConfigEntry<String> confKey) {
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            return conf.getString(confKey.getKey(), confKey.getDefaultValue());
        } else {
            throw new IllegalArgumentException("cluster " + clusterCode + " not exists");
        }
    }

    public int getInt(String clusterCode, ConfigEntry<Integer> confKey) {
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            return conf.getInt(confKey.getKey(), confKey.getDefaultValue());
        } else {
            throw new IllegalArgumentException("cluster " + clusterCode + " not exists");
        }
    }

    public long getLong(String clusterCode, ConfigEntry<Long> confKey) {
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            return conf.getLong(confKey.getKey(), confKey.getDefaultValue());
        } else {
            throw new IllegalArgumentException("cluster " + clusterCode + " not exists");
        }
    }

    public boolean getBoolean(String clusterCode, ConfigEntry<Boolean> confKey) {
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            return conf.getBoolean(confKey.getKey(), confKey.getDefaultValue());
        } else {
            throw new IllegalArgumentException("cluster " + clusterCode + " not exists");
        }
    }

    public String getArrayValue(String clusterCode, String property) {
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            return String.join(",", conf.getStringArray(property));
        } else {
            throw new IllegalArgumentException("cluster " + clusterCode + " not exists");
        }
    }

    /**
     * 集群管理配置中非spark 配置项
     *
     * @param clusterCode
     * @return
     */
    private String getNonSparkConfig(String clusterCode) {
        StringBuilder sb = new StringBuilder();
        if (configMap.containsKey(clusterCode)) {
            PropertiesConfiguration conf = configurationMap.get(clusterCode);
            Iterator<String> iter = conf.getKeys();
            while (iter.hasNext()) {
                String key = iter.next();
                if (!key.startsWith("spark.")) {
                    String value = conf.getString(key);
                    if (StringUtils.isNotBlank(value)) {
                        sb.append(key).append(" = ").append(value).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    public String getDriverHadoopUserName(String clusterCode) {
        String user = System.getProperty(SparkJobServerConf.JOBSERVER_DRIVER_HADOOP_USER_NAME.getKey());
        if (StringUtils.isBlank(user)) {
            user = this.getValue(clusterCode, SparkJobServerConf.JOBSERVER_DRIVER_HADOOP_USER_NAME);
        }

        return user;
    }
}

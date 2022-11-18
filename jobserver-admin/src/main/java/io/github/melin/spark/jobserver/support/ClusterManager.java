package io.github.melin.spark.jobserver.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gitee.melin.bee.util.MapperUtils;
import com.google.common.collect.Maps;
import io.github.melin.spark.jobserver.deployment.dto.YarnResource;
import io.github.melin.spark.jobserver.api.SparkJobServerException;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.exception.ResouceLimitException;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.SaslRpcServer;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import java.util.concurrent.*;

import static io.github.melin.spark.jobserver.SparkJobServerConf.JOBSERVER_YARN_MIN_CPU_CORES;
import static io.github.melin.spark.jobserver.SparkJobServerConf.JOBSERVER_YARN_MIN_MEMORY_MB;
import static io.github.melin.spark.jobserver.support.KerberosLogin.DEFAULT_KEYTAB_FILE_NAME;

@Service
public class ClusterManager implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterManager.class);

    public static final String LOCAL_HADOOP_CONFIG_DIR = FileUtils.getUserDirectory() + "/tmp/jobserver-config";

    @Autowired
    private ClusterService clusterService;

    @Autowired
    protected ClusterConfig clusterConfig;

    @Autowired
    private KerberosLogin kerberosLogin;

    @Autowired
    private RestTemplate restTemplate;

    private ScheduledExecutorService executorService;

    /**
     * 不同yarn集群的配置文件
     */
    private final ConcurrentMap<String, String> yarnConfigDirLists = Maps.newConcurrentMap();

    /**
     * 集群最新更新时间
     */
    private final Map<String, Long> clusterUpdateTimeMap = Maps.newHashMap();

    private final Map<String, Configuration> hadoopConfList = Maps.newHashMap();

    private final Map<String, String> keytabFileMap = Maps.newHashMap();

    private final Map<String, String> kerberosConfMap = Maps.newHashMap();

    private final ConcurrentMap<String, String> yarnClientAddrMap = Maps.newConcurrentMap();

    private final ConcurrentMap<String, YarnResource> yarnResourceMap = Maps.newConcurrentMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("清理本地配置数据");
        FileUtils.deleteQuietly(new File(LOCAL_HADOOP_CONFIG_DIR));

        List<Cluster> clusters = clusterService.findByNamedParam("status", true);

        for (Cluster cluster : clusters) {
            LOGGER.info("========================= load {} ==============================", cluster.getCode());
            downloadClusterConfig(cluster);
        }
        executorService = new ScheduledThreadPoolExecutor(5);
        executorService.scheduleWithFixedDelay(() -> {
            try {
                for (String clusterCode : yarnClientAddrMap.keySet()) {
                    YarnResource yarnResource = getResourceByCluster(clusterCode);
                    if (yarnResource != null) {
                        yarnResourceMap.put(clusterCode, yarnResource);
                    }
                }

                List<Cluster> clusterList = clusterService.findByNamedParam("status", true);
                for (Cluster cluster : clusterList) {
                    long updateTime = clusterService.getClusterUpdateTime(cluster.getCode());
                    Long cacheUpdateTime = clusterUpdateTimeMap.get(cluster.getCode());
                    if (cacheUpdateTime == null || cacheUpdateTime != updateTime) {
                        LOGGER.info("========================= reload {} ==============================", cluster.getCode());
                        downloadClusterConfig(cluster);
                    }
                }

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public Set<String> getCluerCodes() {
        return hadoopConfList.keySet();
    }

    public Configuration getHadoopConf(String cluserCode){
        if (hadoopConfList.containsKey(cluserCode)) {
            return hadoopConfList.get(cluserCode);
        }

        throw new RuntimeException("集群不存在，或者下线：" + cluserCode);
    }

    public <T> T runSecured(String cluserCode, final Callable<T> securedCallable) {
        String authentication = "simple";
        try {
            Configuration conf = getHadoopConf(cluserCode);

            UserGroupInformation userGroupInformation;

            LOGGER.debug(cluserCode + " login: " + authentication);
            if (clusterService.isKerberosEnabled(cluserCode)) {
                authentication = "kerberos";
                try {
                    UserGroupInformation.setConfiguration(conf);
                    userGroupInformation = loginToKerberos(cluserCode);
                } catch (IOException e1) {
                    throw new RuntimeException("setAcl kerberos login error, dataCenter code:"
                            + cluserCode + " errorMsg: " + e1.getMessage(), e1);
                }
            } else {
                String user = clusterConfig.getDriverHadoopUserName(cluserCode);
                userGroupInformation = UserGroupInformation.createRemoteUser(user, SaslRpcServer.AuthMethod.SIMPLE);
                UserGroupInformation.setLoginUser(userGroupInformation);
            }

            return userGroupInformation.doAs((PrivilegedExceptionAction<T>) securedCallable::call);
        } catch (Exception e) {
            String msg = "authentication: " + authentication + ", 集群: " + cluserCode + " 登录失败: " + e.getMessage();
            throw new SparkJobServerException(msg, e);
        }
    }

    public String getYarnConfigDir(String cluserCode) {
        if (yarnConfigDirLists.containsKey(cluserCode)) {
            return yarnConfigDirLists.get(cluserCode);
        }
        return null;
    }

    private void initYarnRMAddr(String clusterCode, Configuration conf) {
        if (conf == null) {
        	return;
        }

        Iterator<Map.Entry<String, String>> iter = conf.iterator();
        boolean breakFind = false;

        while (iter.hasNext() && !breakFind) {
            Map.Entry<String, String> map = iter.next();
            String name = map.getKey();
            if (name.startsWith("yarn.resourcemanager.webapp.address")) {
                String value = map.getValue();
                String[] items = value.split(":");
                if (items.length > 1) {
                    String addr = items[0];
                    int port = Integer.parseInt(items[1]);
                    try {
                        new Socket(addr, port);
                        yarnClientAddrMap.put(clusterCode, value);
                        breakFind = true;
                    } catch (Exception e){
                        LOGGER.error("unactive resourcemanager webapp address:" + value + "  try next...");
                    }
                }
            }
        }
    }

    private Configuration initConfiguration(String clusterCode, String confDir) {
        LOGGER.info("init hadoop config: {}", clusterCode);
        Configuration conf = new Configuration(false);
        conf.clear();
        conf.addResource(new Path(confDir + "/core-site.xml"));
        conf.addResource(new Path(confDir + "/hdfs-site.xml"));
        conf.addResource(new Path(confDir + "/yarn-site.xml"));

        String rmAddr = null;
        boolean breakFind = false;
        Iterator<Map.Entry<String, String>> iter = conf.iterator();
        while (iter.hasNext() && !breakFind) {
            Map.Entry<String, String> map = iter.next();
            String name = map.getKey();
            if (name.startsWith("yarn.resourcemanager.address")) {
                String value = map.getValue();
                if (value.split(":").length > 1) {
                    String addr = value.split(":")[0];
                    int port = Integer.parseInt(value.split(":")[1]);
                    try {
                        LOGGER.info("检测地址：{}:{}", addr, port);
                        new Socket(addr, port);
                        rmAddr = value;
                        breakFind = true;
                    } catch (Exception e) {
                        LOGGER.warn("unactive resourcemanager address: {} 失败原因: {}", value, e.getMessage());
                    }
                }
            }
        }
        initYarnRMAddr(clusterCode, conf);

        if (StringUtils.isEmpty(rmAddr)) {
            LOGGER.error("cluster {} can not find yarn.resourcemanager.address", clusterCode);
            return null;
        }
        LOGGER.info("yarn.resourcemanager.address: {}", rmAddr);
        conf.set("yarn.resourcemanager.address", rmAddr);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("ipc.client.fallback-to-simple-auth-allowed", "true");
        conf.set("yarn.client.failover-max-attempts", "5");

        LOGGER.info("init hadoop config finished: {}", clusterCode);
        return conf;
    }

    private void downloadClusterConfig(Cluster cluster) throws IOException {
        String clusterCode = cluster.getCode();
        String destDir = LOCAL_HADOOP_CONFIG_DIR + "/" + clusterCode;

        FileUtils.forceMkdir(new File(LOCAL_HADOOP_CONFIG_DIR));
        FileUtils.forceMkdir(new File(destDir));

        if (StringUtils.isNotBlank(cluster.getCoreConfig()) &&
                StringUtils.isNotBlank(cluster.getHdfsConfig()) &&
                StringUtils.isNotBlank(cluster.getHiveConfig()) &&
                StringUtils.isNotBlank(cluster.getYarnConfig())) {
            FileUtils.write(new File(destDir + "/core-site.xml"),
                    cluster.getCoreConfig(), StandardCharsets.UTF_8);
            FileUtils.write(new File(destDir + "/hdfs-site.xml"),
                    cluster.getHdfsConfig(), StandardCharsets.UTF_8);
            FileUtils.write(new File(destDir + "/hive-site.xml"),
                    cluster.getHiveConfig(), StandardCharsets.UTF_8);
            FileUtils.write(new File(destDir + "/yarn-site.xml"),
                    cluster.getYarnConfig(), StandardCharsets.UTF_8);

            FileUtils.write(new File(destDir + "/spark.conf"),
                    cluster.getSparkConfig(), StandardCharsets.UTF_8);
        } else {
            LOGGER.error("集群 " + cluster.getCode() + " hadoop config 有空");
        }

        Configuration configuration = initConfiguration(clusterCode, destDir);
        if (configuration != null) {
            yarnConfigDirLists.put(clusterCode, destDir);
            hadoopConfList.put(clusterCode, configuration);

            if (cluster.isKerberosEnabled()) {
                configuration.set("hadoop.security.authentication", "kerberos");
                configuration.set("hadoop.security.authorization", "true");
            } else {
                configuration.set("hadoop.security.authentication", "simple");
                configuration.set("hadoop.security.authorization", "false");
            }

            long updateTime = clusterService.getClusterUpdateTime(clusterCode);
            if (updateTime > 0) {
                clusterUpdateTimeMap.put(clusterCode, updateTime);
            }
            LOGGER.info("load config {} of cluster {}", destDir, cluster.getName());

            String authentication = configuration.get("hadoop.security.authentication", "");
            if (cluster.isKerberosEnabled()) {
                if (!"kerberos".equals(authentication)) {
                    throw new RuntimeException(clusterCode + "存储集群开启kerberos, hadoop.security.authentication 不为 kerberos");
                }

                LOGGER.info("load kerberos config: {} -> {}", clusterCode, cluster.getCode());
                String confDir = LOCAL_HADOOP_CONFIG_DIR + "/" + cluster.getCode();
                String keytabFile = confDir + "/" + DEFAULT_KEYTAB_FILE_NAME;
                FileUtils.forceMkdir(new File(confDir));

                kerberosLogin.downloadKeytabFile(cluster, confDir);
                LOGGER.info("重新加载 kerberos 证书：{}", keytabFile);
                String kerberosConfFile = confDir + "/krb5.conf";
                if (!new File(kerberosConfFile).exists()) {
                    throw new IllegalArgumentException("kerberos conf 文件不存在：" + kerberosConfFile);
                }

                keytabFileMap.put(clusterCode, keytabFile);
                kerberosConfMap.put(clusterCode, kerberosConfFile);
            } else {
                if ("kerberos".equals(authentication)) {
                    throw new RuntimeException(clusterCode + "存储集群没有开启kerberos, hadoop.security.authentication=kerberos");
                }
            }
        }
    }

    public void updateClusterConfig(String clusterCode) throws IOException {
        Long updateTime = clusterService.getClusterUpdateTime(clusterCode);
        Long cacheUpdateTime = clusterUpdateTimeMap.get(clusterCode);
        if (cacheUpdateTime != null && cacheUpdateTime.longValue() != updateTime.longValue()) {
            LOGGER.info("{} 重新加载 hadoop 配置文件，老配置md5: {}, 新配置md5: {}", clusterCode, cacheUpdateTime, updateTime);
            Cluster cluster = clusterService.getClusterByCode(clusterCode);
            downloadClusterConfig(cluster);
        }
    }

    private UserGroupInformation loginToKerberos(String clusterCode) throws IOException {
        String kertabFile = keytabFileMap.get(clusterCode);
        String kerberConfFile = kerberosConfMap.get(clusterCode);
        String kerberosUser = clusterService.queryKerberosUser(clusterCode);

        Configuration conf = hadoopConfList.get(clusterCode);
        return kerberosLogin.loginToKerberos(kertabFile, kerberConfFile, kerberosUser, conf);
    }

    public String getKeyTabPath(String clusterCode) {
        return keytabFileMap.get(clusterCode);
    }

    private YarnResource getResource(String addr) {
        try {
            String url = "http://" + addr + "/ws/v1/cluster/metrics";
            String result = restTemplate.getForObject(url, String.class);
            Map<String, Object> data = MapperUtils.toJavaObject(result, new TypeReference<Map<String, Object>>() {});
            LinkedHashMap<String, Object> metrics = (LinkedHashMap<String, Object>) data.get("clusterMetrics");
            int availableMemoryMB = (Integer) metrics.get("availableMB");
            int availableVirtualCores = (Integer) metrics.get("availableVirtualCores");
            return new YarnResource(availableMemoryMB, availableVirtualCores);
        } catch (Exception e) {
            LOGGER.error("get cluster metrics {} error:{}", addr, e.getMessage());
            return null;
        }
    }

    private YarnResource getResourceByCluster(String clusterCode) {
        String addr = yarnClientAddrMap.get(clusterCode);
        YarnResource yarnResource = getResource(addr);
        if (yarnResource == null) {
            Configuration conf = hadoopConfList.get(clusterCode);
            initYarnRMAddr(clusterCode, conf);
            addr = yarnClientAddrMap.get(clusterCode);
            return getResource(addr);
        }
        return yarnResource;
    }

    public boolean isKerberosEnabled(String cluserCode) {
        return clusterService.isKerberosEnabled(cluserCode);
    }

    public void checkYarnResourceLimit(String clusterCode) {
        YarnResource yarnResource = yarnResourceMap.getOrDefault(clusterCode, null);
        if (yarnResource != null) {
            int limitMemory = clusterConfig.getInt(clusterCode, JOBSERVER_YARN_MIN_MEMORY_MB);
            int limitCores = clusterConfig.getInt(clusterCode, JOBSERVER_YARN_MIN_CPU_CORES);

            int availableMemoryMB = yarnResource.getAvailableMemoryMB();
            int availableVirtualCores = yarnResource.getAvailableVirtualCores();

            String memoryMsg = "当前yarn 集群可用内存: " + availableMemoryMB +
                    "MB, 最小需要内存: " + limitMemory + ", 可以调整参数(单位MB): " + JOBSERVER_YARN_MIN_MEMORY_MB.getKey();
            String cpuMsg = "当前yarn 集群可用CPU数量: " + availableVirtualCores +
                    ", 最小需要CPU: " + limitCores + ", 可以调整参数: " + JOBSERVER_YARN_MIN_CPU_CORES.getKey();
            LOGGER.info(memoryMsg);
            LOGGER.info(cpuMsg);

            if (availableMemoryMB < limitMemory) {
                throw new ResouceLimitException(memoryMsg);
            }
            if (availableVirtualCores < limitCores) {
                throw new ResouceLimitException(cpuMsg);
            }
        }
    }

    public String loadYarnConfig(String clusterCode) throws IOException {
        this.updateClusterConfig(clusterCode);
        String confDir = this.getYarnConfigDir(clusterCode);
        if (confDir == null) {
            throw new RuntimeException("Can not get Hadoop Configuration of " + clusterCode);
        }
        return confDir;
    }
}

package io.github.melin.spark.jobserver.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gitee.melin.bee.util.JsonUtils;
import com.google.common.collect.Maps;
import io.github.melin.spark.jobserver.core.enums.SchedulerType;
import io.github.melin.spark.jobserver.deployment.dto.YarnResource;
import io.github.melin.spark.jobserver.api.JobServerException;
import io.github.melin.spark.jobserver.core.entity.Cluster;
import io.github.melin.spark.jobserver.core.exception.ResouceLimitException;
import io.github.melin.spark.jobserver.core.service.ClusterService;
import io.github.melin.spark.jobserver.util.DateUtils;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Service
public class ClusterManager implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterManager.class);

    public static final String LOCAL_CLUSTER_CONFIG_DIR = FileUtils.getUserDirectory() + "/tmp/spark-jobserver-config";

    private static final ConcurrentMap<String, KerberosInfo> CLUSTER_KERBEROS_INFO_MAP = Maps.newConcurrentMap();

    @Autowired
    private ClusterService clusterService;

    @Autowired
    protected ClusterConfig clusterConfig;

    @Autowired
    private RestTemplate restTemplate;

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    /**
     * 不同yarn集群的配置文件
     */
    private final ConcurrentMap<String, String> yarnConfigDirLists = Maps.newConcurrentMap();

    /**
     * 集群最新更新时间
     */
    private final Map<String, Long> clusterUpdateTimeMap = Maps.newHashMap();

    private final Map<String, Configuration> hadoopConfList = Maps.newHashMap();

    private final ConcurrentMap<String, String> yarnRMAddrMap = Maps.newConcurrentMap();

    private final ConcurrentMap<String, String> yarnRMWebAppAddrMap = Maps.newConcurrentMap();

    private final ConcurrentMap<String, YarnResource> yarnResourceMap = Maps.newConcurrentMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("清理本地配置数据");
        FileUtils.deleteQuietly(new File(LOCAL_CLUSTER_CONFIG_DIR));

        List<Cluster> clusters = clusterService.findByNamedParam("status", true);

        for (Cluster cluster : clusters) {
            LOGGER.info("========================= load {} start==============================", cluster.getCode());
            downloadClusterConfig(cluster);
            LOGGER.info("========================= load {} end==============================", cluster.getCode());
        }

        executorService.scheduleWithFixedDelay(() -> {
            try {
                for (String clusterCode : yarnRMAddrMap.keySet()) {
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
            KerberosInfo kerberosInfo = CLUSTER_KERBEROS_INFO_MAP.get(cluserCode);

            UserGroupInformation userGroupInformation;
            if (kerberosInfo != null && kerberosInfo.isEnabled()) {
                authentication = "kerberos";
                userGroupInformation = loginToKerberos(kerberosInfo, cluserCode);
            } else {
                String user = clusterConfig.getDriverHadoopUserName(cluserCode);
                userGroupInformation = UserGroupInformation.createRemoteUser(user, SaslRpcServer.AuthMethod.SIMPLE);
                UserGroupInformation.setLoginUser(userGroupInformation);
            }

            return userGroupInformation.doAs((PrivilegedExceptionAction<T>) securedCallable::call);
        } catch (Exception e) {
            String msg = "authentication: " + authentication + ", 集群: " + cluserCode + " 登录失败: " + e.getMessage();
            throw new JobServerException(msg, e);
        }
    }

    public String getYarnConfigDir(String cluserCode) {
        if (yarnConfigDirLists.containsKey(cluserCode)) {
            return yarnConfigDirLists.get(cluserCode);
        }
        return null;
    }

    private String initYarnAddress(String clusterCode, Configuration conf) {
        if (conf == null) {
        	return null;
        }

        Iterator<Map.Entry<String, String>> iter = conf.iterator();
        String rmAddress = null;
        while (iter.hasNext()) {
            Map.Entry<String, String> map = iter.next();
            String name = map.getKey();
            if (name.startsWith("yarn.resourcemanager.address")) {
                String value = map.getValue();
                String[] items = value.split(":");
                if (items.length > 1) {
                    String addr = items[0];
                    int port = Integer.parseInt(items[1]);
                    try {
                        new Socket(addr, port);
                        yarnRMAddrMap.put(clusterCode, value);
                        rmAddress = value;
                    } catch (Exception e){
                        LOGGER.error("unactive resourcemanager address: " + value + ", try next...");
                    }
                }
            } else if (StringUtils.startsWith(name, "yarn.resourcemanager.webapp.address")) {
                String value = map.getValue();
                if (checkYarnResourceManagerAddress(value)) {
                    yarnRMWebAppAddrMap.put(clusterCode, value);
                }
            }
        }

        return rmAddress;
    }

    private Configuration initConfiguration(Cluster cluster, boolean yarnEnabled, String confDir) {
        final String clusterCode = cluster.getCode();
        LOGGER.info("init hadoop config: {}", clusterCode);
        Configuration conf = new Configuration(false);
        conf.clear();
        conf.addResource(new Path(confDir + "/core-site.xml"));
        conf.addResource(new Path(confDir + "/hdfs-site.xml"));

        if (yarnEnabled) {
            conf.addResource(new Path(confDir + "/yarn-site.xml"));

            String rmAddr = initYarnAddress(clusterCode, conf);
            if (StringUtils.isEmpty(rmAddr)) {
                LOGGER.error("cluster {} can not find yarn.resourcemanager.address", clusterCode);
                return null;
            }
            conf.set("yarn.resourcemanager.address", rmAddr);
        }

        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("ipc.client.fallback-to-simple-auth-allowed", "true");
        conf.set("yarn.client.failover-max-attempts", "5");

        if (cluster.isKerberosEnabled()) {
            conf.set("hadoop.security.authentication", "kerberos");
            conf.set("hadoop.security.authorization", "true");
        } else {
            conf.set("hadoop.security.authentication", "simple");
            conf.set("hadoop.security.authorization", "false");
        }

        LOGGER.info("init hadoop config finished: {}", clusterCode);
        return conf;
    }

    private boolean checkYarnResourceManagerAddress(String rmAddress) {
        try {
            String url = "http://" + rmAddress + "/ws/v1/cluster/info";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                HashMap<String, Object> root = (HashMap<String, Object>) JsonUtils.toJavaMap(response.getBody());
                HashMap<String, Object> clusterinfo = (LinkedHashMap<String, Object>) root.get("clusterInfo");

                long startedOn = (long) clusterinfo.get("startedOn");
                String time = DateUtils.formatTimestamp(startedOn);
                String state = (String) clusterinfo.get("state");
                String haState = (String) clusterinfo.get("haState");
                String hadoopVersion = (String) clusterinfo.get("hadoopVersion");

                LOGGER.info("check yarn resourcemanager status, hadoopVersion: {}, startedOn: {}, state: {}, haState: {}, rmAddress: {}",
                        hadoopVersion, time, state, haState, rmAddress);

                return "STARTED".equals(state) && "ACTIVE".equals(haState);
            } else {
                LOGGER.warn("check yarn resourcemanager status failed: {}, rmAddress: {}, msg: {}",
                        response.getStatusCodeValue(), rmAddress, response.getBody());
            }
        } catch (Exception e) {
            LOGGER.warn("check yarn resourcemanager failed: {}, rmAddress: {}", e.getMessage(), rmAddress);
        }

        return false;
    }

    private void downloadClusterConfig(Cluster cluster) throws IOException {
        String clusterCode = cluster.getCode();
        String destDir = LOCAL_CLUSTER_CONFIG_DIR + "/" + clusterCode;

        FileUtils.forceMkdir(new File(LOCAL_CLUSTER_CONFIG_DIR));
        FileUtils.forceMkdir(new File(destDir));

        if (StringUtils.isNotBlank(cluster.getCoreConfig()) &&
                StringUtils.isNotBlank(cluster.getHdfsConfig())) {
            FileUtils.write(new File(destDir + "/core-site.xml"), cluster.getCoreConfig(), StandardCharsets.UTF_8);
            FileUtils.write(new File(destDir + "/hdfs-site.xml"), cluster.getHdfsConfig(), StandardCharsets.UTF_8);
            FileUtils.write(new File(destDir + "/spark.conf"), cluster.getSparkConfig(), StandardCharsets.UTF_8);
        } else {
            LOGGER.warn("集群 " + cluster.getCode() + " hadoop config 有空");
        }

        if (StringUtils.isNotBlank(cluster.getHiveConfig())) {
            FileUtils.write(new File(destDir + "/hive-site.xml"), cluster.getHiveConfig(), StandardCharsets.UTF_8);
        }

        boolean yarnEnabled = false;
        if (cluster.getSchedulerType() == SchedulerType.YARN) {
            if (StringUtils.isNotBlank(cluster.getYarnConfig())) {
                yarnEnabled = true;
                FileUtils.write(new File(destDir + "/yarn-site.xml"), cluster.getYarnConfig(), StandardCharsets.UTF_8);
            }
        } else {
            FileUtils.write(new File(destDir + "/kubenetes.yml"), cluster.getYarnConfig(), StandardCharsets.UTF_8);

            FileUtils.write(new File(destDir + "/kubenetes.yml"), cluster.getYarnConfig(), StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(cluster.getDriverPodTemplate())) {
                FileUtils.write(new File(destDir + "/driver-pod-manager.yml"), cluster.getDriverPodTemplate(), StandardCharsets.UTF_8);
            }
            if (StringUtils.isNotBlank(cluster.getExecutorPodTemplate())) {
                FileUtils.write(new File(destDir + "/executor-pod-manager.yml"), cluster.getExecutorPodTemplate(), StandardCharsets.UTF_8);
            }
        }

        Configuration configuration = initConfiguration(cluster, yarnEnabled, destDir);
        if (configuration != null) {
            long updateTime = clusterService.getClusterUpdateTime(clusterCode);
            if (updateTime > 0) {
                clusterUpdateTimeMap.put(clusterCode, updateTime);
            }
            LOGGER.info("load config {} of cluster {}", destDir, cluster.getName());

            if (cluster.isKerberosEnabled()) {
                LOGGER.info("load kerberos config: {}", clusterCode);
                String krb5File = destDir + "/krb5.conf";
                String keytabFile = destDir + "/kerberos.keytab";

                if (StringUtils.isBlank(cluster.getKerberosConfig())) {
                    LOGGER.error("cluster {} kerberos enabled, krb5 conf is blank", clusterCode);
                    return;
                }
                if (cluster.getKerberosKeytab() == null || cluster.getKerberosKeytab().length == 0) {
                    LOGGER.error("cluster {} kerberos enabled, Keytab file is blank", clusterCode);
                    return;
                }

                FileUtils.write(new File(krb5File), cluster.getKerberosConfig(), StandardCharsets.UTF_8);
                FileUtils.writeByteArrayToFile(new File(keytabFile), cluster.getKerberosKeytab());

                KerberosInfo kerberosInfo = KerberosInfo.builder().enabled(true)
                        .krb5File(krb5File)
                        .keytabFile(keytabFile)
                        .principal(cluster.getKerberosUser()).build();
                CLUSTER_KERBEROS_INFO_MAP.put(clusterCode, kerberosInfo);
            }

            yarnConfigDirLists.put(clusterCode, destDir);
            hadoopConfList.put(clusterCode, configuration);
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

    private UserGroupInformation loginToKerberos(KerberosInfo kerberosInfo, String clusterCode) throws IOException {
        //https://stackoverflow.com/questions/34616676/should-i-call-ugi-checktgtandreloginfromkeytab-before-every-action-on-hadoop
        UserGroupInformation connectUgi = UserGroupInformation.getCurrentUser();
        if (!connectUgi.isFromKeytab()) {
            System.setProperty("java.security.krb5.conf", kerberosInfo.getKrb5File());
            Configuration conf = hadoopConfList.get(clusterCode);
            UserGroupInformation.setConfiguration(conf);
            connectUgi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(
                    kerberosInfo.getPrincipal(), kerberosInfo.getKeytabFile());
        }

        connectUgi.checkTGTAndReloginFromKeytab();

        return connectUgi;
    }

    public KerberosInfo getKerberosInfo(String clusterCode) {
        return CLUSTER_KERBEROS_INFO_MAP.get(clusterCode);
    }

    private YarnResource getResource(String yarnRMWebAppAddr) {
        try {
            String url = "http://" + yarnRMWebAppAddr + "/ws/v1/cluster/metrics";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            Map<String, Object> data = JsonUtils.toJavaObject(response.getBody(), new TypeReference<Map<String, Object>>() {});
            LinkedHashMap<String, Object> metrics = (LinkedHashMap<String, Object>) data.get("clusterMetrics");
            int availableMemoryMB = (Integer) metrics.get("availableMB");
            int availableVirtualCores = (Integer) metrics.get("availableVirtualCores");
            return new YarnResource(availableMemoryMB, availableVirtualCores);
        } catch (Exception e) {
            LOGGER.error("get cluster metrics {} error:{}", yarnRMWebAppAddr, e.getMessage());
            return null;
        }
    }

    private YarnResource getResourceByCluster(String clusterCode) {
        String yarnRMWebAppAddr = yarnRMWebAppAddrMap.get(clusterCode);
        YarnResource yarnResource = getResource(yarnRMWebAppAddr);
        if (yarnResource == null) {
            Configuration conf = hadoopConfList.get(clusterCode);
            String rmAddr = initYarnAddress(clusterCode, conf);
            return getResource(rmAddr);
        }
        return yarnResource;
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

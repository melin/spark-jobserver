package io.github.melin.spark.jobserver.deployment;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.support.ClusterConfig;
import io.github.melin.spark.jobserver.support.ClusterManager;
import io.github.melin.spark.jobserver.support.YarnClientService;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.util.FSUtils;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.ComputeType;
import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import io.github.melin.spark.jobserver.core.exception.ResouceLimitException;
import io.github.melin.spark.jobserver.core.exception.SparkJobException;
import io.github.melin.spark.jobserver.core.exception.SubmitTimeoutException;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import com.gitee.melin.bee.util.NetUtils;
import com.google.common.collect.Lists;
import io.github.melin.spark.jobserver.deployment.dto.DriverInfo;
import io.github.melin.spark.jobserver.deployment.dto.JobInstanceInfo;
import io.github.melin.spark.jobserver.deployment.dto.SubmitYarnResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.github.melin.spark.jobserver.SparkJobServerConf.*;

/**
 * huaixin 2022/4/14 21:08
 */
public abstract class AbstractSubmitService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSubmitService.class);

    @Autowired
    protected ConfigProperties config;

    @Autowired
    protected SparkDriverService driverService;

    @Autowired
    protected YarnClientService yarnClientService;

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    protected ClusterManager clusterManager;

    @Autowired
    private ClusterConfig clusterConfig;

    @Value("${spring.profiles.active}")
    protected String profiles;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUserName;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private static final String hostName = NetUtils.getLocalHost();

    public abstract DriverInfo allocateDriver(JobInstanceInfo jobInstanceInfo, ComputeType computeType, boolean shareDriver);

    /**
     * 通过spark-submit提交任务到集群
     */
    public SubmitYarnResult submitToYarn(JobInstanceInfo job, Long driverId) throws Exception {
        String jobInstanceCode = job.getInstanceCode();
        String clusterCode = job.getClusterCode();
        String yarnQueue = job.getYarnQueue();
        LOG.info("jobserver yarn queue: {}", yarnQueue);

        int submitTimeOut = config.getDriverSubmitTimeOutSeconds();
        SparkAppHandle sparkAppHandle = startApplication(job, clusterCode, driverId, yarnQueue);
        long appSubmitTime = System.currentTimeMillis();
        SparkAppHandle.State state = sparkAppHandle.getState();
        String applicationId;
        boolean updateApplicationId = false;
        while (state != SparkAppHandle.State.RUNNING) {
            applicationId = sparkAppHandle.getAppId();
            if (System.currentTimeMillis() - appSubmitTime > (submitTimeOut * 1000L)) {
                sparkAppHandle.kill();
                if (StringUtils.isNotBlank(applicationId)) {
                    yarnClientService.killYarnApp(clusterCode, applicationId);
                }
                throw new SubmitTimeoutException(hostName + ", Submit to yarn" +
                        " timed out(" + submitTimeOut + "s), applicationId: " + applicationId);
            }
            if (sparkAppHandle.getState() == SparkAppHandle.State.KILLED) {
                throw new RuntimeException(hostName + ", Submit to yarn failed , yarn status: " + state.name());
            }

            if (StringUtils.isBlank(applicationId)) {
                if (sparkAppHandle.getState() == SparkAppHandle.State.FAILED) {
                    if (sparkAppHandle.getError().isPresent()) {
                        Throwable error = sparkAppHandle.getError().get();
                        throw new RuntimeException("InstanceCode: " + jobInstanceCode + " 启动 jobserver 失败", error);
                    } else {
                        throw new RuntimeException("InstanceCode: " + jobInstanceCode + " 启动 jobserver 失败");
                    }
                }
            } else {
                //启动jobserver 失败
                ApplicationReport applicationReport = yarnClientService.getYarnApplicationReport(clusterCode, applicationId);
                if (applicationReport.getYarnApplicationState() == YarnApplicationState.FAILED) {
                    if (sparkAppHandle.getError().isPresent()) {
                        Throwable error = sparkAppHandle.getError().get();
                        throw new RuntimeException("InstanceCode: " + jobInstanceCode +
                                "启动 jobserver 失败, applicationId " + applicationId, error);
                    } else {
                        throw new RuntimeException("InstanceCode: " + jobInstanceCode +
                                " 启动 jobserver 失败, applicationId " + applicationId);
                    }
                }
            }

            if (!updateApplicationId && StringUtils.isNotBlank(applicationId)) {
                SparkDriver driver = driverService.getEntity(driverId);
                if (driver != null) {
                    driver.setApplicationId(applicationId);
                    driverService.updateEntity(driver);
                }
                updateApplicationId = true;
            }

            Thread.sleep(3000);
            state = sparkAppHandle.getState();
        }

        applicationId = sparkAppHandle.getAppId();
        sparkAppHandle.kill();

        String msg = "driver application " + applicationId + " 提交 yarn 耗时: "
                + (System.currentTimeMillis() - appSubmitTime) / 1000 + " s";
        LOG.info("InstanceCode: " + jobInstanceCode + ", " + msg);

        long getServerTime = System.currentTimeMillis();
        SparkDriver driver = driverService.queryDriverByAppId(applicationId);
        if (driver == null || driver.getServerPort() == -1) { // 默认值: -1
            int tryNum = 50;
            while (--tryNum > 0) {
                if (driver != null && driver.getStatus() != DriverStatus.INIT) {
                    break;
                }
                LOG.info("InstanceCode: " + jobInstanceCode + ", " + "waiting address for application: " + applicationId);

                Thread.sleep(2000);
                driver = driverService.queryDriverByAppId(applicationId);
            }
            if (driver == null) {
                throw new RuntimeException("Can not get Address about: " + applicationId);
            }
        }

        long execTime = (System.currentTimeMillis() - getServerTime) / 1000;
        msg =  "driver application " + applicationId + " 启动耗时：" + execTime + " s";
        LOG.info("InstanceCode: " + jobInstanceCode + ", " + msg);

        String sparkDriverUrl = driver.getSparkDriverUrl();
        LOG.info("InstanceCode {} Application {} stared at {}", jobInstanceCode, applicationId, sparkDriverUrl);
        return new SubmitYarnResult(applicationId, sparkDriverUrl, yarnQueue);
    }

    /**
     * 构造SparkLauncher
     */
    protected SparkAppHandle startApplication(JobInstanceInfo jobInstanceInfo, String clusterCode,
                                              Long driverId, String yarnQueue) throws Exception {

        String sparkHome = clusterConfig.getValue(clusterCode, JOBSERVER_SPARK_HOME);
        if (StringUtils.isBlank(sparkHome)) {
            throw new IllegalArgumentException("sparkHome 不能为空");
        } else {
            CommonUtils.checkDirExists(sparkHome);
            LOG.info("sparkHome: {}", sparkHome);
        }
        String confDir = clusterManager.loadYarnConfig(clusterCode);

        //获取 SPARK_HOME 地址
        Configuration hadoopConf = clusterManager.getHadoopConf(clusterCode);
        String defaultFS = hadoopConf.get("fs.defaultFS", "hdfs://dzcluster");
        String hadoopUserName = clusterConfig.getDriverHadoopUserName(clusterCode);

        HashMap<String, String> envParams = new HashMap<>();
        LOG.info("hadoop conf: {}", confDir);
        envParams.put("YARN_CONF_DIR", confDir);
        envParams.put("HADOOP_CONF_DIR", confDir);
        envParams.put("HADOOP_USER_NAME", hadoopUserName);

        SparkLauncher sparkLauncher = new SparkLauncher(envParams);

        boolean hiveEnabled = clusterConfig.getBoolean(clusterCode, JOBSERVER_DRIVER_HIVE_ENABLED);
        try (Stream<Path> paths = Files.walk(Paths.get(confDir))) {
            paths.forEach(path -> {
                String file = path.toFile().getPath();
                if (StringUtils.endsWith(file, "xml")) {
                    if (StringUtils.endsWith(file, "hive-site.xml")) {
                        if (hiveEnabled) {
                            LOG.info("hadoop conf: {}", file);
                            sparkLauncher.addFile(file);
                        }
                    } else {
                        LOG.info("hadoop conf: {}", file);
                        sparkLauncher.addFile(file);
                    }
                }
            });
        }
        LOG.info("fs.defaultFS: {}", defaultFS);

        String propertyFile = confDir + "/spark.conf";
        CommonUtils.checkDirExists(propertyFile);
        sparkLauncher.setPropertiesFile(propertyFile);

        LOG.info("aspectj version: {}", config.getAspectjVersion());
        String driverHome = defaultFS + clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_HOME);
        String aspectjweaverJar = "aspectjweaver-" + config.getAspectjVersion() + ".jar";
        String aspectjPath = driverHome + "/" + aspectjweaverJar;
        sparkLauncher.setConf("spark.yarn.dist.jars", aspectjPath);

        Properties params = addJobConfig(sparkLauncher, jobInstanceInfo);
        String driverExtraOptions = getJvmOpts(clusterCode, params, "driver");
        String executorExtraOptions = getJvmOpts(clusterCode, params, "executor");

        String sparkDriverExtraJavaOptionsConf = "-Dfile.encoding=UTF-8"
                + " -Dspring.profiles.active=" + profiles
                + " -DHADOOP_USER_NAME=" + hadoopUserName
                + " -Ddriver.hdfs.home=" + driverHome
                + " -Dspring.datasource.url=" + datasourceUrl
                + " -Dspring.datasource.username=" + datasourceUserName
                + " -Dspring.datasource.password=" + datasourcePassword
                + " -javaagent:" + aspectjweaverJar + " " + driverExtraOptions;

        boolean remoteDebug = clusterConfig.getBoolean(clusterCode, JOBSERVER_DRIVER_REMOTE_DEBUG_ENABLED);
        if (remoteDebug) {
            sparkDriverExtraJavaOptionsConf += " -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=50112 ";
        }

        String sparkExecutorExtraJavaOptionsConf = "-javaagent:" + aspectjweaverJar + " " + executorExtraOptions;
        String log4j2File = driverHome + "/log4j.properties";
        boolean hasLog4jFile = FSUtils.exists(hadoopConf, log4j2File);
        if (hasLog4jFile) {
            sparkLauncher.setConf("spark.yarn.dist.jars", log4j2File);
            sparkDriverExtraJavaOptionsConf += " -Dlog4j.configuration=log4j.properties ";
            sparkExecutorExtraJavaOptionsConf += " -Dlog4j.configuration=log4j.properties ";
        }

        boolean isKerberosEnabled = clusterManager.isKerberosEnabled(clusterCode);
        setSparkConf(clusterCode, sparkLauncher, hadoopConf, defaultFS, yarnQueue, driverHome, isKerberosEnabled);

        String conf = Base64.getEncoder().encodeToString("{}".getBytes(StandardCharsets.UTF_8));

        //driverJar 包 hdfs 地址
        String driverJarFile = driverHome + "/" +
                clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_JAR_NAME);
        LOG.info("driver kerberos enabled: {}, hive enabled: {}", isKerberosEnabled, hiveEnabled);

        List<String> programArgs = Lists.newArrayList("-j", String.valueOf(driverId),
                "-type", "driverserver", "-conf", conf);
        if (hiveEnabled) {
            programArgs.add("-hive");
        }

        pythonEnvConf(sparkLauncher, clusterCode);

        LOG.info("directory: {}", sparkHome);
        String appName = JobServerUtils.appName(profiles);
        return sparkLauncher.setAppName(appName)
                .setVerbose(false)
                .setSparkHome(sparkHome)
                .setMaster("yarn")
                .directory(new File(sparkHome))
                .setDeployMode("cluster")
                .setConf("spark.yarn.applicationType", "spark-jobserver")
                .setConf("spark.jobserver.host", hostName)
                .setConf("spark.driver.extraJavaOptions", sparkDriverExtraJavaOptionsConf)
                .setConf("spark.executor.extraJavaOptions", sparkExecutorExtraJavaOptionsConf)
                .setMainClass("io.github.melin.spark.jobserver.driver.SparkDriverApp")
                .setAppResource(driverJarFile)
                .addAppArgs(programArgs.toArray(new String[0]))
                .startApplication();
    }

    private void pythonEnvConf(SparkLauncher sparkLauncher, String clusterCode) {
        String pythonPath = clusterConfig.getValue(clusterCode, JOBSERVER_PYTHON_HOME);
        String pysparkPath = clusterConfig.getValue(clusterCode, JOBSERVER_PYSPARK_PATH);

        if (StringUtils.isNotBlank(pythonPath) && StringUtils.isNotBlank(pysparkPath)) {
            sparkLauncher.setConf("spark.pyspark.driver.python", pythonPath);
            sparkLauncher.setConf("spark.pyspark.python", pythonPath);
            sparkLauncher.setConf("spark.executorEnv.PYTHONPATH", pysparkPath);

            LOG.info("pythonPath = {}", pythonPath);
            LOG.info("pysparkPath = {}", pysparkPath);
        }
    }

    private Properties addJobConfig(SparkLauncher sparkLauncher, JobInstanceInfo jobInstanceInfo) throws IOException {
        Properties properties = new Properties();
        String jobConfig = jobInstanceInfo.getJobConfig();
        if (StringUtils.isNotBlank(jobConfig)) {
            properties.load(new StringReader(jobConfig));

            for (Object key : properties.keySet()) {
                String propKey = (String) key;
                String value = properties.getProperty(propKey);
                sparkLauncher.setConf(propKey, value);
                LOG.info("instance config: {} = {}", key, value);
            }
        }

        return properties;
    }

    /*
     * 设置spark.yarn.jars 参数
     */
    private void setSparkConf(String clusterCode, SparkLauncher sparkLauncher, Configuration hadoopConf,
                              String defaultFS, String yarnQueue, String driverHome, boolean isKerberosEnabled) throws Exception {
        //设置队列
        if (StringUtils.isNotBlank(yarnQueue)) {
            sparkLauncher.setConf("spark.yarn.queue", yarnQueue);
        } else {
            yarnQueue = clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_YAEN_QUEUE_NAME);
            sparkLauncher.setConf("spark.yarn.queue", yarnQueue);
        }

        if (isKerberosEnabled) {
            LOG.info("启动 kerberos 认证}");
            // @TODO
        }

        // 注意不要删除参数，避免分区表丢失数据
        sparkLauncher.setConf("spark.sql.sources.partitionOverwriteMode", "dynamic");

        String sparkVersion = clusterConfig.getValue(clusterCode, JOBSERVER_SPARK_VERSION);
        String sparkYarnJarsDir = driverHome + "/spark-" + sparkVersion;
        FSUtils.checkHdfsPathExist(hadoopConf, sparkYarnJarsDir);
        String yarnJars =  sparkYarnJarsDir + "/*";
        String driverJar = driverHome + "/" + clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_JAR_NAME);
        yarnJars = yarnJars + "," + driverJar;

        String customSparkYarnJars = clusterConfig.getValue(clusterCode, JOBSERVER_CUSTOM_SPARK_YARN_JARS);
        if (StringUtils.isNotBlank(customSparkYarnJars)) {
            if (!StringUtils.startsWith(customSparkYarnJars, defaultFS)) {
                customSparkYarnJars = defaultFS + customSparkYarnJars;
            }

            yarnJars = yarnJars + "," + customSparkYarnJars;
        }

        LOG.info("spark.yarn.jars = {}", yarnJars);
        sparkLauncher.setConf("spark.yarn.jars", yarnJars + "," + driverJar);

        String sparkHistoryDir = driverHome + "/spark-eventLogs";
        FSUtils.mkdir(hadoopConf, sparkHistoryDir);
        sparkLauncher.setConf("spark.eventLog.dir", sparkHistoryDir);

        String sparkCheckPointDir = driverHome + "/spark-checkpoints";
        FSUtils.mkdir(hadoopConf, sparkCheckPointDir);
        sparkLauncher.setConf("spark.sql.streaming.checkpointLocation", sparkCheckPointDir);
    }

    /**
     * driver & executor jvm 参数
     */
    private String getJvmOpts(String clusterCode, Properties params, String role) {
        String key = String.format("spark.job.%s.java.opts", role);
        String jvmOptions = "driver".equals(role) ?
                clusterConfig.getValue(clusterCode, JOBSERVER_JOB_DRIVER_JAVA_OPTS) :
                clusterConfig.getValue(clusterCode, JOBSERVER_JOB_EXECUTOR_JAVA_OPTS);

        if (StringUtils.isNotBlank(jvmOptions)) {
            jvmOptions = jvmOptions + " " + params.getProperty(key, "");
        } else {
            jvmOptions = params.getProperty(key, "");
        }
        LOG.info("{} jvm options: {}", role, jvmOptions);
        return jvmOptions;
    }

    protected void checkLocalAvailableMemory() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        int kb = 1024;
        long totalMemorySize = hal.getMemory().getTotal() / kb;
        long availableMemorySize = hal.getMemory().getAvailable() / kb;
        long minAvailableMem = config.getLocalMinMemoryMb();

        String totalMemorySizeRead = CommonUtils.convertUnit(totalMemorySize);
        String availableMemorySizeRead = CommonUtils.convertUnit(availableMemorySize);

        if (availableMemorySize < (minAvailableMem * 1024)) {
            String msg = "当前系统总内存: " + totalMemorySizeRead + ", 可用内存: " + availableMemorySizeRead
                    + ", 要求最小可用内存: " + minAvailableMem + "m " + NetUtils.getLocalHost();
            msg = msg + ", 可调整参数：jobserver.local-min-memory-mb, 单位兆";
            LOG.warn(msg);
            throw new ResouceLimitException(msg);
        }
    }

    /**
     * 初始化jobserver实例
     */
    protected Long initSparkDriver(String clusterCode, boolean shareDriver) {
        Long driverId;
        try {
            SparkDriver driver = SparkDriver.buildSparkDriver(clusterCode, shareDriver);
            String yarnQueue = clusterConfig.getValue(clusterCode, JOBSERVER_DRIVER_YAEN_QUEUE_NAME);
            driver.setYarnQueue(yarnQueue);

            while (!redisLeaderElection.trylock()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            LOG.info("Get redis lock");

            long initDriverCount = driverService.queryCount("status", DriverStatus.INIT);
            long maxConcurrentSubmitCount = clusterConfig.getInt(clusterCode, JOBSERVER_SUBMIT_DRIVER_MAX_CONCURRENT_COUNT);
            if (initDriverCount > maxConcurrentSubmitCount) {
                String msg = "当前正在提交jobserver数量: " + initDriverCount + ", 最大提交数量: " + maxConcurrentSubmitCount
                        + ", 可调整参数: jobserver.concurrent.submit.max.num";
                throw new ResouceLimitException(msg);
            }
            driverId = driverService.insertEntity(driver);
        } catch (SparkJobException jobException) {
            throw jobException;
        } catch (Exception e1) {
            throw new RuntimeException(e1.getMessage());
        } finally {
            redisLeaderElection.deletelock();
        }

        if (driverId == null) {
            throw new RuntimeException("Init Spark Driver Error");
        }
        return driverId;
    }

    protected void checkMaxDriverCount(String clusterCode) {
        long driverCount = driverService.queryCount();
        int driverMaxCount = clusterConfig.getInt(clusterCode, JOBSERVER_DRIVER_MAX_COUNT);
        if (driverCount >= driverMaxCount) {
            String msg = "当前正在运行任务数量已达最大数量限制: " + driverMaxCount + "，请休息一会再重试！";
            throw new ResouceLimitException(msg);
        }
    }
}

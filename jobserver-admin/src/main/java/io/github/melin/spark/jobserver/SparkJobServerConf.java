package io.github.melin.spark.jobserver;

import com.gitee.melin.bee.core.conf.*;
import org.apache.commons.lang3.SystemUtils;

import java.lang.reflect.Field;

/**
 * @author melin
 */
public class SparkJobServerConf extends BeeConf {

    public static final String DEFAULT_SPARK_VERSION = "spark-3.3.0-bin-hadoop2";

    public static final ConfigEntry<Integer> JOBSERVER_DRIVER_MIN_COUNT =
            buildConf("jobserver.driver.min.count")
                    .doc("driver 最小初始化数量")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(1);

    public static final ConfigEntry<Integer> JOBSERVER_DRIVER_MAX_COUNT =
            buildConf("jobserver.driver.max.count")
                    .doc("driver 最大数量")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(3);

    public static final ConfigEntry<Integer> JOBSERVER_DRIVER_RUN_MAX_INSTANCE_COUNT =
            buildConf("jobserver.driver.run.max.instance.count")
                    .doc("driver 运行最大作业实例数量，超过最大实例数量，关闭此Driver")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(30);

    public static final ConfigEntry<Long> JOBSERVER_DRIVER_MIN_PRIMARY_ID =
            buildConf("jobserver.driver.min.primary.id")
                    .doc("driver 运行最大作业实例数量，超过最大实例数量，关闭此Driver")
                    .version("3.3.0")
                    .longConf()
                    .createWithDefault(0L);

    public static final ConfigEntry<Integer> JOBSERVER_DRIVER_MAX_IDLE_TIME_SECONDS =
            buildConf("jobserver.driver.max.idle.time.seconds")
                    .doc("driver 最大空闲时间, 默认10分钟")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(60 * 10);

    public static final ConfigEntry<String> JOBSERVER_DRIVER_HADOOP_USER_NAME =
            buildConf("jobserver.driver.hadoop.user.name")
                    .doc("driver hadoop user name, 默认为 jobserver 系统账号")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault(SystemUtils.getUserName());

    public static final ConfigEntry<String> JOBSERVER_DRIVER_HOME =
            buildConf("jobserver.driver.home")
                    .doc("driver home 路径，一般为 hdfs 路径")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("/user/superior/spark-jobserver");

    public static final ConfigEntry<String> JOBSERVER_DRIVER_JAR_NAME =
            buildConf("jobserver.driver.jar.name")
                    .doc("driver 部署 jar 文件名")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("spark-jobserver-driver-0.1.0.jar");

    public static final ConfigEntry<String> JOBSERVER_DRIVER_YAEN_QUEUE_NAME =
            buildConf("jobserver.driver.yarn.queue.name")
                    .doc("driver 运行yarn 队列名")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("default");

    public static final ConfigEntry<String> JOBSERVER_DRIVER_DATATUNNEL_JARS_DIR =
            buildConf("jobserver.driver.datatunnel.jars.dir")
                    .doc("drirver datatunnel jar 目录名")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("datatunnel-3.3.0");

    public static final ConfigEntry<Boolean> JOBSERVER_DRIVER_HIVE_ENABLED =
            buildConf("jobserver.driver.hive.enabled")
                    .doc("spark hive enabled")
                    .version("3.3.0")
                    .booleanConf()
                    .createWithDefault(true);

    public static final ConfigEntry<Boolean> JOBSERVER_DRIVER_REMOTE_DEBUG_ENABLED =
            buildConf("jobserver.driver.remote.debug.enabled")
                    .doc("spark driver 开启远程调试")
                    .version("3.3.0")
                    .booleanConf()
                    .createWithDefault(true);

    //------------------------------------------------------------------------------

    public static final ConfigEntry<Integer> JOBSERVER_SUBMIT_DRIVER_MAX_CONCURRENT_COUNT =
            buildConf("jobserver.submit.driver.max.concurrent.count")
                    .doc("Jobserver 提交driver，最大并发数量，")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(2);

    public static final ConfigEntry<String> JOBSERVER_SPARK_HOME =
            buildConf("jobserver.spark.home")
                    .doc("jobserver 本地 spark home 路径")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault(SystemUtils.USER_HOME + "/" + DEFAULT_SPARK_VERSION);

    public static final ConfigEntry<String> JOBSERVER_PYTHON_HOME =
            buildConf("jobserver.python.home")
                    .doc("jobserver 本地 python home 路径")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("/home/devops/anaconda3/bin/python");

    public static final ConfigEntry<String> JOBSERVER_PYSPARK_PATH =
            buildConf("jobserver.pyspark.path")
                    .doc("pyspark 包路径 路径")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("/home/devops/pyspark/3.3.0/pyspark.zip:/home/devops/pyspark/3.3.0/py4j-0.10.9.5-src.zip");

    public static final ConfigEntry<String> JOBSERVER_SPARK_VERSION =
            buildConf("jobserver.spark.version")
                    .doc("jobserver spark verion")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("3.3.0");

    public static final ConfigEntry<Integer> JOBSERVER_YARN_MIN_MEMORY_MB =
            buildConf("jobserver.yarn.min.memory.mb")
                    .doc("yarn 最小剩余内存")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(4096);

    public static final ConfigEntry<Integer> JOBSERVER_YARN_MIN_CPU_CORES =
            buildConf("jobserver.yarn.min.cpu.cores")
                    .doc("yarn 最小剩余cpu 数量")
                    .version("3.3.0")
                    .intConf()
                    .createWithDefault(5);

    public static final ConfigEntry<String> JOBSERVER_YARN_PROXY_URI =
            buildConf("jobserver.yarn.proxy.uri")
                    .doc("yarn 代理地址，用于访问spark 控制台")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("");

    public static final ConfigEntry<String> JOBSERVER_JOB_DRIVER_EXTRA_JAVA_OPTIONS =
            buildConf("jobserver.job.driver.extraJavaOptions")
                    .doc("driver jvm 参数")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("");

    public static final ConfigEntry<String> JOBSERVER_JOB_EXECUTOR_EXTRA_JAVA_OPTIONS =
            buildConf("jobserver.job.executor.extraJavaOptions")
                    .doc("executor jvm 参数")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("");

    public static final ConfigEntry<String> JOBSERVER_CUSTOM_SPARK_YARN_JARS =
            buildConf("jobserver.custom.spark.yarn.jars")
                    .doc("用户自定spark yarn jars")
                    .version("3.3.0")
                    .stringConf()
                    .createWithDefault("");

    public static String printConfWithDefaultValue() throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = SparkJobServerConf.class.getDeclaredFields();
        for (Field field : fields) {
            Object result = field.get(null);
            if (result instanceof ConfigEntry) {
                ConfigEntry entry = (ConfigEntry) result;
                sb.append(entry.getKey() + " = " + entry.getDefaultValue()).append("\n");
            }
        }

        return sb.toString();
    }
}

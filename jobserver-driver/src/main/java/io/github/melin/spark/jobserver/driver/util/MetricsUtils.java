package io.github.melin.spark.jobserver.driver.util;

import io.github.melin.spark.jobserver.core.enums.JobType;
import io.github.melin.spark.jobserver.core.util.CommonUtils;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.driver.listener.DriverMetric;
import io.github.melin.spark.jobserver.driver.listener.ExecutorMetric;
import io.github.melin.spark.jobserver.driver.listener.MetricsData;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by libinsong on 2018/8/18.
 */
public class MetricsUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsUtils.class);

    public static void logMetricsCollect(JobType jobType) {
        try {
            SparkConf sparkConf = SparkDriverEnv.getSparkSession().sparkContext().getConf();
            long executorMemory = sparkConf.getSizeAsKb("spark.executor.memory", "10485760");
            long driverMemory = sparkConf.getSizeAsKb("spark.driver.memory", "5242880");

            long executorMemoryOverhead = sparkConf.getSizeAsKb("spark.executor.memoryOverhead", "3145728");
            long driverMemoryOverhead = sparkConf.getSizeAsKb("spark.driver.memoryOverhead", "2097152");

            DriverMetric driverMetric = MetricsData.DRIVER_METRIC;
            long currentDriverMemory = driverMetric.getJvmHeapMemory() + driverMetric.getJvmOffHeapMemory();
            long currentPythonMemory = driverMetric.getProcessTreePythonRSSMemory();

            long executorMinHeapMemory = Long.MAX_VALUE;
            long executorAverageHeapMemory = 0;
            long executorMaxHeapMemory = 0;

            int size = MetricsData.EXECUTOR_METRICS.size();
            long totalMemory = 0L;
            for (ExecutorMetric metric : MetricsData.EXECUTOR_METRICS.values()) {
                long memory = metric.getProcessTreeJVMRSSMemory();
                if (executorMinHeapMemory > memory) {
                    executorMinHeapMemory = memory;
                }
                if (executorMaxHeapMemory < memory) {
                    executorMaxHeapMemory = memory;
                }

                totalMemory += memory;
            }
            if (size > 0) {
                executorAverageHeapMemory = totalMemory / size;
            } else {
                executorMinHeapMemory = 0;
            }

            long executorPythonMinHeapMemory = 0;
            long executorPythonAverageHeapMemory = 0;
            long executorPythonMaxHeapMemory = 0;

            if (JobType.SPARK_PYTHON == jobType) {
                size = MetricsData.EXECUTOR_METRICS.size();
                totalMemory = 0L;
                for (ExecutorMetric metric : MetricsData.EXECUTOR_METRICS.values()) {
                    long memory = metric.getProcessTreePythonRSSMemory();
                    if (executorPythonMinHeapMemory > memory) {
                        executorPythonMinHeapMemory = memory;
                    }
                    if (executorPythonMaxHeapMemory < memory) {
                        executorPythonMaxHeapMemory = memory;
                    }

                    totalMemory += memory;
                }
                if (size > 0) {
                    executorPythonAverageHeapMemory = totalMemory / size;
                }
            }

            SparkConf conf = SparkDriverEnv.getSparkSession().sparkContext().getConf();
            String defaultDriverMemory = conf.get("spark.default.driver.memory", "0G");
            String defaultExecutorMemory = conf.get("spark.default.executor.memory", "0G");
            String defaultDriverMemoryOverhead = conf.get("spark.default.driver.memoryOverhead", "0G");
            String defaultExecutorMemoryOverhead = conf.get("spark.default.executor.memoryOverhead", "0G");
            long defaultDriverMemoryKb = CommonUtils.byteStringAsKb(defaultDriverMemory);
            long defaultExecutorMemoryKb = CommonUtils.byteStringAsKb(defaultExecutorMemory);
            long defaultDriverMemoryOverheadKb = CommonUtils.byteStringAsKb(defaultDriverMemoryOverhead);
            long defaultExecutorMemoryOverheadKb = CommonUtils.byteStringAsKb(defaultExecutorMemoryOverhead);

            long totalExecutorMem = executorMemory + executorMemoryOverhead;
            long totalDriverMem = driverMemory + driverMemoryOverhead;

            double driverMemoryRatio = (currentDriverMemory + currentPythonMemory) * 1.0 / totalDriverMem;
            double executorMemoryRatio = executorMaxHeapMemory * 1.0 / totalExecutorMem;
            double driverMemoryOverheadRatio = (currentPythonMemory + executorPythonMaxHeapMemory) * 1.0 / driverMemoryOverhead;
            double executorMemoryOverheadRatio = executorPythonMaxHeapMemory * 1.0 / executorMemoryOverhead;

            List<String> msgs = Lists.newArrayList();
            if (currentDriverMemory > 0 && driverMemory > defaultDriverMemoryKb
                    && driverMemoryRatio < 0.5) {
                msgs.add("Driver 内存利用率: " + CommonUtils.DECIMAL_FORMAT.format(driverMemoryRatio)
                        + ", 建议您下调 spark.driver.memory 参数值或者删除参数配置，节约宝贵资源，默认值：" + defaultDriverMemory);
            }
            if ((JobType.SPARK_PYTHON == jobType)
                    && currentDriverMemory > 0 && driverMemoryOverhead > defaultDriverMemoryOverheadKb
                    && driverMemoryOverheadRatio < 0.5) {
                msgs.add("Driver python 内存利用率: " + CommonUtils.DECIMAL_FORMAT.format(driverMemoryOverheadRatio)
                        + ", 建议您下调 spark.driver.memoryOverhead 参数值或者删除参数配置，节约宝贵资源，默认值：" + defaultDriverMemoryOverhead);
            }
            if (executorMaxHeapMemory > 0 && executorMemory > defaultExecutorMemoryKb
                    && executorMemoryRatio < 0.5) {
                msgs.add("Executor 内存利用率: " + CommonUtils.DECIMAL_FORMAT.format(executorMemoryRatio)
                        + ", 建议您下调 spark.executor.memory 参数值或者删除参数配置，节约宝贵资源，默认值：" + defaultExecutorMemory);
            }
            if ((JobType.SPARK_PYTHON == jobType)
                    && executorPythonMaxHeapMemory > 0 && executorMemoryOverhead > defaultExecutorMemoryOverheadKb
                    && executorMemoryOverheadRatio < 0.5) {
                msgs.add("Executor python 内存利用率: " + CommonUtils.DECIMAL_FORMAT.format(executorMemoryOverheadRatio)
                        + ", 建议您下调 spark.executor.memoryOverhead 参数值或者删除参数配置，节约宝贵资源，默认值：" + defaultExecutorMemoryOverhead);
            }

            StringBuilder driverSB = new StringBuilder("Driver 配置内存 = ").append(CommonUtils.convertUnit(totalDriverMem));
            driverSB.append(", Driver 实际使用内存 = ").append(CommonUtils.convertUnit(currentDriverMemory));
            if (JobType.SPARK_PYTHON == jobType) {
                driverSB.append(", Driver python 可用内存 = ").append(CommonUtils.convertUnit(driverMemoryOverhead));
                driverSB.append(", Driver Python 进程实际使用内存 = ").append(CommonUtils.convertUnit(currentPythonMemory));
            }

            StringBuilder executorSB = new StringBuilder("JVM 配置内存 = ").append(CommonUtils.convertUnit(totalExecutorMem));
            executorSB.append(", JVM 最小使用内存 = ").append(CommonUtils.convertUnit(executorMinHeapMemory));
            executorSB.append(", JVM 最大使用内存 = ").append(CommonUtils.convertUnit(executorMaxHeapMemory));
            executorSB.append(", JVM 平均使用内存 = ").append(CommonUtils.convertUnit(executorAverageHeapMemory));

            LogUtils.info("Driver 内存统计信息 = " + driverSB);
            LogUtils.info("Executor JVM 内存统计信息：" + executorSB);
            if (JobType.SPARK_PYTHON == jobType) {
                StringBuilder executorPythonSB = new StringBuilder("Python 最大可用内存 = ").append(CommonUtils.convertUnit(executorMemoryOverhead));
                executorPythonSB.append(", Python 最小使用内存 = ").append(CommonUtils.convertUnit(executorPythonMinHeapMemory));
                executorPythonSB.append(", Python 最大使用内存 = ").append(CommonUtils.convertUnit(executorPythonMaxHeapMemory));
                executorPythonSB.append(", Python 平均使用内存 = ").append(CommonUtils.convertUnit(executorPythonAverageHeapMemory));
                LogUtils.info("Executor Python 内存统计信息：" + executorPythonSB);
            }

            if (msgs.size() > 0) {
                LogUtils.warn(StringUtils.join(msgs, "\n\t"));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}

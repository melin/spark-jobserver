package io.github.melin.spark.jobserver.driver.listener;

import io.github.melin.spark.jobserver.api.JobServerLogEvent;
import io.github.melin.spark.jobserver.api.LogLevel;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.scheduler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import scala.collection.JavaConverters;

import java.util.List;

/**
 * huaixin 2022/4/7 11:04 AM
 */
@Service
public class JobServerListener extends SparkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServerListener.class);

    @Override
    public void onJobStart(SparkListenerJobStart jobStart) {
        List<Object> list = JavaConverters.seqAsJavaList(jobStart.stageIds());
        MetricsData.JOB_STAGES_MAP.put(jobStart.jobId(), list);
    }

    @Override
    public void onStageSubmitted(SparkListenerStageSubmitted stageSubmitted) {
        StageInfo stageInfo = stageSubmitted.stageInfo();

        Integer stageId = stageInfo.stageId();
        Integer numTasks = stageInfo.numTasks();

        MetricsData.STAGE_TASK_PROGRESS_MAP.put(stageId, Pair.of(numTasks, 0));
        MetricsData.STAGE_INFO_MAP.put(stageInfo.stageId(), stageInfo);
    }

    @Override
    public void onStageCompleted(SparkListenerStageCompleted stageCompleted) {
        StageInfo stageInfo = stageCompleted.stageInfo();
        MetricsData.STAGE_INFO_MAP.put(stageInfo.stageId(), stageInfo);
    }

    @Override
    public void onTaskEnd(SparkListenerTaskEnd taskEnd) {
        Integer stageId = taskEnd.stageId();
        Pair<Integer, Integer> pair = MetricsData.STAGE_TASK_PROGRESS_MAP.get(stageId);
        if (pair != null) {
            if (taskEnd.taskInfo().successful()) {
                Integer numTasks = pair.getKey();
                Integer completeTask = pair.getValue();
                MetricsData.STAGE_TASK_PROGRESS_MAP.put(stageId, Pair.of(numTasks, completeTask + 1));
            }
        }
    }

    @Override
    public void onExecutorRemoved(SparkListenerExecutorRemoved executorRemoved) {
        String executorId = executorRemoved.executorId();
        MetricsData.EXECUTOR_METRICS.remove(executorId);
    }

    @Override
    public void onOtherEvent(SparkListenerEvent event) {
        if (event instanceof JobServerLogEvent) {
            JobServerLogEvent logEvent = (JobServerLogEvent) event;
            LogLevel messageType = logEvent.getLogLevel();
            String message = logEvent.getMessage();
            if (LogLevel.INFO == messageType && StringUtils.isNotBlank(message)) {
                LogUtils.info(message);
            } else if (LogLevel.WARN == messageType && StringUtils.isNotBlank(message)) {
                LogUtils.warn(message);
            } else if (LogLevel.ERROR == messageType && StringUtils.isNotBlank(message)) {
                LogUtils.error(message);
            } else if (LogLevel.STDOUT == messageType && StringUtils.isNotBlank(message)) {
                LogUtils.stdout(message);
            } else {
                LOGGER.warn("event info: {}", logEvent);
            }
        }
    }

    @Override
    public void onExecutorMetricsUpdate(SparkListenerExecutorMetricsUpdate executorMetricsUpdate) {
        try {
            JavaConverters.mapAsJavaMap(executorMetricsUpdate.executorUpdates()).forEach((tuple2, metrics) -> {
                if ("driver".equals(executorMetricsUpdate.execId())) {
                    DriverMetric driverMetric = MetricsData.DRIVER_METRIC;
                    driverMetric.setJvmHeapMemory(metrics.getMetricValue("JVMHeapMemory"));
                    driverMetric.setJvmOffHeapMemory(metrics.getMetricValue("JVMOffHeapMemory"));
                    driverMetric.setDirectPoolMemory(metrics.getMetricValue("DirectPoolMemory"));
                    driverMetric.setMappedPoolMemory(metrics.getMetricValue("MappedPoolMemory"));

                    driverMetric.setProcessTreeJVMVMemory(metrics.getMetricValue("ProcessTreeJVMVMemory"));
                    driverMetric.setProcessTreeJVMRSSMemory(metrics.getMetricValue("ProcessTreeJVMRSSMemory"));
                    driverMetric.setProcessTreePythonVMemory(metrics.getMetricValue("ProcessTreePythonVMemory"));
                    driverMetric.setProcessTreePythonRSSMemory(metrics.getMetricValue("ProcessTreePythonRSSMemory"));
                    driverMetric.setProcessTreeOtherVMemory(metrics.getMetricValue("ProcessTreeOtherVMemory"));
                    driverMetric.setProcessTreeOtherRSSMemory(metrics.getMetricValue("ProcessTreeOtherRSSMemory"));
                } else {
                    String execId = executorMetricsUpdate.execId();
                    ExecutorMetric executorMetric = MetricsData.EXECUTOR_METRICS.get(execId);
                    if (executorMetric == null) {
                        executorMetric = new ExecutorMetric();
                        MetricsData.EXECUTOR_METRICS.put(execId, executorMetric);
                    }

                    executorMetric.setExecId(execId);
                    executorMetric.setJvmHeapMemory(metrics.getMetricValue("JVMHeapMemory"));
                    executorMetric.setJvmOffHeapMemory(metrics.getMetricValue("JVMOffHeapMemory"));
                    executorMetric.setOnHeapExecutionMemory(metrics.getMetricValue("OnHeapExecutionMemory"));
                    executorMetric.setOffHeapExecutionMemory(metrics.getMetricValue("OffHeapExecutionMemory"));
                    executorMetric.setOnHeapStorageMemory(metrics.getMetricValue("OnHeapStorageMemory"));
                    executorMetric.setOffHeapStorageMemory(metrics.getMetricValue("OffHeapStorageMemory"));
                    executorMetric.setOnHeapUnifiedMemory(metrics.getMetricValue("OnHeapUnifiedMemory"));
                    executorMetric.setOffHeapUnifiedMemory(metrics.getMetricValue("OffHeapUnifiedMemory"));
                    executorMetric.setDirectPoolMemory(metrics.getMetricValue("DirectPoolMemory"));
                    executorMetric.setMappedPoolMemory(metrics.getMetricValue("MappedPoolMemory"));

                    executorMetric.setProcessTreeJVMVMemory(metrics.getMetricValue("ProcessTreeJVMVMemory"));
                    executorMetric.setProcessTreeJVMRSSMemory(metrics.getMetricValue("ProcessTreeJVMRSSMemory"));
                    executorMetric.setProcessTreePythonVMemory(metrics.getMetricValue("ProcessTreePythonVMemory"));
                    executorMetric.setProcessTreePythonRSSMemory(metrics.getMetricValue("ProcessTreePythonRSSMemory"));
                    executorMetric.setProcessTreeOtherVMemory(metrics.getMetricValue("ProcessTreeOtherVMemory"));
                    executorMetric.setProcessTreeOtherRSSMemory(metrics.getMetricValue("ProcessTreeOtherRSSMemory"));
                }
            });
        } catch (Exception e) {
            LOGGER.error("采集内存信息失败：" + e.getMessage());
        }
    }
}

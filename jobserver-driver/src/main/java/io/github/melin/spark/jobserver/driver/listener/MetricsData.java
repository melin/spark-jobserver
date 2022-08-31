package io.github.melin.spark.jobserver.driver.listener;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.scheduler.StageInfo;
import org.apache.spark.storage.RDDInfo;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

/**
 * huaixin 2022/4/7 11:54 AM
 */
public class MetricsData {

    public static final DriverMetric DRIVER_METRIC = new DriverMetric();

    /**
     * (executerId -> ExecutorMetric)
     */
    public static final ConcurrentMap<String, ExecutorMetric> EXECUTOR_METRICS = Maps.newConcurrentMap();

    /**
     * (jobId -> stageId列表)
     */
    public static final HashMap<Integer, List<Object>> JOB_STAGES_MAP = Maps.newHashMap();

    /**
     * (stageId -> (task总数, 已完成数))
     */
    public static final HashMap<Integer, Pair<Integer, Integer>> STAGE_TASK_PROGRESS_MAP = Maps.newHashMap();

    /**
     * stage列表
     */
    public static final HashMap<Integer, StageInfo> STAGE_INFO_MAP = Maps.newHashMap();

    public static void clearCaches() {
        EXECUTOR_METRICS.clear();
        JOB_STAGES_MAP.clear();
        STAGE_TASK_PROGRESS_MAP.clear();
        STAGE_INFO_MAP.clear();
    }

    /**
     * 获取某个stage下面的task完成情况
     */
    private static String getStageInfo(Integer stageId) {
        StringBuilder sb = new StringBuilder("stage");
        if (!STAGE_TASK_PROGRESS_MAP.containsKey(stageId)) {
            return "";
        }
        Pair<Integer, Integer> pair = STAGE_TASK_PROGRESS_MAP.get(stageId);
        sb.append(stageId).append("(").append(pair.getValue()).append("/").append(pair.getKey()).append(")");

        String stageType = "";
        if (STAGE_INFO_MAP.containsKey(stageId)) {
            StageInfo stageInfo = STAGE_INFO_MAP.get(stageId);
            for (RDDInfo rddInfo : JavaConverters.seqAsJavaList(stageInfo.rddInfos())) {
                if (rddInfo.name().contains("ZippedPartitionsRDD") && rddInfo.parentIds().size() >= 2) {
                    stageType = "Join";
                } else if (rddInfo.name().contains("UnionRDD") && rddInfo.parentIds().size() >= 2) {
                    stageType = "Union";
                }
            }
        }
        if (StringUtils.isNotBlank(stageType)) {
            sb.append("[").append(stageType).append("]");
        }

        return sb.toString();
    }

    /**
     * 获取某个job下面的stage完成情况
     */
    private static String getJobInfo(Integer jobId) {
        StringBuilder sb = new StringBuilder("Job");
        TreeMap<Integer, String> map = new TreeMap<>();
        for (Object stageId : JOB_STAGES_MAP.get(jobId)) {
            String stageInfo = getStageInfo((Integer) stageId);
            if (StringUtils.isNotBlank(stageInfo)) {
                map.put((Integer) stageId, stageInfo);
            }
        }

        ArrayList<String> list = new ArrayList<>();
        for (Integer integer : map.keySet()) {
            list.add(map.get(integer));
        }

        sb.append(jobId).append("/").append(StringUtils.join(list, "/"));
        return sb.toString();
    }

    /**
     * 获取当前对执行进度日志
     */
    public static String getJobProgressLog() {
        StringBuilder sb = new StringBuilder();

        TreeMap<Integer, String> map = new TreeMap<>();
        for (Integer jobId : JOB_STAGES_MAP.keySet()) {
            map.put(jobId, getJobInfo(jobId));
        }
        ArrayList<String> list = new ArrayList<>();
        for (Integer integer : map.keySet()) {
            list.add(map.get(integer));
        }
        sb.append(StringUtils.join(list, "; "));

        return sb.toString();
    }
}

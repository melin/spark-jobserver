package io.github.melin.spark.jobserver.scheduler;

import io.github.melin.spark.jobserver.deployment.SparkJobSubmitService;
import io.github.melin.spark.jobserver.deployment.dto.JobInstanceInfo;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import com.gitee.melin.bee.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static io.github.melin.spark.jobserver.util.Constant.ROOT_SCHEDULE_NODE;
import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.FAILED;
import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.WAITING;

/**
 * 每隔一分钟，检测待运行的作业实例是否要运行；
 */
@Service
public class StartJobInstanceScheduler {

    private static final Logger LOG = LoggerFactory.getLogger("instanceLogger");

    private static final int CPU_NUMS = 1; //Runtime.getRuntime().availableProcessors();

    @Autowired
    private JobInstanceService jobInstanceService;

    @Autowired
    private SparkJobSubmitService sparkJobSubmitService;

    private final ExecutorService startInstanceExecutor = ThreadUtils.newDaemonFixedThreadPool(CPU_NUMS, "start-job-executor");

    @Scheduled(fixedDelay = 200) //200ms
    public void startJobExecute() {
        List<Long> jobInstIds = jobInstanceService.findScheduleInstances();

        if (jobInstIds.size() > 0) {
            List<List<Long>> groups = JobServerUtils.partition(jobInstIds, CPU_NUMS);
            for (List<Long> ids : groups) {
                CountDownLatch latch = new CountDownLatch(ids.size());
                ids.forEach(instanceId -> {
                    try {
                        startInstanceExecutor.execute(() -> {
                            try {
                                JobInstance instance = jobInstanceService.getEntity(instanceId);
                                boolean startInstance = checkInstanceStart(instance);
                                if (startInstance) {
                                    JobInstanceContent content = jobInstanceService.queryJobTextByCode(instance.getCode());
                                    JobInstanceInfo instanceInfo = createJobParams(instance, content.getJobText(), content.getJobConfig());
                                    sparkJobSubmitService.submitJob(instanceInfo);
                                }
                            } catch (Throwable e) {
                                LOG.error("启动实例失败: " + e.getMessage(), e);
                            } finally {
                                latch.countDown();
                            }
                        });
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                });

                try {
                    latch.await();
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }

    private JobInstanceInfo createJobParams(JobInstance instance, String jobText, String jobConfig) {
        return JobInstanceInfo.builder()
                .setAccessKey("")
                .setClusterCode(instance.getClusterCode())
                .setJobType(instance.getJobType())
                .setInstanceType(instance.getInstanceType())
                .setInstanceCode(instance.getCode())
                .setJobName(instance.getName())
                .setJobText(jobText)
                .setJobConfig(jobConfig)
                .setYarnQueue(instance.getYarnQueue())
                .setScheduleTime(instance.getScheduleTime())
                .setOwner(instance.getOwner())
                .build();
    }

    /**
     * 检测实例是否能够启动
     * @param instance 检测实例
     */
    private boolean checkInstanceStart(JobInstance instance) {
        if (WAITING == instance.getStatus()) {
            Instant currentDate = Instant.now();
            //判断是否到达调度时间
            if (instance.getScheduleTime().isAfter(currentDate)) {
                return false;
            }

            //起始节点
            if (!ROOT_SCHEDULE_NODE.equals(instance.getDependentCode())) {
                long notSuccessCount = jobInstanceService.findParentInstanceNotSuccessCount(instance.getCode());
                // 上游节点没有全部运行成功，当前节点不能运行
                return notSuccessCount <= 0;
            }
            return true;
        } else if (FAILED == instance.getStatus()) {
            Integer retryCount = instance.getRetryCount();
            // 如果没有设置重试，当前节点不能运行
            if (retryCount == null || retryCount <= 0) {
                return false;
            }

            Duration interval = Duration.between(Instant.now(), instance.getScheduleTime());
            //作业失败10分钟后重试
            if (interval.get(ChronoUnit.SECONDS) > 600) {
                //增加重试次数
                instance.setRetryCount(instance.getRetryCount() + 1);
                jobInstanceService.insertEntity(instance);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

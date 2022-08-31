package io.github.melin.spark.jobserver.monitor.task;

import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.support.leader.LeaderTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.melin.spark.jobserver.core.enums.InstanceStatus.WAITING;

/**
 * 作业提交超时，修复为等待提交状态
 * huaixin 2022/3/19 3:38 PM
 */
@Service
public class CheckJobSubmitTimoutTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger("serverMinitor");

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    private JobInstanceService instanceService;

    @Override
    public void run() {
        if (!redisLeaderElection.checkLeader(LeaderTypeEnum.DRIVER_POOL_MONITOR)) {
            return;
        }

        try {
            int submitTimeOut = 60 * 10; // 60 秒
            Instant dateTime = Instant.now();
            dateTime = dateTime.minusSeconds(submitTimeOut);
            List<JobInstance> instances = instanceService.findJobInstanceSubmitTimeOut(dateTime);
            if (instances.size() > 0) {
                String codes = instances.stream().map(JobInstance::getCode).collect(Collectors.joining(","));
                LOG.info("[CheckJobSubmitTimout] start check Job: {}", codes);
                instances.forEach(instance -> instanceService.updateJobStatusByCode(instance.getCode(), WAITING));
            }
        } catch (Throwable e) {
            LOG.info(e.getMessage(), e);
        }
    }
}

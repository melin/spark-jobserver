package io.github.melin.spark.jobserver.scheduler;

import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.support.leader.LeaderTypeEnum;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import io.github.melin.spark.jobserver.util.DateUtils;
import io.github.melin.spark.jobserver.util.JobServerUtils;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.JobInstanceContent;
import io.github.melin.spark.jobserver.core.service.JobInstanceContentService;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import com.google.common.collect.Sets;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 清理过期实例
 */
@Service
public class CleanExpireInstanceScheduler implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(CleanExpireInstanceScheduler.class);

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    private JobInstanceService instanceService;

    @Autowired
    private JobInstanceContentService instanceContentService;

    @Autowired
    private ConfigProperties configProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisLeaderElection.buildLeader(LeaderTypeEnum.CLEAN_EXPIRE_INSTANCE);
    }

    @Scheduled(cron = "0 1 0 ? * *") //每天凌晨00:01分
    public void cleanInstance() {
        try {
            if (!redisLeaderElection.checkLeader(LeaderTypeEnum.CLEAN_EXPIRE_INSTANCE)) {
                return;
            }

            String logPath = configProperties.getInstanceLogPath();
            List<Long> list = queryExpireInstances();
            Set<String> scheduleDates = Sets.newHashSet();
            for (Long instanceId : list) {
                JobInstance instance = instanceService.getEntity(instanceId);
                if (instance != null) {
                    instanceService.deleteEntity(instance);

                    JobInstanceContent instanceContent = instanceContentService.queryJobInstanceContentByCode(instance.getCode());
                    if (instanceContent != null) {
                        instanceContentService.deleteEntity(instanceContent);
                    }

                    String scheduleDate = JobServerUtils.deleteLogFile(logPath, instance);
                    scheduleDates.add(scheduleDate);
                }
            }

            for (String scheduleDate : scheduleDates) {
                String logDir = logPath + "/" + scheduleDate;
                Path path = Paths.get(logDir);
                if (JobServerUtils.isEmptyDir(path)) {
                    Files.delete(path);
                    LOG.info("delete empty log dir: {}", logDir);
                }
            }

            LOG.info("clean expire instance count: {}", list.size());
        } catch (Exception e) {
            LOG.error("clean job log failure", e);
        }
    }

    private List<Long> queryExpireInstances() {
        int maxDays = configProperties.getMaxInstanceDays();
        LOG.info("max instance day: {}", maxDays);
        if (maxDays > 0) {
            Instant lastDay = Instant.now().minus(maxDays, ChronoUnit.DAYS);
            LOG.info("max instance day: {}, expire time: {}", maxDays, DateUtils.formateDateTime(lastDay));

            Criterion beforeScheduleTimeCrt = Restrictions.lt("scheduleTime", lastDay);
            return instanceService.findByCriterion(Projections.property("id"), beforeScheduleTimeCrt);
        } else {
            return Collections.emptyList();
        }
    }
}

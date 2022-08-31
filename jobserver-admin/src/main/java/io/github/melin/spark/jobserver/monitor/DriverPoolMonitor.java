package io.github.melin.spark.jobserver.monitor;

import io.github.melin.spark.jobserver.monitor.task.CheckJobSubmitTimoutTask;
import io.github.melin.spark.jobserver.monitor.task.CheckSparkDriverTask;
import io.github.melin.spark.jobserver.monitor.task.UpdateDriverResourceTask;
import io.github.melin.spark.jobserver.support.leader.RedisLeaderElection;
import com.gitee.melin.bee.util.ThreadUtils;
import io.github.melin.spark.jobserver.support.leader.LeaderTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * jobserver pool 大小控制
 *
 * @author melin 2021/9/19 10:19 下午
 */
@Component
public class DriverPoolMonitor implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger("serverMinitor");

    @Autowired
    private RedisLeaderElection redisLeaderElection;

    @Autowired
    private UpdateDriverResourceTask updateDriverResourceTask;

    @Autowired
    private CheckJobSubmitTimoutTask checkJobSubmitTimoutTask;

    @Autowired
    private CheckSparkDriverTask checkSparkDriverTask;

    private final ScheduledExecutorService jobServerCheckExecutorService =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("check-jobserver");

    private final ScheduledExecutorService checkJobSubmitTimoutExecutorService =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("check-jobserverWaiting-app");

    private final ScheduledExecutorService updateDriverResourceExecutorService =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("update-driver-resource");

    @Override
    public void afterPropertiesSet() throws Exception {
        redisLeaderElection.buildLeader(LeaderTypeEnum.DRIVER_POOL_MONITOR);

        jobServerCheckExecutorService.scheduleAtFixedRate(checkSparkDriverTask,
                1, 60, TimeUnit.SECONDS);

        //作业实例提交超时
        checkJobSubmitTimoutExecutorService.scheduleAtFixedRate(checkJobSubmitTimoutTask,
                1, 30, TimeUnit.MINUTES);

        updateDriverResourceExecutorService.scheduleAtFixedRate(updateDriverResourceTask,
                1, 3, TimeUnit.SECONDS);
    }
}

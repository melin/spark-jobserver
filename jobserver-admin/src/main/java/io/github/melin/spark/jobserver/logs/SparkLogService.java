package io.github.melin.spark.jobserver.logs;

import com.google.common.collect.Maps;
import io.github.melin.spark.jobserver.ConfigProperties;
import io.github.melin.spark.jobserver.deployment.dto.JobInstanceInfo;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.enums.ComputeType;
import io.github.melin.spark.jobserver.core.service.JobInstanceService;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import com.gitee.melin.bee.util.ThreadUtils;
import org.apache.hadoop.util.ShutdownHookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SparkLogService implements ApplicationContextAware, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkLogService.class);

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private JobInstanceService instanceService;

    @Autowired
    private ConfigProperties configProperties;

    /**
     * jobinstance对日志线程的映射
     */
    private final ConcurrentMap<String, Thread> logThreadMap = Maps.newConcurrentMap();

    private ApplicationContext applicationContext;

    private final ScheduledExecutorService scheduledExecutorService =
            ThreadUtils.newDaemonSingleThreadScheduledExecutor("load-log-thread");

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        driverService.clearCurrentLogServer();

        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                List<SparkDriver> list = driverService.queryEmptyLogServers();
                if (list.size() > 0) {
                    LOGGER.info("query log server count: {}", list.size());
                }
                list.forEach(driver -> {
                    boolean flag = driverService.lockCurrentLogServer(driver.getApplicationId());
                    if (flag) {
                        ComputeType scheduleType = driver.getComputeType();
                        String appId = driver.getApplicationId();
                        String sparkDriverUrl = driver.getSparkDriverUrl();
                        boolean shareDriver = driver.isShareDriver();

                        if (ComputeType.YARN_BATCH == scheduleType) {
                            JobInstance instance = instanceService.queryInstanceByAppId(appId);
                            if (instance != null) {
                                JobInstanceInfo instanceInfo = new JobInstanceInfo();
                                String instanceCode = instance.getCode();
                                instanceInfo.setClusterCode(instance.getClusterCode());
                                instanceInfo.setInstanceCode(instanceCode);
                                instanceInfo.setScheduleTime(instance.getScheduleTime());
                                instanceInfo.setInstanceType(instance.getInstanceType());
                                instanceInfo.setJobType(instance.getJobType());
                                instanceInfo.setOwner(instance.getOwner());

                                this.createSparkJobLog(instanceInfo, appId, shareDriver, sparkDriverUrl);
                                this.startJobLogThread(instanceCode);
                            }
                        } else {
                            throw new UnsupportedOperationException("compute type not supported");
                        }
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }, 3, 1, TimeUnit.SECONDS);

        ShutdownHookManager.get().addShutdownHook(() -> {
            LOGGER.info("clear current log server");
            driverService.clearCurrentLogServer();
        }, 2);
    }

    @Override
    public void destroy() {
        driverService.clearCurrentLogServer();
    }

    public void createSparkJobLog(JobInstanceInfo instanceInfo, String applicationId,
                                  boolean shareDriver, String sparkDriverUrl) {

        String instanceCode = instanceInfo.getInstanceCode();
        LogTaskDto logTaskDto = LogTaskDto.builder()
                .setScheduleTime(instanceInfo.getScheduleTime())
                .setApplicationId(applicationId)
                .setClusterCode(instanceInfo.getClusterCode())
                .setInstanceCode(instanceCode)
                .setInstanceType(instanceInfo.getInstanceType())
                .setJobType(instanceInfo.getJobType())
                .setShareDriver(shareDriver)
                .setSparkDriverUrl(sparkDriverUrl)
                .setOwner(instanceInfo.getOwner())
                .build();
        SparkTaskLogThread logThread = new SparkTaskLogThread(applicationContext, logTaskDto);
        logThread.setName("batch-" + applicationId);
        logThreadMap.put(instanceCode, logThread);
        LOGGER.info("create instance log thread: {}", instanceCode);
    }

    public boolean startJobLogThread(String instanceCode) {
        Thread jobLog = logThreadMap.get(instanceCode);
        if (jobLog != null) {
            jobLog.start();
            LOGGER.info("Start log thread, instanceCode: {}", instanceCode);
            return true;
        } else {
            LOGGER.warn("No log thread: {}", instanceCode);
            return false;
        }
    }

    public void removeLogThread(String instanceCode) {
        LOGGER.info("clear log thread, instanceCode: {}", instanceCode);
        logThreadMap.remove(instanceCode);
    }
}

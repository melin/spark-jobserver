package io.github.melin.spark.jobserver.driver.support;

import io.github.melin.spark.jobserver.core.enums.DriverStatus;
import io.github.melin.spark.jobserver.driver.SparkDriverContext;
import io.github.melin.spark.jobserver.driver.SparkDriverEnv;
import io.github.melin.spark.jobserver.driver.listener.MetricsData;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import com.gitee.melin.bee.util.ThreadUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author melin 2021/8/23 10:06 上午
 */
@Service
public class TaskLogThread implements InitializingBean, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskLogThread.class);

    @Autowired
    protected SparkDriverContext sparkDriverContext;

    private final AtomicBoolean queryLog = new AtomicBoolean(true);

    private int oneSecondCount = 0;

    private int twoSecondCount = 0;

    private int thirdSecondCount = 0;

    private String lastlog = "";

    private long lastTime = 0L;

    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorService logExecutor = ThreadUtils.newDaemonSingleThreadExecutor("LogThread");
        logExecutor.execute(this);
    }

    public void stopQueySparkStageLog() {
        processlog();
        queryLog.getAndSet(false);
    }

    public void startQueySparkStageLog() {
        lastlog = "";
        oneSecondCount = 0;
        twoSecondCount = 0;
        thirdSecondCount = 0;
        lastTime = System.currentTimeMillis();
        queryLog.getAndSet(true);
    }

    @Override
    public void run() {
        while (true) {
            if (queryLog.get()) {
                try {
                    SparkSession sparkSession = SparkDriverEnv.getSparkSession();
                    if (sparkSession != null) {
                        SparkConf conf = sparkSession.sparkContext().getConf();
                        boolean logEnabled = conf.getBoolean("spark.job.status.log.enable", true);
                        if (logEnabled) {
                            processlog();
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                } finally {
                    try {
                        if (oneSecondCount < 10) {
                            TimeUnit.SECONDS.sleep(1);
                            oneSecondCount += 1;
                        } else if (twoSecondCount < 20) {
                            TimeUnit.SECONDS.sleep(2);
                            twoSecondCount += 1;
                        } else if (thirdSecondCount < 50) {
                            TimeUnit.SECONDS.sleep(5);
                            thirdSecondCount += 1;
                        } else {
                            TimeUnit.SECONDS.sleep(10);
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            } else {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception ignored) {}
            }
        }
    }

    private void processlog() {
        if (sparkDriverContext.getStatus() == DriverStatus.RUNNING && queryLog.get()) {
            String log = MetricsData.getJobProgressLog();
            long time = System.currentTimeMillis();
            if (StringUtils.isNotBlank(log)) {
                if (!log.equalsIgnoreCase(lastlog) || (time - lastTime) >= 60 * 1000) {
                    LogUtils.info("spark status: " + printLog(log));
                    lastlog = log;
                    lastTime = time;
                }
            } else {
                if ((time - lastTime) >= 60 * 1000) {
                    LogUtils.info("Application Running...");
                    lastTime = time;
                }
            }
        }
    }

    private String printLog(String log) {
        if (StringUtils.isNotBlank(log) && log.length() > 120) {
            String[] temps = log.split(";");
            List<String> list = Lists.newArrayList();
            int length = 0;
            for (int index = 0; index < temps.length; index++) {
                String str = temps[temps.length - 1 - index];
                if (length + str.trim().length() < 120 || length == 0) {
                    list.add(str.trim());
                    length += str.trim().length();
                }
            }
            Collections.reverse(list);
            return StringUtils.join(list, "; ");
        } else {
            return log;
        }
    }
}

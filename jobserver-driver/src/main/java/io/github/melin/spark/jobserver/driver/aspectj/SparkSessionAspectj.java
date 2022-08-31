package io.github.melin.spark.jobserver.driver.aspectj;

import io.github.melin.spark.jobserver.core.service.JobInstanceContentService;
import io.github.melin.spark.jobserver.driver.util.LogUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SparkSessionAspectj implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SparkSessionAspectj.class);

    private static JobInstanceContentService jobInstanceContentService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        jobInstanceContentService = applicationContext.getBean(JobInstanceContentService.class);
    }

    @Around("execution(public * org.apache.spark.sql.SparkSession.sql(..)) && args(sql)")
    public Object sparkSessionSql(ProceedingJoinPoint pjp, String sql) throws Throwable {
        LogUtils.info("exec spark sql: {}", sql);
        return pjp.proceed();
    }
}

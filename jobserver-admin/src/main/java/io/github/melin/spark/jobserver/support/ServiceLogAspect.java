package io.github.melin.spark.jobserver.support;

import io.github.melin.spark.jobserver.util.DateUtils;
import com.gitee.melin.bee.util.MapperUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Map;

/**
 * Created by admin on 2020/3/22 下午5:07
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger LOG = LoggerFactory.getLogger("accessLog");

    @Pointcut("execution(public * io.github.melin.spark.jobserver.service.JobServerServiceImpl.*(..))")
    public void serviceAccessLog(){}

    @Around("serviceAccessLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Throwable ex = null;
        boolean success = true;
        Object value = null;
        try {
            value = joinPoint.proceed();
        } catch (Exception e) {
            ex = e;
            success = false;
        } finally {
            stopWatch.stop();
        }

        Map<String, Object> json = Maps.newHashMap();
        json.put("method", joinPoint.getSignature().toShortString());

        json.put("requetTime", DateUtils.getCurrentDateTime());
        json.put("execTime", stopWatch.getTotalTimeMillis());
        json.put("success", success);
        json.put("params", MapperUtils.toJSONString(joinPoint.getArgs()));
        if (!success) {
            String rootMsg = ExceptionUtils.getRootCauseMessage(ex);
            json.put("exception", rootMsg);
        }
        LOG.info(MapperUtils.toJSONString(json));

        if (!success) {
            throw ex;
        }
        return value;
    }
}

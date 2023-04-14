package io.github.melin.spark.jobserver.support.leader;

import com.gitee.melin.bee.util.NetUtils;
import io.github.melin.spark.jobserver.api.JobServerException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于redis 锁选举产生leader;
 */
@Service
public class RedisLeaderElection implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLeaderElection.class);

    private static final int LOCK_TIMEOUT = 1000 * (LeaderTypeEnum.values().length + 10); //ms

    private static final String SUBMIT_DRIVER_LOCK_KEY = "jobserver-submit-driver-lock";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private Environment environment;

    private final ConcurrentMap<LeaderTypeEnum, AtomicBoolean> leaders = Maps.newConcurrentMap();

    private String profiles;

    private String currentIp;

    @Override
    public void afterPropertiesSet() throws Exception {
        currentIp = NetUtils.getLocalHost();
        profiles = StringUtils.join(environment.getActiveProfiles(), "-");
        LOGGER.info("current ip: {}, profiles: {}", currentIp, profiles);
    }

    @Scheduled(fixedDelay = 1000)
    public void tryToAcquireLock() {
        leaders.forEach((leaderType, isLeader) -> {
            final String leaderKey = "RLE::" + profiles + "_" + leaderType.getRedisKey();
            try {
                redisTemplate.opsForValue().setIfAbsent(leaderKey, currentIp, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);

                String value = redisTemplate.opsForValue().get(leaderKey);
                if (StringUtils.equals(currentIp, value)) {
                    //避免过期
                    redisTemplate.opsForValue().setIfPresent(leaderKey, currentIp, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                    boolean result = isLeader.getAndSet(true);
                    if (!result) {
                        LOGGER.info("{}, 当前节点选择作为leader", leaderType.name());
                    }
                } else {
                    boolean result = isLeader.getAndSet(false);
                    if (result) {
                        LOGGER.warn("{}, 当前节点失去作为leader，新leader：{}", leaderType.name(), value);
                    }
                }

                TimeUnit.MILLISECONDS.sleep(1010);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        });
    }

    public void buildLeader(LeaderTypeEnum leaderType) {
        if (leaders.containsKey(leaderType)) {
            throw new JobServerException("leaderType exist");
        } else {
            leaders.putIfAbsent(leaderType, new AtomicBoolean(false));
        }
    }

    public boolean checkLeader(LeaderTypeEnum leaderType) {
        AtomicBoolean isLeader = leaders.get(leaderType);
        if (isLeader == null) {
            throw new JobServerException("leaderType not exist");
        }

        boolean leader = isLeader.get();

        if (leader) {
            String key = "RLE::" + profiles + "_" + leaderType.getRedisKey() + "_last_alive_time";
            long time = System.currentTimeMillis();
            redisTemplate.opsForValue().set(key, String.valueOf(time));
        }

        return leader;
    }

    public boolean trylock() {
        final String lockValue = UUID.randomUUID().toString();
        final String key = "RLE::" + profiles + "_" + SUBMIT_DRIVER_LOCK_KEY;
        redisTemplate.opsForValue().setIfAbsent(key, lockValue, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        String value = redisTemplate.opsForValue().get(key);
        return StringUtils.equals(lockValue, value);
    }

    public void deletelock() {
        final String key = "RLE::" + profiles + "_" + SUBMIT_DRIVER_LOCK_KEY;
        redisTemplate.delete(key);
    }
}

package io.github.melin.spark.jobserver.support.leader;

/**
 * Created by admin on 2020/6/9.
 */
public enum LeaderTypeEnum {
    DRIVER_POOL_MANAGER("leader_driver_pool_manager"),

    DRIVER_POOL_MONITOR("leader_driver_pool_monitor"),

    CLEAN_EXPIRE_INSTANCE("leader_clean_expire_instance");

    private String redisKey;

    private LeaderTypeEnum(String redisKey){
        this.redisKey = redisKey;
    }

    public String getRedisKey() {
        return redisKey;
    }
}

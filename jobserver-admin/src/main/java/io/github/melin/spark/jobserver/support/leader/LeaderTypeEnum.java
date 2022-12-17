package io.github.melin.spark.jobserver.support.leader;

public enum LeaderTypeEnum {
    DRIVER_POOL_MANAGER("spark_leader_driver_pool_manager"),

    DRIVER_POOL_MONITOR("spark_leader_driver_pool_monitor"),

    CLEAN_EXPIRE_INSTANCE("spark_leader_clean_expire_instance");

    private String redisKey;

    private LeaderTypeEnum(String redisKey){
        this.redisKey = redisKey;
    }

    public String getRedisKey() {
        return redisKey;
    }
}

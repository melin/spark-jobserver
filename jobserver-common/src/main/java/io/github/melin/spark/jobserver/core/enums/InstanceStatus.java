package io.github.melin.spark.jobserver.core.enums;

import com.gitee.melin.bee.core.enums.BaseStringEnum;

/**
 * huaixin 2022/3/28 5:05 PM
 */
public enum InstanceStatus implements BaseStringEnum {
    WAITING,
    SUBMITTING,
    LOCKED,
    RUNNING,
    FAILED,
    FINISHED,
    KILLED;

    @Override
    public String getValue() {
        return this.name();
    }
}

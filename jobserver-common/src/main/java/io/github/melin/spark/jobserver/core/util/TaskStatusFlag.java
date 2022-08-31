package io.github.melin.spark.jobserver.core.util;

/**
 * huaixin 2022/4/9 10:58 PM
 */
public enum TaskStatusFlag {
    TASK_END_FLAG,

    TASK_STOP_FLAG,

    STAGE_ERROR_FLAG,  //Stage 一直卡住不运行

    TASK_ERROR_FLAG;
}

package io.github.melin.spark.jobserver.core.util;

import io.github.melin.spark.jobserver.api.LogLevel;

/**
 * huaixin 2022/4/9 10:28 PM
 */
public class LogRecord {

    private LogLevel level;

    private TaskStatusFlag flag;

    private String message;

    public LogRecord() {
    }

    public LogRecord(LogLevel level, String message) {
        this.level = level;
        this.message = message;
    }

    public LogRecord(LogLevel level, TaskStatusFlag flag, String message) {
        this.level = level;
        this.flag = flag;
        this.message = message;
    }

    public static LogRecord of(LogLevel level, String message) {
        return new LogRecord(level, message);
    }

    public static LogRecord of(LogLevel level, TaskStatusFlag flag, String message) {
        return new LogRecord(level, flag, message);
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaskStatusFlag getFlag() {
        return flag;
    }

    public void setFlag(TaskStatusFlag flag) {
        this.flag = flag;
    }
}

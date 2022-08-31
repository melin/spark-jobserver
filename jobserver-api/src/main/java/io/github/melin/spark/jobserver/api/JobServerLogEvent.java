package io.github.melin.spark.jobserver.api;

import org.apache.spark.scheduler.SparkListenerEvent;

/**
 * @author melin 2021/8/18 9:12 上午
 */
public class JobServerLogEvent implements SparkListenerEvent {
    private final LogLevel logLevel;

    private final String message;

    public JobServerLogEvent(LogLevel logLevel, String message) {
        this.logLevel = logLevel;
        this.message = message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "JobServerLogEvent{" +
                "logLevel=" + logLevel +
                ", message='" + message + '\'' +
                '}';
    }
}

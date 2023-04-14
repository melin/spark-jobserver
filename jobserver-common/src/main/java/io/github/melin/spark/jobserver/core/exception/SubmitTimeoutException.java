package io.github.melin.spark.jobserver.core.exception;

/**
 * Job server 共享实例，操作最大运行次数
 * @author melin 2021/7/20 12:10 下午
 */
public class SubmitTimeoutException extends SparkJobException {

    public SubmitTimeoutException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SubmitTimeoutException(String message, Object... params) {
        super(message, params);
    }
}

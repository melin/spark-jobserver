package io.github.melin.spark.jobserver.api;

/**
 * Created by admin on 2019/7/25.
 */
public class SparkJobServerException extends RuntimeException {

    public SparkJobServerException(String message) {
        super(message);
    }

    public SparkJobServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

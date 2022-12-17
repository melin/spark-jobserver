package io.github.melin.spark.jobserver.api;

public class SparkJobServerException extends RuntimeException {

    public SparkJobServerException(String message) {
        super(message);
    }

    public SparkJobServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

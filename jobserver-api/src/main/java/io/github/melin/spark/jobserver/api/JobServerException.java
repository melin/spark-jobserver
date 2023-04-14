package io.github.melin.spark.jobserver.api;

import org.slf4j.helpers.MessageFormatter;

public class JobServerException extends RuntimeException {

    private String detailMessage;

    private Throwable cause = this;

    public JobServerException() {
        super();
    }

    public JobServerException(String message, Object... params) {
        fillInStackTrace();
        detailMessage = formatMessage(message, params);
    }

    public JobServerException(String message, Throwable cause) {
        fillInStackTrace();
        detailMessage = message;
        this.cause = cause;
    }

    public JobServerException(Throwable cause, String message, Object... params) {
        fillInStackTrace();
        detailMessage = formatMessage(message, params);
        this.cause = cause;
    }

    public JobServerException(Throwable cause) {
        fillInStackTrace();
        detailMessage = (cause==null ? null : cause.toString());
        this.cause = cause;
    }

    @Override
    public synchronized Throwable getCause() {
        return (cause == this ? null : cause);
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }

    private String formatMessage(String message, Object... params) {
        if (params.length > 0) {
            String format = message;
            message = MessageFormatter.arrayFormat(format, params).getMessage();
        }

        return message;
    }
}

package io.github.melin.spark.jobserver.core.exception;

import com.gitee.melin.bee.core.support.BeeRuntimeException;

public class SparkJobException extends BeeRuntimeException {
    public SparkJobException(Throwable cause, String message, Object... params) {
        super(cause, message, params);
    }

    public SparkJobException(String message, Object... params) {
        super(message, params);
    }
}

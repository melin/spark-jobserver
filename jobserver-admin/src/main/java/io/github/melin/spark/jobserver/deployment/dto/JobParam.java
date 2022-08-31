package io.github.melin.spark.jobserver.deployment.dto;

import com.google.common.base.Objects;

/**
 * huaixin 2022/4/3 2:48 PM
 */
public class JobParam {
    private String key;

    private String value;

    /**
     * 是否有效参数
     */
    private boolean valid;

    public static final JobParam INSTANCE = new JobParam();

    private JobParam() {
    }

    public JobParam(String key, String value, boolean valid) {
        this.key = key;
        this.value = value;
        this.valid = valid;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isValid() {
        return valid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobParam jobParam = (JobParam) o;
        return Objects.equal(key, jobParam.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }
}

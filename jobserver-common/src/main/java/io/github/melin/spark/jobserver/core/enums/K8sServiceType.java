package io.github.melin.spark.jobserver.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gitee.melin.bee.core.enums.BaseStringEnum;
import com.gitee.melin.bee.core.enums.jackson.JacksonEnumStringSerializer;

/**
 * 函数类型: NodePort、Ingress
 */
@JsonSerialize(using = JacksonEnumStringSerializer.class)
public enum K8sServiceType implements BaseStringEnum {
    NODE_PORT("NodePort"),
    INGRESS_NGINX("IngressNginx"),
    TRAEFIK("Traefik");

    String code;

    K8sServiceType(String code) {
        this.code = code;
    }

    @JsonValue
    @Override
    public String getValue() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}

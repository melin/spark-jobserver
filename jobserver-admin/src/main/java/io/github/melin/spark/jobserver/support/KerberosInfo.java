package io.github.melin.spark.jobserver.support;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(builderClassName = "Builder")
public class KerberosInfo {
    private boolean enabled;

    private boolean tempKerberos;

    private String principal;

    private String keytabFile;

    private String krb5File;
}

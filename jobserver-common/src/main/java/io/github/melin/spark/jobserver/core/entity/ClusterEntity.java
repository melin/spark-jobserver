package io.github.melin.spark.jobserver.core.entity;

import static io.github.melin.spark.jobserver.core.enums.DataCatalogType.*;

import com.gitee.melin.bee.core.hibernate5.type.BooleanToIntConverter;
import com.gitee.melin.bee.core.model.BaseEntity;
import io.github.melin.spark.jobserver.core.enums.CloudType;
import io.github.melin.spark.jobserver.core.enums.ClusterType;
import io.github.melin.spark.jobserver.core.enums.DataCatalogType;
import io.github.melin.spark.jobserver.core.enums.K8sScheduler;
import io.github.melin.spark.jobserver.core.enums.K8sServiceType;
import io.github.melin.spark.jobserver.core.enums.StorageType;
import io.github.melin.spark.jobserver.core.util.ClusterId;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

// @Entity
@Table(name = "meta_cluster")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClusterEntity extends BaseEntity {

    @Column(name = "region_code")
    private String regionCode;

    private String name;

    private String code;

    @Column(name = "cluster_type")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.ClusterType")
            })
    private ClusterType clusterType;

    @Column(name = "cloud_type")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.CloudType")
            })
    private CloudType cloudType;

    @Column(name = "storage_type")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.StorageType")
            })
    private StorageType storageType;

    @Column(name = "hive_server2_url")
    private String hiveServer2Url;

    @Column(name = "kerberos_enabled")
    @Convert(converter = BooleanToIntConverter.class)
    private boolean kerberosEnabled = false;

    @Column(name = "kerberos_code")
    private String kerberosCode;

    @Column(name = "hive_principal")
    private String hivePrincipal;

    @Column(name = "core_config")
    private String coreConfig;

    @Column(name = "hdfs_config")
    private String hdfsConfig;

    @Column(name = "hive_config")
    private String hiveConfig;

    @Column(name = "yarn_config")
    private String yarnConfig;

    @Column(name = "mapred_config")
    private String mapredConfig;

    @Column(name = "k8s_service_type")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.K8sServiceType")
            })
    private K8sServiceType k8sServiceType;

    @Column(name = "k8s_config")
    private String k8sConfig;

    @Column(name = "k8s_scheduler")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.K8sScheduler")
            })
    private K8sScheduler k8sScheduler;

    // k8s namespace
    private String namespace;

    // k8s API 服务器终端节点, 从kubeconfig 解析获取
    private String endpoint;

    // 数据湖 rest Endpoint
    @Column(name = "datalake_endpoint")
    private String datalakeEndpoint;

    // 注册私有镜像
    @Column(name = "image_secret_enabled")
    @Convert(converter = BooleanToIntConverter.class)
    private boolean imageSecretEnabled = false;

    // Docker 容器服务地址
    @Column(name = "docker_address")
    private String dockerAddress;

    // Docker 容器服务认证用户名
    @Column(name = "docker_username")
    private String dockerUsername;

    // Docker 容器服务认证密码
    @Column(name = "docker_password")
    private String dockerPassword;

    @Column(name = "k8s_service_account")
    private String k8sServiceAccount;

    @Column(name = "ingress_url")
    private String ingressUrl;

    @Column(name = "ingress_password")
    private String ingressPassword;

    @Column(name = "hadoop_user")
    private String hadoopUser;

    @Column(name = "data_catalog_type")
    @Type(
            type = "com.gitee.melin.bee.core.enums.StringValuedEnumType",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "enumClass",
                        value = "io.github.melin.spark.jobserver.core.enums.DataCatalogType")
            })
    private DataCatalogType dataCatalogType;

    @Column(name = "hms_catalog_name")
    private String hmsCatalogName;

    @Column(name = "jdbc_url")
    private String jdbcUrl;

    @Column(name = "jdbc_user")
    private String jdbcUser;

    @Column(name = "jdbc_password")
    private String jdbcPassword;

    @Convert(converter = BooleanToIntConverter.class)
    private Boolean status;

    @Column(name = "failure_reason")
    private String failureReason;

    public boolean supportHmsCatalog() {
        return HIVE == dataCatalogType || GLUE == dataCatalogType || DLF == dataCatalogType;
    }

    public ClusterId getClusterId() {
        return ClusterId.of(regionCode, clusterType, code);
    }
}

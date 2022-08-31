package io.github.melin.spark.jobserver;

import lombok.Data;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * huaixin 2022/4/3 2:36 PM
 */
@Data
@Configuration
@Primary
@ConfigurationProperties(prefix = "jobserver")
public class ConfigProperties {

    /**
     * aspectj jar version
     */
    private String aspectjVersion = "1.9.9.1";

    /**
     * jobserver 相关配置文件，包含spark-default.conf, core-site.xml，hdfs-site.xml, yarn-site.xml 等配置文件
     */
    private String jobserverConfDir = SystemUtils.getUserHome() + "/spark-jobserver-config";

    /**
     * driver 启动超时时间 6m
     */
    private int driverSubmitTimeOutSeconds = 1000 * 60;

    /**
     * 实例运行日志存放路径
     */
    private String instanceLogPath = SystemUtils.getUserHome() + "/instanceLogs";

    /**
     * 提交driver 服务器本地最小可用内存(mb)
     */
    private int localMinMemoryMb = 2048;

    /**
     * 实例最大保留天数，小于1, 不清理
     */
    private int maxInstanceDays = 30;

}

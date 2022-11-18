package io.github.melin.spark.jobserver.driver;

import io.github.melin.spark.jobserver.driver.model.DriverParam;
import io.github.melin.spark.jobserver.driver.support.ConfigClient;
import com.beust.jcommander.JCommander;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * huaixin 2022/4/6 7:05 PM
 */
@ImportResource(locations = {"classpath*:*-context.xml"})
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, GsonAutoConfiguration.class})
@EnableScheduling
@EnableTransactionManagement
public class SparkDriverServer extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(SparkDriverServer.class);

    public static void main(String[] args) throws Exception {
        DriverParam driverParam = new DriverParam();
        LOG.info("args: {}", StringUtils.join(args, ","));
        JCommander.newBuilder().addObject(driverParam).build().parse(args);

        Long driverId = driverParam.getDriverId();
        byte[] asBytes = Base64.getDecoder().decode(driverParam.getConfig());
        String configText = new String(asBytes, StandardCharsets.UTF_8);

        ApplicationContext applicationContext = SpringApplication.run(SparkDriverServer.class);
        SparkDriverContext sparkDriverContext = applicationContext.getBean(SparkDriverContext.class);

        boolean kerberosEnabled = driverParam.isKerberosEnabled();
        boolean hiveEnabled = driverParam.isHiveEnable();
        SparkConf conf = new SparkConf(true);
        ConfigClient.init(configText, conf);
        SparkDriverEnv.init(conf, kerberosEnabled, hiveEnabled);

        sparkDriverContext.initSparkDriver(driverId);

        SparkDriverEnv.waitDriver();
        LOG.info("application finished");
    }
}

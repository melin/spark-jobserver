package io.github.melin.spark.jobserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * huaixin 2022/3/29 6:44 PM
 */
@ImportResource(locations = {"classpath*:jobserver-context.xml"})
@EnableScheduling
@EnableAspectJAutoProxy
@ServletComponentScan
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class SparkJobServerMain extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(SparkJobServerMain.class, args);
    }
}

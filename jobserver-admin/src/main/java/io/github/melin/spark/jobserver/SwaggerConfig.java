package io.github.melin.spark.jobserver;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JobServer 系统API")
                        .version("1.0")
                        .description("JobServer 系统API")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/melin/spark-jobserver")));
    }
}

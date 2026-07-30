package io.github.melin.spark.jobserver.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));

        httpSecurity
                .csrf(configurer -> configurer.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/error",
                                "/metrics",
                                "/jobserver.ico",
                                "/ok",
                                "/profile",
                                "/gitInfo",
                                "/doc/**",
                                "/driver/**",
                                "/v3/api-docs",
                                "/swagger-ui.html",
                                "/doc.html")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        return httpSecurity.build();
    }
}

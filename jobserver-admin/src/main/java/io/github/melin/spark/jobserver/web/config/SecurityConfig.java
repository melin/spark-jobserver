package io.github.melin.spark.jobserver.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/img/**", "/error", "/metrics", "/jobserver.ico",
                        "/ok", "/profile", "/gitInfo", "/doc/**", "/driver/**").permitAll()
                .anyRequest()
                .authenticated();

        return httpSecurity.build();
    }
}

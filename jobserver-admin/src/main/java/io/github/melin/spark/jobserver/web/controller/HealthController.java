package io.github.melin.spark.jobserver.web.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 健康检测
 *
 */
@Controller
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;

    private volatile long lastCheckDbTime = 0;

    @RequestMapping(value = "/ok", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String ok(HttpServletResponse response) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckDbTime > 1000 * 5) {
            lastCheckDbTime = currentTime;
            try {
                jdbcTemplate.queryForList("SELECT 1 FROM DUAL", Integer.class);
            } catch (Exception e) {
                LOGGER.error("检测数据失败：" + e.getMessage());
                response.setStatus(500);

                return "[error]数据库检测失败：" + e.getMessage();
            }
        }

        return "ok";
    }

    @RequestMapping(value = "/profile", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String profile() {

        if (ArrayUtils.contains(environment.getActiveProfiles(), "pro")) {
            return "production";
        } else if (ArrayUtils.contains(environment.getActiveProfiles(), "test")) {
            return "test";
        } else {
            return "dev";
        }
    }

    @RequestMapping(value = "/gitInfo", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String gitInfo() throws IOException {
        ClassPathResource hadoopResource = new ClassPathResource("git.properties");
        return IOUtils.toString(hadoopResource.getInputStream(), Charset.defaultCharset());
    }

}

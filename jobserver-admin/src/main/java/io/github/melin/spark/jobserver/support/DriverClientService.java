package io.github.melin.spark.jobserver.support;

import io.github.melin.spark.jobserver.core.entity.SparkDriver;
import io.github.melin.spark.jobserver.core.service.SparkDriverService;
import io.github.melin.spark.jobserver.core.util.LogRecord;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * huaixin 2022/4/14 20:35
 */
@Service
public class DriverClientService {

    private static final Logger LOG = LoggerFactory.getLogger(DriverClientService.class);

    @Autowired
    private SparkDriverService driverService;

    @Autowired
    private YarnClientService yarnClientService;

    @Autowired
    private RestTemplate restTemplate;

    public List<LogRecord> getServerLog(String sparkDriverUrl, String instanceCode) {
        String uri = sparkDriverUrl + "/sparkDriver/getServerLog?instanceCode=" + instanceCode;
        try {
            ResponseEntity<List<LogRecord>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LogRecord>>() {});
            if (response.hasBody()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.error("get task {} log error: {}", instanceCode, e.getMessage());
            return null;
        }
    }

    public Boolean isSparkJobRunning(String sparkDriverUrl, String instanceCode, String applicationId) {
        String url = sparkDriverUrl + "/sparkDriver/isJobRunning?instanceCode=" + instanceCode;
        try {
            return restTemplate.postForObject(url, null, Boolean.class);
        } catch (Exception e) {
            SparkDriver driver = driverService.queryDriverByAppId(applicationId);
            if (driver != null) {
                YarnApplicationState state = yarnClientService.getApplicationStatus(driver.getClusterCode(), applicationId);

                String newSparkDriverUrl = driver.getSparkDriverUrl();
                if (!newSparkDriverUrl.equals(sparkDriverUrl)) {
                    LOG.info("driver 重启，切换到新的节点：{}: {}", driver.getServerIp(), driver.getServerPort());
                }

                return YarnApplicationState.RUNNING == state;
            }
            return false;
        }
    }
}

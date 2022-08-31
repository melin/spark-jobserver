package io.github.melin.spark.jobserver.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * huaixin 2022/3/24 11:11 AM
 */
@Service
public class ServerPortService {
    private static final Logger LOG = LoggerFactory.getLogger(ServerPortService.class);

    private int port;

    public int getPort() {
        return port;
    }

    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        LOG.info("driver spring server port: {}", event.getWebServer().getPort());
        port = event.getWebServer().getPort();
    }
}

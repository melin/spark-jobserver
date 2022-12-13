package io.github.melin.spark.jobserver.driver.aspectj;

import io.github.melin.spark.jobserver.core.entity.DataConnector;
import io.github.melin.spark.jobserver.core.service.DataConnectorService;
import io.github.melin.spark.jobserver.driver.util.CatalogUtils;
import org.apache.spark.sql.connector.catalog.CatalogNotFoundException;
import org.apache.spark.sql.connector.catalog.CatalogPlugin;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static io.github.melin.spark.jobserver.driver.util.CatalogUtils.SESSION_CATALOG_NAME;

@Aspect
@Component
public class SparkCatalogAspectj implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SparkCatalogAspectj.class);

    private final HashMap<String, CatalogPlugin> catalogs = new HashMap<>();

    private static DataConnectorService connectorService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        connectorService = applicationContext.getBean(DataConnectorService.class);
    }

    @Around("execution(public * org.apache.spark.sql.connector.catalog.CatalogManager.catalog(..)) && args(catalogName)")
    public Object aroundCatalog(ProceedingJoinPoint pjp, String catalogName) throws Throwable {
        synchronized (SparkCatalogAspectj.class) {
            if (catalogName.equalsIgnoreCase(SESSION_CATALOG_NAME)) {
                return pjp.proceed();
            }

            CatalogPlugin catalog = catalogs.get(catalogName);
            if (catalog != null) {
                return catalog;
            }

            DataConnector dataConnector = connectorService.queryDataConnector(catalogName);

            if (dataConnector != null) {
                LOG.info("init catalog {}", catalogName);

                catalog = CatalogUtils.buildJdbcCatalogPlugin(dataConnector, catalogName);
                catalogs.put(catalogName, catalog);
                return catalog;
            } else {
                throw new CatalogNotFoundException("catalog not exists: " + catalogName);
            }
        }
    }
}

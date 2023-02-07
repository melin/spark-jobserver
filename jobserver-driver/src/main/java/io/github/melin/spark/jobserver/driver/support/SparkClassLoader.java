package io.github.melin.spark.jobserver.driver.support;

import io.github.melin.spark.jobserver.driver.util.DriverUtils;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 优先加载从SparkClassLoader 加载className
 */
public class SparkClassLoader extends URLClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkClassLoader.class);

    static {
        try {
            DriverUtils.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public SparkClassLoader(URL[] urls, ClassLoader parent){
        super(urls, parent);
    }

    public void addJar(String jar) throws Exception{
        this.addURL(new URL(jar));
    }
}

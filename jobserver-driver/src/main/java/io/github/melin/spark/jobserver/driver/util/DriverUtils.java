package io.github.melin.spark.jobserver.driver.util;

import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparkproject.guava.hash.HashCodes;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author melin 2021/8/20 2:15 下午
 */
public class DriverUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    public static final String CURRENT_PATH = new File("").getAbsolutePath();

    public static void setURLStreamHandlerFactory(URLStreamHandlerFactory factory) throws Exception {
        try {
            URL.setURLStreamHandlerFactory(factory);
        } catch (Error e) {
            final Field[] fields = URL.class.getDeclaredFields();
            int index = 0;
            Field factoryField = null;
            while (factoryField == null && index < fields.length) {
                final Field current = fields[index];
                if (Modifier.isStatic(current.getModifiers()) &&
                        current.getType().equals(URLStreamHandlerFactory.class)){
                    factoryField = current;
                    factoryField.setAccessible(true);
                } else {
                    index++;
                }
            }

            if (factoryField == null) {
                throw new Exception("Unable to detect static field in the URL class for the URLStreamHandlerFactory");
            }

            try {
                URLStreamHandlerFactory oldFactory = (URLStreamHandlerFactory) factoryField.get(null);
                if (factory instanceof ParentAwareURLStreamHandlerFactory) {
                    ((ParentAwareURLStreamHandlerFactory) factory).setParentFactory(oldFactory);
                }
                factoryField.set(null, factory);
            } catch (Exception e1) {
                throw new Exception("Unable to set url stream handler factory " + factory);
            }
        }
    }

    public static String createSecret() {
        SecureRandom rnd = new SecureRandom();
        byte[] secretBytes = new byte[256 / Byte.SIZE];
        rnd.nextBytes(secretBytes);
        return HashCodes.fromBytes(secretBytes).toString();
    }

    public static List<Object[]> rowsToJava(List<Row> rows) {
        return rows.stream().map(DriverUtils::toJava).collect(Collectors.toList());
    }

    private static Object[] toJava(Row row) {
        return IntStream.range(0, row.size())
                .mapToObj(pos -> {
                    if (row.isNullAt(pos)) {
                        return null;
                    }

                    Object value = row.get(pos);
                    if (value instanceof Row) {
                        return toJava((Row) value);
                    } else if (value instanceof scala.collection.Seq) {
                        return row.getList(pos);
                    } else if (value instanceof scala.collection.Map) {
                        return row.getJavaMap(pos);
                    } else {
                        return value;
                    }
                })
                .toArray(Object[]::new);
    }

    private abstract static class ParentAwareURLStreamHandlerFactory implements URLStreamHandlerFactory{

        protected URLStreamHandlerFactory parentFactory;

        public void setParentFactory(URLStreamHandlerFactory factory){
            this.parentFactory = factory;
        }

        public URLStreamHandlerFactory getParent(){
            return parentFactory;
        }

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            URLStreamHandler handler = this.create(protocol);
            if (handler == null && this.parentFactory != null) {
                handler = this.parentFactory.createURLStreamHandler(protocol);
            }
            return handler;
        }

        protected abstract URLStreamHandler create(String protocol);
    }

}

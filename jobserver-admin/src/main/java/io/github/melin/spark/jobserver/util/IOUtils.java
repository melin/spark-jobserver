package io.github.melin.spark.jobserver.util;

public class IOUtils {

    public static void closeQuietly(AutoCloseable... closeables) {
        if (closeables == null) {
            return;
        }
        for (final AutoCloseable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

    public static void closeQuietly(final AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception e) {}
        }
    }
}

package io.github.melin.spark.jobserver.util;

import io.github.melin.spark.jobserver.api.SparkJobServerException;
import io.github.melin.spark.jobserver.core.entity.JobInstance;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * huaixin 2022/4/2 5:38 PM
 */
public class JobServerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JobServerUtils.class);

    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        if (list == null) {
            throw new NullPointerException("List must not be null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        return new Partition<>(list, size);
    }

    private static class Partition<T> extends AbstractList<List<T>> {
        private final List<T> list;

        private final int size;

        private Partition(final List<T> list, final int size) {
            this.list = list;
            this.size = size;
        }

        @Override
        public List<T> get(final int index) {
            final int listSize = size();
            if (index < 0) {
                throw new IndexOutOfBoundsException("Index " + index + " must not be negative");
            }
            if (index >= listSize) {
                throw new IndexOutOfBoundsException("Index " + index + " must be less than size " +
                        listSize);
            }
            final int start = index * size;
            final int end = Math.min(start + size, list.size());
            return list.subList(start, end);
        }

        @Override
        public int size() {
            return (list.size() + size - 1) / size;
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

    public static String deleteLogFile(String instanceLogPath, JobInstance instance) {
        String scheduleDate = DateUtils.formateDate(instance.getScheduleTime());
        String instanceCode = instance.getCode();
        String logFile = instanceLogPath + "/" + scheduleDate + "/" + instanceCode + ".log";
        File file = new File(logFile);
        FileUtils.deleteQuietly(file);
        return scheduleDate;
    }

    public static void validateJobConfig(String jobConfig) {
        if (StringUtils.isBlank(jobConfig)) {
            return;
        }

        try {
            Properties properties = new Properties();
            properties.load(new StringReader(jobConfig));
            properties.keySet().forEach(key -> {
                String propKey = (String) key;
                if (!StringUtils.startsWith(propKey, "spark.")) {
                    throw new IllegalArgumentException("只支持spark 参数设置，参数前缀为: spark.");
                }
            });
        } catch (Exception e) {
            LOG.error("parse job config failure", e);
            throw new SparkJobServerException("parse job config failure: " + e.getMessage());
        }
    }

    /**
     * 读取日志文件最后 2000 行
     */
    public static LinkedList<String> readLogFile(File file) throws IOException {
        try (ReversedLinesFileReader fileReader = new ReversedLinesFileReader(file, Charset.defaultCharset())) {
            LinkedList<String> lines = Lists.newLinkedList();
            int counter = 0;

            String line = fileReader.readLine();
            while (counter < 2000 && line != null) {
                lines.addFirst(line);
                line = fileReader.readLine();
                counter++;
            }
            return lines;
        }
    }

    public static boolean isEmptyDir(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            }
        }

        return false;
    }

    public static String appName(String profiles) {
        return "spark-jobserver[" + profiles + "][share]";
    }

    public static String appNamePrefix(String profiles) {
        return "spark-jobserver[" + profiles + "]";
    }
}

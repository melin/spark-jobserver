package io.github.melin.spark.jobserver.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FSUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FSUtils.class);

    public static boolean exists(Configuration conf, String path) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        return fs.exists(new Path(path));
    }

    public static void checkHdfsPathExist(Configuration conf, String path) throws Exception {
        FileSystem fileSystem = FileSystem.get(conf);
        boolean exists = fileSystem.exists(new Path(path));
        if (!exists) {
            throw new IllegalAccessException("hdfs path not exist: " + path);
        }
    }

    public static void mkdir(Configuration conf, String dir) throws IOException {
        FileSystem fs = FileSystem.get(conf);

        Path path = new Path(dir);
        if (!fs.exists(path)) {
            fs.mkdirs(path);
            LOG.info("mkdir {}", dir);
        }
    }
}

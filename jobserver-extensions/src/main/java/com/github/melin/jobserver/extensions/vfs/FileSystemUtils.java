package com.github.melin.jobserver.extensions.vfs;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class FileSystemUtils {

    public static String buildUri(Configuration conf, Path path) {
        String filePath = path.toString();
        filePath = StringUtils.substringAfter(filePath, "vfs://");
        filePath = StringUtils.replace(filePath, ":/", "://");
        if (StringUtils.contains(filePath, "?")) {
            filePath = StringUtils.substringBefore(filePath, "?");
        }
        return filePath;
    }
}

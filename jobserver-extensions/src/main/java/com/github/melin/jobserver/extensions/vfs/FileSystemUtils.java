package com.github.melin.jobserver.extensions.vfs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.github.vfss3.S3FileSystemConfigBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.File;

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

    public static FileSystemOptions buildFileSystemOptions(Configuration conf, Path path) {
        String pathStr = path.toString();
        FileSystemOptions options = new FileSystemOptions();
        if (StringUtils.contains(pathStr, "ftp:/")) {
            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);
        } else if (StringUtils.contains(pathStr, "sftp:/")) {
            SftpFileSystemConfigBuilder.getInstance().setIdentities(options, new File[0]);
        } else if (StringUtils.contains(pathStr, "s3:/")) {
            S3FileSystemConfigBuilder.getInstance().setUseHttps(options, false);

            String userInfo = StringUtils.substringBetween(pathStr, "s3:/", "@");
            String[] items = StringUtils.split(userInfo, ":");
            BasicAWSCredentials credentials = new BasicAWSCredentials(items[0], items[1]);
            AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            S3FileSystemConfigBuilder.getInstance().setCredentialsProvider(options, credentialsProvider);

            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setMaxErrorRetry(3);
            S3FileSystemConfigBuilder.getInstance().setClientConfiguration(options, clientConfiguration);
        } else {
            throw new RuntimeException("not support, path: " + pathStr);
        }

        return options;
    }
}

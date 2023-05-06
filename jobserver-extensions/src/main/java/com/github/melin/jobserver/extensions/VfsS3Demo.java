package com.github.melin.jobserver.extensions;

import com.amazonaws.ClientConfiguration;
import com.github.vfss3.S3FileSystemConfigBuilder;
import org.apache.commons.vfs2.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VfsS3Demo {
    public static void main(String[] args) throws IOException {
        FileSystemManager fsManager = VFS.getManager();

        FileSystemOptions options = new FileSystemOptions();
        S3FileSystemConfigBuilder.getInstance().setUseHttps(options, false);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(3);
        S3FileSystemConfigBuilder.getInstance().setClientConfiguration(options, clientConfiguration);

        FileObject fileObject = fsManager.resolveFile("s3://BxiljVd5YZa3mRUn:3Mq9dsmdMbN1JipE1TlOF7OuDkuYBYpe@cdh1:9300/demo-bucket/demo.csv", options);
        System.out.println(fileObject.getName().getPath());
        System.out.println(fileObject.getContent().getString(StandardCharsets.UTF_8));

        //fsManager.closeFileSystem(fileObject.getFileSystem());

        FileObject fileObject1 = fsManager.resolveFile("tgz:s3://BxiljVd5YZa3mRUn:3Mq9dsmdMbN1JipE1TlOF7OuDkuYBYpe@cdh1:9300/demo-bucket/csv.tar.gz!/csv", options);
        for (FileObject fo : fileObject1.getChildren()) {
            System.out.println(fo.getName());

            System.out.println(fo.getContent().getString(StandardCharsets.UTF_8));
        }

        fsManager.closeFileSystem(fileObject.getFileSystem());
    }

}

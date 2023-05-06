package com.github.melin.jobserver.extensions;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VfsSftpDemo {
    public static void main(String[] args) throws IOException {
        FileSystemManager fsManager = VFS.getManager();

        FileSystemOptions options = new FileSystemOptions();
        //SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(options, "no");
        SftpFileSystemConfigBuilder.getInstance().setIdentities(options, new File[0]);

        //SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, true);

        FileObject fileObject1 = fsManager.resolveFile("tgz:sftp:///test:test2023@172.18.5.46:22/ftpdata/csv.tar.gz!/csv", options);
        for (FileObject fo : fileObject1.getChildren()) {
            System.out.println(fo.getName());
            System.out.println(fo.getContent().getString(StandardCharsets.UTF_8));
        }
    }
}

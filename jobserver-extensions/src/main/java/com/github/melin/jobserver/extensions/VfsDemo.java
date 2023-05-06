package com.github.melin.jobserver.extensions;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

public class VfsDemo {
    public static void main(String[] args) throws IOException {
        FileSystemManager fsManager = VFS.getManager();

        FileSystemOptions options1 = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options1, true);
        FileObject fileObject1 = fsManager.resolveFile("ftp://fcftp:fcftp@172.18.1.52/", options1);
        for (FileObject fo : fileObject1.getChildren()) {
            System.out.println(fo.getName());
        }

        FileObject fileObject2 = fsManager.resolveFile("ftp://fcftp:fcftp@172.18.1.52/demo.csv", options1);
        System.out.println(fileObject2.getContent().getString(Charset.defaultCharset()));

        FileObject fileObject3 = fsManager.resolveFile("zip:/Users/melin/Documents/codes/csv.zip!/csv");
        for (FileObject fo : fileObject3.getChildren()) {
            System.out.println(fo.getName());
        }

        FileObject fileObject4 = fsManager.resolveFile("tgz:/Users/melin/Documents/codes/csv.tar.gz!/csv");
        for (FileObject fo : fileObject4.getChildren()) {
            System.out.println(fo.getName());
        }

        FileObject fileObject5 = fsManager.resolveFile("tgz:ftp://fcftp:fcftp@172.18.1.52/csv.tar.gz!/csv", options1);
        for (FileObject fo : fileObject5.getChildren()) {
            System.out.println(fo.getName());
        }

        fsManager.close();
    }

    public static FileSystemOptions createDefaultOptions()
            throws FileSystemException {
        FileSystemOptions opts = new FileSystemOptions();

        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(
                opts, "no");

        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

        return opts;
    }
}

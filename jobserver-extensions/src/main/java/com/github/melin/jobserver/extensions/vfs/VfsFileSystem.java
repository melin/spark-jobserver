package com.github.melin.jobserver.extensions.vfs;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.util.Progressable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class VfsFileSystem extends FileSystem {

    public static final int DEFAULT_BLOCK_SIZE = 4 * 1024;

    @Override
    public String getScheme() {
        return "vfs";
    }

    private URI uri;

    /**
     * Set configuration from UI.
     *
     * @param uriInfo
     * @param conf
     * @throws IOException
     */
    private void setConfigurationFromURI(URI uriInfo, Configuration conf)
            throws IOException {
    }

    @Override
    public void initialize(URI uriInfo, Configuration conf) throws IOException {
        super.initialize(uriInfo, conf);

        setConfigurationFromURI(uriInfo, conf);
        setConf(conf);
        this.uri = uriInfo;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public FSDataInputStream open(Path path, int i) throws IOException {
        FileSystemManager fsManager = VFS.getManager();
        FileSystemOptions options = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);

        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);
        if (FileType.FOLDER == fileObject.getType()) {
            throw new FileNotFoundException("Path " + file + " is a directory.");
        }

        return new FSDataInputStream(
                new VfsInputStream(fileObject.getContent(), fileObject.getName().getPath(), statistics));
    }

    @Override
    public FSDataOutputStream create(Path path, FsPermission fsPermission, boolean b, int i, short i1, long l, Progressable progressable) throws IOException {
        return null;
    }

    @Override
    public FSDataOutputStream append(Path path, int i, Progressable progressable) throws IOException {
        return null;
    }

    @Override
    public boolean rename(Path path, Path path1) throws IOException {
        return false;
    }

    @Override
    public boolean delete(Path path, boolean b) throws IOException {
        return false;
    }

    @Override
    public FileStatus[] listStatus(Path path) throws FileNotFoundException, IOException {
        FileSystemManager fsManager = VFS.getManager();
        FileSystemOptions options = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);

        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);
        if (FileType.FILE == fileObject.getType()) {
            return new FileStatus[]{getFileStatus(fileObject, path)};
        }

        FileObject[] fos = fileObject.getChildren();
        FileStatus[] fileStats = new FileStatus[fos.length];
        for (int i = 0; i < fos.length; i++) {
            fileStats[i] = getFileStatus(fos[i], new Path("vfs://" + fos[i].getURI().toString()));
        }
        return fileStats;
    }

    @Override
    public void setWorkingDirectory(Path path) {

    }

    @Override
    public Path getWorkingDirectory() {
        return new Path("vfs:///ftp:///fcftp:fcftp@172.18.1.52/");
    }

    @Override
    public boolean mkdirs(Path path, FsPermission fsPermission) throws IOException {
        return false;
    }

    @Override
    public FileStatus getFileStatus(Path path) throws IOException {
        FileSystemManager fsManager = VFS.getManager();
        FileSystemOptions options = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(options, true);
        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);

        return getFileStatus(fileObject, path);
    }

    private FileStatus getFileStatus(FileObject fileObject, Path file)
            throws IOException {
        long length = -1;
        boolean isDir = true;
        long modTime = -1;

        if (!fileObject.exists()) {
            throw new FileNotFoundException("File " + file + " does not exist.");
        }

        if (fileObject.getType() == FileType.FILE) {
            length = fileObject.getContent().getSize();
            isDir = false;
        }

        int blockReplication = 1;
        modTime = fileObject.getContent().getLastModifiedTime() * 1000L; // convert to milliseconds
        long accessTime = 0L;

        return new FileStatus(length, isDir, blockReplication, DEFAULT_BLOCK_SIZE, modTime,
                accessTime, null, null, null, file);
    }
}

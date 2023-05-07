package com.github.melin.jobserver.extensions.vfs;

import org.apache.commons.vfs2.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.util.Progressable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import static com.github.melin.jobserver.extensions.vfs.FileSystemUtils.buildFileSystemOptions;

public class VfsFileSystem extends FileSystem {

    public static final int DEFAULT_BLOCK_SIZE = 4 * 1024;

    private FileSystemManager fsManager;

    @Override
    public String getScheme() {
        return "vfs";
    }

    private URI uri;

    @Override
    public void initialize(URI uriInfo, Configuration conf) throws IOException {
        super.initialize(uriInfo, conf);
        this.uri = uriInfo;

        fsManager = VFS.getManager();
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public FSDataInputStream open(Path path, int i) throws IOException {
        FileSystemOptions options = buildFileSystemOptions(getConf(), path);
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
        FileSystemOptions options = buildFileSystemOptions(getConf(), path);
        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);

        return new FSDataOutputStream(fileObject.getContent().getOutputStream(), statistics);
    }

    @Override
    public FSDataOutputStream append(Path path, int i, Progressable progressable) throws IOException {
        throw new RuntimeException("not support");
    }

    @Override
    public boolean rename(Path src, Path dst) throws IOException {
        FileSystemOptions options = buildFileSystemOptions(getConf(), src);

        String srcPath = FileSystemUtils.buildUri(getConf(), src);
        String dstPath = FileSystemUtils.buildUri(getConf(), dst);

        FileObject srcObject = fsManager.resolveFile(srcPath, options);
        FileObject dstObject = fsManager.resolveFile(dstPath, options);
        srcObject.moveTo(dstObject);
        return true;
    }

    @Override
    public boolean delete(Path path, boolean recursive) throws IOException {
        FileSystemOptions options = buildFileSystemOptions(getConf(), path);
        delete(fsManager, options, path, recursive);
        return true;
    }

    /**
     * Convenience method, so that we don't open a new connection when using this
     * method from within another method. Otherwise every API invocation incurs
     * the overhead of opening/closing a TCP connection.
     */
    private boolean delete(FileSystemManager fsManager, FileSystemOptions options,
                           Path path, boolean recursive) throws IOException {

        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);

        if (FileType.FILE == fileObject.getType() || FileType.IMAGINARY == fileObject.getType()) {
            return fileObject.delete();
        }

        FileObject[] dirEntries = fileObject.getChildren();
        if (dirEntries != null && dirEntries.length > 0 && !(recursive)) {
            throw new IOException("Directory: " + file + " is not empty.");
        }
        for (FileObject dirEntry : dirEntries) {
            delete(fsManager, options, new Path("vfs://" + dirEntry.getURI().toString()), recursive);
        }
        return fileObject.delete();
    }

    @Override
    public FileStatus[] listStatus(Path path) throws IOException {
        FileSystemOptions options = buildFileSystemOptions(getConf(), path);

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
        throw new RuntimeException("not support");
    }

    @Override
    public Path getWorkingDirectory() {
        return new Path("/");
    }

    @Override
    public boolean mkdirs(Path path, FsPermission fsPermission) throws IOException {
        throw new RuntimeException("not support");
    }

    @Override
    public FileStatus getFileStatus(Path path) throws IOException {
        FileSystemOptions options = buildFileSystemOptions(getConf(), path);
        String file = FileSystemUtils.buildUri(getConf(), path);
        FileObject fileObject = fsManager.resolveFile(file, options);

        return getFileStatus(fileObject, path);
    }

    private FileStatus getFileStatus(FileObject fileObject, Path file) throws IOException {
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

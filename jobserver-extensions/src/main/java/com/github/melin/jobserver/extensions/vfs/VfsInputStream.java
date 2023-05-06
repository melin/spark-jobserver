package com.github.melin.jobserver.extensions.vfs;

import org.apache.commons.vfs2.FileContent;
import org.apache.hadoop.fs.FSExceptionMessages;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

class VfsInputStream extends FSInputStream {

    private final FileContent fileContent;
    private final String path;
    private InputStream wrappedStream;
    private FileSystem.Statistics stats;
    private boolean closed;
    private long pos;
    private long nextPos;
    private long contentLength;

    VfsInputStream(FileContent fileContent, String path, FileSystem.Statistics stats)
            throws IOException {
        this.fileContent = fileContent;
        this.path = path;
        this.stats = stats;
        this.wrappedStream = fileContent.getInputStream();
        this.contentLength = fileContent.getSize();
    }

    @Override
    public synchronized void seek(long position) throws IOException {
        checkNotClosed();
        if (position < 0) {
            throw new EOFException(FSExceptionMessages.NEGATIVE_SEEK);
        }
        nextPos = position;
    }

    @Override
    public synchronized int available() throws IOException {
        checkNotClosed();
        long remaining = contentLength - nextPos;
        if (remaining > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) remaining;
    }

    private void seekInternal() throws IOException {
        if (pos == nextPos) {
            return;
        }
        if (nextPos > pos) {
            long skipped = wrappedStream.skip(nextPos - pos);
            pos = pos + skipped;
        }
        if (nextPos < pos) {
            wrappedStream.close();
            wrappedStream = fileContent.getInputStream();
            pos = wrappedStream.skip(nextPos);
        }
    }

    @Override
    public boolean seekToNewSource(long targetPos) throws IOException {
        return false;
    }

    @Override
    public synchronized long getPos() throws IOException {
        return nextPos;
    }

    @Override
    public synchronized int read() throws IOException {
        checkNotClosed();
        if (this.contentLength == 0 || (nextPos >= contentLength)) {
            return -1;
        }
        seekInternal();
        int byteRead = wrappedStream.read();
        if (byteRead >= 0) {
            pos++;
            nextPos++;
        }
        if (stats != null & byteRead >= 0) {
            stats.incrementBytesRead(1);
        }
        return byteRead;
    }

    public synchronized void close() throws IOException {
        if (closed) {
            return;
        }
        super.close();
        wrappedStream.close();
        closed = true;
    }

    private void checkNotClosed() throws IOException {
        if (closed) {
            throw new IOException(path + ": " + FSExceptionMessages.STREAM_IS_CLOSED);
        }
    }
}

package org.elasolutions.utils.file;

import java.io.Closeable;
import java.io.IOException;

public interface SyncWriter extends Closeable {

    @Override
    public abstract void close() throws IOException;

    public abstract void write(String s) throws IOException;

    public abstract void flush() throws IOException;

    public abstract byte[] getByteArray();
}
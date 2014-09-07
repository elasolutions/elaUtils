package org.elasolutions.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.elasolutions.utils.CloseUtil;
import org.elasolutions.utils.InternalString;

public class SyncCompressFileWrite implements SyncWriter {

    public static SyncCompressFileWrite newCompression(final File file) throws CompressorException, FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("Null value for file");
        }
        FileOutputStream fileStream = new FileOutputStream(file);
        CompressorOutputStream compress =
                (new CompressorStreamFactory()).createCompressorOutputStream(
                    "bzip2", fileStream);
        return new SyncCompressFileWrite(fileStream,compress);
    }


    @Override
    public void close() throws IOException {
        flush();
        CloseUtil.close(m_compressOut);
        CloseUtil.close(m_outputStream);
    }

    @Override
    public void flush() throws IOException {
        if( m_compressOut!=null ) {
            m_compressOut.flush();
        }
    }

    @Override
    public byte[] getByteArray() {
        return m_outputStream.toString().getBytes();
    }

    @Override
    public void write(final String text) throws IOException {
        if (InternalString.isBlank(text)) {
            return;
        }
        lock.lock();
        try {
            m_compressOut.write(text.getBytes());
        } catch (IOException excep) {
            throw excep;
        } finally {
            lock.unlock();
        }
    }

    private SyncCompressFileWrite(FileOutputStream fileStream, CompressorOutputStream compress) {
        m_outputStream = fileStream;
        m_compressOut = compress;
    }

    private final ReentrantLock lock = new ReentrantLock(true);

    FileOutputStream m_outputStream;

    CompressorOutputStream m_compressOut;
}

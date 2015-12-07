package org.elasolutions.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.elasolutions.utils.CloseUtil;
import org.elasolutions.utils.InternalString;

/**
 * SyncCompressInMemory allows the compression to occur in memory.
 *
 * Use the getByteArray() to get the byte array to do something.
 *
 * @author Malcolm G. Davis
 * @version 1.0
 */
public class SyncCompressInMemory implements SyncWriter {

    /**
     * Creates the default compression bytestream
     *
     * @throws org.apache.commons.compress.compressors.CompressorException
     * SyncCompressInMemory
     * @return a {@link org.elasolutions.utils.file.SyncCompressInMemory} object.
     */
    public static SyncCompressInMemory newCompression() throws CompressorException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CompressorOutputStream compress = (new CompressorStreamFactory()).createCompressorOutputStream(
            "bzip2", byteStream);
        return new SyncCompressInMemory(byteStream,compress);
    }


    /**
     * <p>newCompression.</p>
     *
     * @param byteStream a {@link java.io.ByteArrayOutputStream} object.
     * @return a {@link org.elasolutions.utils.file.SyncCompressInMemory} object.
     * @throws org.apache.commons.compress.compressors.CompressorException if any.
     */
    public static SyncCompressInMemory newCompression(final ByteArrayOutputStream byteStream) throws CompressorException {
        if (byteStream == null) {
            throw new IllegalArgumentException("Null value for byteStream");
        }
        CompressorOutputStream compress = (new CompressorStreamFactory()).createCompressorOutputStream(
            CompressorStreamFactory.BZIP2, byteStream);
        return new SyncCompressInMemory(byteStream,compress);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        flush();
        CloseUtil.close(m_compressOut);
        CloseUtil.close(m_byteStream);
    }


    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        if( m_compressOut!=null ) {
            m_compressOut.flush();
        }
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getByteArray() {
        return m_byteStream.toByteArray();
    }

    /** {@inheritDoc} */
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

    private SyncCompressInMemory (final ByteArrayOutputStream byteStream, CompressorOutputStream compress)  {
        m_byteStream = byteStream;
        m_compressOut = compress;
    }

    private final ReentrantLock lock = new ReentrantLock(true);

    ByteArrayOutputStream m_byteStream;

    CompressorOutputStream m_compressOut;
}

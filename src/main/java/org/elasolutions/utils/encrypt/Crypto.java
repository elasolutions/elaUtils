package org.elasolutions.utils.encrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public interface Crypto {

    public abstract String decrypt(String str) throws IOException,
            IllegalBlockSizeException, BadPaddingException;

    public abstract String encrypt(String str)
            throws UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException;

}
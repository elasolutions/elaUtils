package org.elasolutions.utils.file;

import java.io.File;

import org.elasolutions.utils.InternalString;


/**
 * <p>FileUtils class.</p>
 *
 * @author malcolm
 * @version $Id: $Id
 */
public class FileUtils {

    /**
     * Verify that the folder exist, and can read and write
     * @param folder to verify
     * @return
     * boolean
     */
    public static boolean verifyFolder(String folder) {
        if( InternalString.isBlank(folder) ) {
            return false;
        }

        File dir = new File(folder);
        return dir.isDirectory() && dir.canWrite() && dir.canRead();
    }

    /**
     * Verify that the file exist, and that the file can be read.
     * @param file to verify
     * @return
     * boolean
     */
    public static boolean verifyFile(String file) {
        if( InternalString.isBlank(file) ) {
            return false;
    }

        File verify = new File(file);
        return verify.isFile() && verify.canRead();
    }
}

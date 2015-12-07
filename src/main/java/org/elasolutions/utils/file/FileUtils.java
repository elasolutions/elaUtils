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


    public static boolean verifyFolder(String folder) {
        if( InternalString.isBlank(folder) ) {
            return false;
        }

        File dir = new File(folder);

        return dir.isDirectory() && dir.canWrite() && dir.canRead();
    }

}

package org.elasolutions.utils;

/**
 * Internal string utils to avoid coupling to third party utils
 *
 * @author Malcolm G. Davis
 * @version 1.0
 */
public class InternalString {

    public static boolean isBlank(String string) {
        if( string==null ) {
            return true;
        }
        for( int index=0; index<string.length(); index++) {
            if( string.charAt(index)!=' ' ) {
                return false;
            }
        }
        return true;
    }

}

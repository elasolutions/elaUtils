package org.elasolutions.utils.jee;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * A mechanism to detect client software, version and platform from a user-agent string.
 *
 * To start extending the information, look at the following resources.
 *
 * @see http://www.hand-interactive.com/resources/detect-mobile-java.htm
 * @see http://wiki.drupalschool.net/index.php/Use_Java_to_Detect_Mobile_Devices
 * @see https://code.google.com/p/mobileesp/source/browse/Java/UAgentInfo.java
 * @see http://www.java2s.com/Code/Java/Servlets/Browserdetection.htm
 * @see http://wurfl.sourceforge.net/
 *
 */
public class WebClient implements Serializable {

    public static boolean isWindows(final HttpServletRequest request) {
        WebClient client = detect(request);

        boolean isWindows = true;
        Platform platform = client.getPlatform();
        switch(platform) {
            case ANDROID:
            case IOS:
            case JAVA_ME:
            case LINUX:
            case MACOSX:
            case UNKNOWN:
                isWindows = false;
                break;
            case WIN2K:
            case WIN7:
            case WIN95:
            case WIN98:
            case WINNT:
            case WINVISTA:
            case WINXP:
            case WIN8:
            case WIN81:
                isWindows = true;
                break;
            default:
                break;
        }
        return isWindows;
    }

    public static WebClient detect(final HttpServletRequest request) {
        return detect(request.getHeader("User-Agent"));
    }

    public static WebClient detect(final String userAgentString) {
        UserAgent ua = UserAgent.UNKNOWN;
        int version = 0;
        String ver = null;

        if( userAgentString!=null && userAgentString.trim().length()>0 ) {
            try {
                if (userAgentString.contains("Yahoo! Slurp")) {
                    ua = UserAgent.YAHOO_SLURP;
                } else if (userAgentString.contains("Googlebot/")) {
                    ua = UserAgent.GOOGLEBOT;
                    ver = userAgentString.substring(userAgentString.indexOf("Googlebot/")+10);
                    ver = ver.substring(0, (ver.indexOf(";") > 0 ? ver.indexOf(";") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("msnbot/")) {
                    ua = UserAgent.MSNBOT;
                    ver = userAgentString.substring(userAgentString.indexOf("msnbot/")+7);
                    ver = ver.substring(0, (ver.indexOf(" ") > 0 ? ver.indexOf(" ") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if(userAgentString.contains("Chrome/")) {
                    ua = UserAgent.CHROME;
                    ver = userAgentString.substring(userAgentString.indexOf("Chrome/")+7);
                    ver = ver.substring(0, ver.indexOf(" ")).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("Safari/")) {
                    ua = UserAgent.SAFARI;
                    ver = userAgentString.substring(userAgentString.indexOf("Version/")+8);
                    ver = ver.substring(0, (ver.indexOf(" ") > 0 ? ver.indexOf(" ") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("Opera Mini/")) {
                    ua = UserAgent.OPERA_MINI;
                    ver = userAgentString.substring(userAgentString.indexOf("Opera Mini/")+11);
                    ver = ver.substring(0, (ver.indexOf("/") > 0 ? ver.indexOf("/") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("Opera ")) {
                    ua = UserAgent.OPERA;
                    ver = userAgentString.substring(userAgentString.indexOf("Opera ")+6);
                    ver = ver.substring(0, (ver.indexOf(" ") > 0 ? ver.indexOf(" ") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("Firefox/")) {
                    ua = UserAgent.FIREFOX;
                    ver = userAgentString.substring(userAgentString.indexOf("Firefox/")+8);
                    ver = ver.substring(0, (ver.indexOf(" ") > 0 ? ver.indexOf(" ") : ver.length())).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                }  else if (userAgentString.contains("MSIE ")) {
                    ua = UserAgent.IE;
                    ver = userAgentString.substring(userAgentString.indexOf("MSIE ")+5);
                    ver = ver.substring(0, ver.indexOf(";")).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else if (userAgentString.contains("Opera/")) {
                    ua = UserAgent.OPERA;
                    ver = userAgentString.substring(userAgentString.indexOf("Opera/")+6);
                    ver = ver.substring(0, ver.indexOf(" ")).trim();
                    version = Integer.parseInt(ver.substring(0, ver.indexOf(".")));
                } else {
                    LOGGER.severe("Unknown agent=" + userAgentString);
                }
            } catch (NumberFormatException nfe) {
                ver = null;
                version = 0;
            }
        }
        Platform platform = detectedPlatform(userAgentString);
        return new WebClient(ua, version, ver, platform);
    }

    public WebClient(final UserAgent userAgent, final int majorVersion, final String fullVersion, final Platform platform) {
        this.m_userAgent = userAgent;
        this.m_majorVersion = majorVersion;
        this.m_fullVersion = fullVersion;
        this.m_platform = platform;
    }

    public Platform getPlatform() {
        return m_platform;
    }

    public String getFullVersion() {
        return m_fullVersion;
    }

    public int getMajorVersion() {
        return m_majorVersion;
    }

    public UserAgent getUserAgent() {
        return m_userAgent;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WebClient other = (WebClient) obj;
        if (this.m_userAgent != other.m_userAgent) {
            return false;
        }
        if (this.m_majorVersion != other.m_majorVersion) {
            return false;
        }
        if ((this.m_fullVersion == null) ? (other.m_fullVersion != null) : !this.m_fullVersion.equals(other.m_fullVersion)) {
            return false;
        }
        if (this.m_platform != other.m_platform) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.m_userAgent != null ? this.m_userAgent.hashCode() : 0);
        hash = 37 * hash + this.m_majorVersion;
        hash = 37 * hash + (this.m_fullVersion != null ? this.m_fullVersion.hashCode() : 0);
        hash = 37 * hash + (this.m_platform != null ? this.m_platform.hashCode() : 0);
        return hash;
    }


    private boolean checkInlineSupport(final boolean returnMhtml) {
        boolean mhtml = false;
        boolean dataUri = false;
        UserAgent userAgent = getUserAgent();
        switch (userAgent) {
            case CHROME:
            case FIREFOX:
            case SAFARI:
                dataUri = true;
                break;
            case OPERA:
                if (getMajorVersion() >= 7) {
                    dataUri = true;
                }
                break;
            case IE:
                if (getMajorVersion() >= 8) {
                    dataUri = true;
                } else if (getMajorVersion() >= 6 && !m_platform.equals(Platform.WINVISTA)) { // vista is unreliable for MHTML
                    mhtml = true;
                }
                break;
            case GOOGLEBOT:
                break;
            case MSNBOT:
                break;
            case OPERA_MINI:
                break;
            case UNKNOWN:
                break;
            case YAHOO_SLURP:
                break;
        }
        return (returnMhtml ? mhtml : dataUri);
    }

    public boolean supportsDataUris() {
        return checkInlineSupport(false);
    }

    public boolean supportsMHTML() {
        return checkInlineSupport(true);
    }

    private static Platform detectedPlatform(final String userAgent) {
        if( userAgent==null || userAgent.trim().length()==0 ) {
            return Platform.UNKNOWN;
        } else if(userAgent.contains("Android")) {
            return Platform.ANDROID;
        } else if(userAgent.contains("J2ME")) {
            return Platform.JAVA_ME;
        } else if(userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("iPad")) {
            return Platform.IOS;
        } else if(userAgent.contains("Mac OS X")) {
            return Platform.MACOSX;
        } else if (userAgent.contains("Windows NT 5.0")) {
            return Platform.WIN2K;
        } else if (userAgent.contains("Windows NT 5.1") || userAgent.contains("Windows NT 5.2") || userAgent.contains("Windows XP")) {
            return Platform.WINXP;
        } else if (userAgent.contains("Windows NT 6.0")) {
            return Platform.WINVISTA;
        } else if (userAgent.contains("Windows NT 6.1")) {
            return Platform.WIN7;
        } else if (userAgent.contains("Windows NT 6.2")) {
            return Platform.WIN8;
        } else if (userAgent.contains("Windows NT 6.3")) {
            return Platform.WIN81;
        } else if (userAgent.contains("Windows NT")) {
            return Platform.WINNT;
        } else if(userAgent.contains("Mac")) {
            return Platform.MACOSX;
        } else if (userAgent.contains("Linux")) {
            return Platform.LINUX;
        }
        LOGGER.severe("Unknown platform type=" + userAgent);
        return Platform.UNKNOWN;
    }



    @Override
    public String toString() {
        return m_userAgent+" "+m_fullVersion+" / "+m_platform;
    }


    private final UserAgent m_userAgent;
    private final int m_majorVersion;
    private final String m_fullVersion;
    private final Platform m_platform;


    private static final Logger LOGGER =
            Logger.getLogger(WebClient.class.getName());
}

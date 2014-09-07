package org.elasolutions.utils.jee;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Dumps the HttpServletRequest to a buffer for display, debugging or logging.
 * 
 * @author Malcolm G. Davis
 * @version 1.0
 */
public class ServletUtil {

    public static StringBuffer dumpRequest(final HttpServletRequest request) {
        final StringBuffer buffer = new StringBuffer();
        writeDebugEnums(request, buffer, MessageCategory.Attribute, request.getAttributeNames());
        writeDebugEnums(request, buffer, MessageCategory.Header, request.getHeaderNames());
        writeDebugEnums(request, buffer, MessageCategory.Param, request.getParameterNames());
        writeDebug(buffer, "", "Auth Type", request.getAuthType());
        writeDebug(buffer, "", "Character Encoding", "" + request.getCharacterEncoding());
        writeDebug(buffer, "", "Context Type", "" + request.getContentType());
        writeDebug(buffer, "", "Context Path", request.getContextPath());
        writeDebug(buffer, "", "Method", request.getMethod());
        writeDebug(buffer, "", "Path Info", request.getPathInfo());
        writeDebug(buffer, "", "Query String", request.getQueryString());
        writeDebug(buffer, "", "Request URI", request.getRequestURI());
        writeDebug(buffer, "", "Remote Addr", request.getRemoteAddr());
        writeDebug(buffer, "", "Remote Host", request.getRemoteHost());
        writeDebug(buffer, "", "Remote User", request.getRemoteUser());
        writeDebug(buffer, "", "Remote Port", "" + request.getRemotePort());
        writeDebug(buffer, "", "Server Name", "" + request.getServerName());
        writeDebug(buffer, "", "Server Port", "" + request.getServerPort());
        writeDebug(buffer, "", "Servlet Path", "" + request.getServletPath());

        final HttpSession session = request.getSession();
        if (session == null) {
            buffer.append("Session is null");
        } else {
            buffer.append("Session Id: " + session.getId());
            final Enumeration<?> enumerates = session.getAttributeNames();
            while (enumerates.hasMoreElements()) {
                final String key = (String) enumerates.nextElement();
                final Object value = session.getAttribute(key);
                String keyValue = "";
                if (value != null) {
                    keyValue = value.toString();
                }
                writeDebug(buffer, "Session Attribute", key, keyValue);
            }
        }
        buffer.append("\r\n");
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(final Cookie cookie : cookies) {
                writeDebug(buffer, "Cookie", cookie.getName(), cookie.getValue());
            }
        }
        return buffer;
    }

    static private void writeDebugEnums(final HttpServletRequest request, final StringBuffer buffer,
            final MessageCategory category, final Enumeration<?> enumerates) {
        while (enumerates.hasMoreElements()) {
            final String key = (String) enumerates.nextElement();
            Object value = "";

            switch (category) {
                case Attribute:
                    value = request.getAttribute(key);
                    break;
                case Header:
                    value = request.getHeader(key);
                    break;
                case Param:
                    value = request.getParameter(key);
                    break;
                default:
                    break;
            }
            String keyValue = "";
            if (value != null) {
                keyValue = value.toString();
            }
            writeDebug(buffer, category.toString(), key, keyValue);
        }
    }

    static private void writeDebug(final StringBuffer buffer, final String category, final String name,
            final String value) {
        buffer.append(category + ": " + name + "=" + value + "\r\n");
    }

    enum MessageCategory {
        Param, Header, Attribute
    }
}

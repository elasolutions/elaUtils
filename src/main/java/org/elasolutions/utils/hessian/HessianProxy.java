package org.elasolutions.utils.hessian;

import java.net.MalformedURLException;

import org.elasolutions.utils.InternalString;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * HessianProxy singleton wrapper to the HessianProxyFactory
 *
 * @author Malcolm G. Davis
 * @version 1.0
 */
public enum HessianProxy {
    INSTANCE;

    public Object create(final Class api, final String service) throws MalformedURLException {
        if( InternalString.isBlank(m_uri) ) {
            throw new IllegalStateException("URI must be set prior to using Hessian proxy");
        }
        return getHessianProxyFactory().create(api, m_uri + service);
    }

    public void setURI(final String uri) {
        m_uri = uri;
        if( !m_uri.endsWith("/") ) {
            m_uri += "/";
        }
    }

    public String getURI() {
        return m_uri;
    }

    HessianProxyFactory getHessianProxyFactory() {
        if (m_HessianProxyFactory == null) {
            m_HessianProxyFactory = new HessianProxyFactory();
            //m_HessianProxyFactory.setReadTimeout(TIMEOUT);
            // m_HessianProxyFactory.setConnectTimeout(TIMEOUT);
        }
        return m_HessianProxyFactory;
    }

    HessianProxyFactory m_HessianProxyFactory;

    private String m_uri;

    // 5 minutes * 60 seconds * 1000 milliseconds
    // static final long TIMEOUT = 20*60*1000;  // NOTE: Timeout is 20 minutes
    // static final long TIMEOUT = 60*60*1000;  // NOTE: Timeout is 60 minutes

}

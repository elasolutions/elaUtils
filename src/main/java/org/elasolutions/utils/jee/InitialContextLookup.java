package org.elasolutions.utils.jee;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;


public enum InitialContextLookup {
    INSTANCE;

    public String lookup(String name) throws NamingException {
        Object o = getInitialContext().lookup(name);
        return ( o!=null ) ? o.toString() : "";
    }

    public Integer lookupInteger(String name) throws NamingException {
        Object o = getInitialContext().lookup(name);
        return ( o!=null ) ? (Integer)o : Integer.valueOf(0);
    }

    public String lookupList(String path) throws NamingException {
        StringBuffer values  = new StringBuffer();
        NamingEnumeration<NameClassPair> list = getInitialContext().list(path);
        while (list.hasMore()) {
            String name = list.next().getName();
            String value = lookup(path+ "/" + name);
            values.append(name).append("=").append(value).append("\r\n");
        }
        return values.toString();
    }

    public Map<String,String> lookupMap(String path) throws NamingException {
        Map<String,String>map = new HashMap<String,String>();
        NamingEnumeration<NameClassPair> list = getInitialContext().list(path);
        while (list.hasMore()) {
            String name = list.next().getName();
            String value = lookup(path+ "/" + name);
            map.put(name,value);
        }
        return map;
    }

    InitialContext getInitialContext() throws NamingException {
        if( context==null ) {
            context = new InitialContext();
        }
        return context;
    }

    InitialContext context = null;
}

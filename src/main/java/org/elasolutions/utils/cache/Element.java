package org.elasolutions.utils.cache;

public class Element<I, T>   {

    public Element(final I id, final T item) {
        m_id = id;
        m_item = item;
        m_lastAccessed = System.currentTimeMillis();
    }

    public I getKey() {
        return m_id;
    }

    public T getObjectValue() {
        m_lastAccessed = System.currentTimeMillis();
        return m_item;
    }

    public void setObjectValue(final T item) {
        m_lastAccessed = System.currentTimeMillis();
        m_item = null; // force a clear of the previous reference.
        m_item = item;
    }

    public long elapsedInMilliseconds() {
        return (System.currentTimeMillis()-m_lastAccessed);
    }

    public long elapsedInSeconds() {
        return (System.currentTimeMillis()-m_lastAccessed)/1000;
    }

    private long m_lastAccessed;

    private T m_item;

    private final I m_id;
}

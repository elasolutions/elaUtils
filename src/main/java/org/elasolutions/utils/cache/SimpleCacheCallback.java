package org.elasolutions.utils.cache;


public interface SimpleCacheCallback<I, T> {
    void removedElement(Element<I, T>element);
}

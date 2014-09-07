package org.elasolutions.utils.cache;


public class SimpleCacheCallbackNull implements SimpleCacheCallback<Object, Object> {

    @Override
    public void removedElement(Element<Object, Object> element) {
        // do nothing
    }

}

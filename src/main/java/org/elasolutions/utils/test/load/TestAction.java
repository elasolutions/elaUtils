package org.elasolutions.utils.test.load;

import org.elasolutions.utils.test.EventStatus;

/**
 * <p>StressAction interface.</p>
 *
 * @author malcolm
 * @version $Id: $Id
 */
public interface TestAction {

    /**
     * <p>action.</p>
     *
     * @param threadNumber is the count of the thread (similar to an id) a int.
     * @param count a int.
     * @param optionalResultMessage that can be set
     * @return a {@link java.lang.String} object.
     */
    public EventStatus action(int threadNumber, int count, final StringBuffer optionalResultMessage);

    /**
     * <p>cleanup.</p>
     */
    public void cleanup();
}

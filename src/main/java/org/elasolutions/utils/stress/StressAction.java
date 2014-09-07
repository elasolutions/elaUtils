package org.elasolutions.utils.stress;

public interface StressAction {

    public String action(int threadId, int count);

    public void cleanup();

}
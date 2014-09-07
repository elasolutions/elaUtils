package org.elasolutions.utils.stress;

import java.util.ArrayList;
import java.util.List;

public class StressTest {

    public static StressTest newTest() {
        return new StressTest();
    }

    public void run(final String name, final int numberOfThreads,
            final int numberOfActionsPerThread, final long delayBetweenActionsInMilliseconds,
            final boolean random, final StressAction action) {

        for(int count = 0; count < numberOfThreads; count++) {
            final StressThead thread =
                    new StressThead(name, count, numberOfActionsPerThread,
                        delayBetweenActionsInMilliseconds, random, action);
            thread.setPrintToSystemOut(printToSystemOut());
            getStressThreads().add(thread);
        }

        final long start = System.currentTimeMillis();
        for (final StressThead thread : getStressThreads()) {
            thread.start();
        }

        long totalAverageThreadTimes = 0;

        int cnt = 1;
        for (final StressThead thread : getStressThreads()) {
            try {
                thread.join();
                totalAverageThreadTimes = totalAverageThreadTimes + thread.averageRuntime();

                if (printToSystemOut()) {
                    String result = "Thread: " + cnt + ",  average runtime: "
                            + thread.averageRuntime();
                    System.out.println(result);
                }
                cnt++;
            } catch (final InterruptedException excep) {
                excep.printStackTrace();
            }
        }
        for (final StressThead thread : getStressThreads()) {
            thread.cleanup();
        }

        m_averageThreadTimes = (totalAverageThreadTimes / (cnt - 1));

        m_totalRuntime = System.currentTimeMillis() - start;
    }

    protected List<StressThead> getStressThreads() {
        if (m_testsList == null) {
            m_testsList = new ArrayList<StressThead>();
        }
        return m_testsList;
    }

    /**
     * Display individual execution results as the stress is executing.
     * This will impact the results due to the additional load placed on the system.
     * 
     * @param displayFlag
     * void
     */
    public void setDisplayDuringExecution(boolean displayFlag) {
        m_displayResultsDuringExecution = displayFlag;
    }

    public boolean printToSystemOut() {
        return m_displayResultsDuringExecution;
    }

    public long getTotalRuntime() {
        return m_totalRuntime;
    }

    public long getAverageThreadTime() {
        return m_averageThreadTimes;
    }

    private List<StressThead> m_testsList;

    private boolean m_displayResultsDuringExecution;

    private long m_totalRuntime;

    private long m_averageThreadTimes;

    private StressTest() {

    }

}

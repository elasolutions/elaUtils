package org.elasolutions.utils.stress;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>StressTest class.</p>
 *
 * @author malcolm
 * @version $Id: $Id
 */
public class StressTest {

    /**
     * <p>newTest.</p>
     *
     * @return a {@link org.elasolutions.utils.stress.StressTest} object.
     */
    public static StressTest newTest() {
        return new StressTest();
    }

    /**
     * <p>run.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param numberOfThreads a int.
     * @param numberOfActionsPerThread a int.
     * @param delayBetweenActionsInMilliseconds a long.
     * @param random a boolean.
     * @param action a {@link org.elasolutions.utils.stress.StressAction} object.
     */
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

    /**
     * <p>getStressThreads.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<StressThead> getStressThreads() {
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

    /**
     * <p>printToSystemOut.</p>
     *
     * @return a boolean.
     */
    public boolean printToSystemOut() {
        return m_displayResultsDuringExecution;
    }

    /**
     * <p>getTotalRuntime.</p>
     *
     * @return a long.
     */
    public long getTotalRuntime() {
        return m_totalRuntime;
    }

    /**
     * <p>getAverageThreadTime.</p>
     *
     * @return a long.
     */
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

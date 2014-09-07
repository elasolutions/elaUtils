package org.elasolutions.utils.stress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class StressThead extends Thread {

    StressThead(final String name, final int threadId, final int count, final long millisDelay,
        final boolean randomDelay, final StressAction runAction) {
        m_name = name;
        m_threadId = threadId;
        m_maxCount = count;
        m_delay = millisDelay;
        m_randomDelay = randomDelay;
        m_runAction = runAction;
    }

    @Override
    public void run() {
        m_startTime = System.currentTimeMillis();

        for(int count = 0; count < m_maxCount; count++) {
            final long start = System.currentTimeMillis();

            final String status = m_runAction.action(m_threadId, count);

            final long runtime = (System.currentTimeMillis() - start);

            m_totalRuntime += runtime;

            final long actionTimein = (System.currentTimeMillis() - m_startTime);

            if (printToSystemOut()) {
                System.out.printf("%s: id=%4d,  cnt=%4d,  %s,  test runtime: %d,  test total time: %d\r\n", m_name,
                    Integer.valueOf(m_threadId), Integer.valueOf(count), status,
                    Long.valueOf(runtime), Long.valueOf(actionTimein));
            }

            StressResults results = StressResults.newResult(m_threadId, count, runtime, status);
            getResults().add(results);

            if (m_delay > 0) {
                try {
                    if (m_randomDelay) {
                        // should the delay be a random value?
                        final Random random = new Random();
                        final long pause = random.nextInt(Long.valueOf(m_delay).intValue());
                        Thread.sleep(pause);
                    } else {
                        Thread.sleep(m_delay);
                    }
                } catch (final InterruptedException excep) {
                }
            }
        }
    }

    protected List<StressResults> getResults() {
        if (m_results == null) {
            m_results = new ArrayList<StressResults>();

        }
        return m_results;
    }

    public void setPrintToSystemOut(boolean printToSystemOut) {
        m_printToSystemOut = printToSystemOut;
    }

    public boolean printToSystemOut() {
        return m_printToSystemOut;
    }

    public void cleanup() {
        m_runAction.cleanup();
    }

    public long getTotalRuntime() {
        return m_totalRuntime;
    }

    public long averageRuntime() {
        return m_totalRuntime / m_maxCount;
    }

    @Override
    public String toString() {
        return "Thread=" + m_threadId + ",  average.runtime= " + averageRuntime();
    }

    private List<StressResults> m_results;

    private boolean m_printToSystemOut = true;

    private final boolean m_randomDelay;

    private final int m_threadId;

    private final String m_name;

    private final StressAction m_runAction;

    private final int m_maxCount;

    private final long m_delay;

    private long m_totalRuntime;

    private long m_startTime;
}
package org.elasolutions.utils.test.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.elasolutions.utils.test.EventDetailResult;
import org.elasolutions.utils.test.EventStatus;
import org.elasolutions.utils.test.NullTestExecutionListener;
import org.elasolutions.utils.test.TestEvent;
import org.elasolutions.utils.test.TestExecutionListener;

public class TestThread extends Thread {

    TestThread(final String name, final int threadId, final int maxIterations, long maxRunTime,
        final long millisDelay, final boolean randomDelay, final TestAction runAction, TestExecutionListener testListener) {
        m_name = name;
        m_threadId = threadId;
        m_maxIterations = maxIterations;
        m_maxRunTime = maxRunTime;
        m_delay = millisDelay;
        m_randomDelay = randomDelay;
        m_runAction = runAction;
        m_testListener = ( testListener==null ) ? new NullTestExecutionListener() : testListener ;
    }


    /** {@inheritDoc} */
    @Override
    public void run() {
        m_startTime = System.currentTimeMillis();

        boolean execute = true;

        final StringBuffer optionalDescription = new StringBuffer();

        while(execute) {

            // increment count and check count if using counter for testing
            m_executionCount++;
            if( m_maxIterations>0  ) {
                if( m_executionCount > m_maxIterations ) {
                    execute = false;
                    break;
                }
            }

            // check run time if max run time is configured
            if( m_maxRunTime>0  ) {
                long totalRunTime = System.currentTimeMillis() - m_startTime;
                if( totalRunTime>=m_maxRunTime  ) {
                    execute = false;
                    break;
                }
            }

            final long start = System.currentTimeMillis();

            m_testListener.event(TestEvent.Start, EventStatus.Success, m_threadId, m_executionCount, 0, 0);

            final EventStatus status = m_runAction.action(m_threadId, m_executionCount, optionalDescription);

            final long runtime = (System.currentTimeMillis() - start);

            m_testExecutionTime += runtime;

            final long actionTimein = (System.currentTimeMillis() - m_startTime);

            m_testListener.event(TestEvent.End, status, m_threadId,  m_executionCount,  runtime, actionTimein);

            EventDetailResult results = EventDetailResult.newResult(m_threadId, m_executionCount, runtime, status,
                optionalDescription.toString());

            getResults().add(results);

            optionalDescription.setLength(0);

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

        m_endTime = System.currentTimeMillis();
    }

    /**
     * <p>getResults.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<EventDetailResult> getResults() {
        if (m_results == null) {
            m_results = new ArrayList<EventDetailResult>();
        }
        return m_results;
    }

    /**
     * <p>cleanup.</p>
     */
    public void cleanup() {
        m_runAction.cleanup();
    }

    /**
     * <p>The number of times that the thread executed.</p>
     * <p>This could be less than the total count if a timeframe limit (m_totalRuntime) has been used.</p>
     *
     * @return a int.
     */
    public int executionCount() {
        return m_executionCount;
    }

    /**
     * <p>totalTestExecutionTime is the total of all test, and does not include pauses or thread sleeps.
     * TestExecutionTime and thread time should be different.
     * </p>
     *
     * @return a long.
     */
    public long totalTestExecutionTime() {
        return m_testExecutionTime;
    }

    /**
     * <p>averageRuntime is the totalTestExecutionTime divided by the total execution count.</p>
     *
     * @return a long.
     */
    public long averageRuntime() {
        return m_testExecutionTime / m_executionCount;
    }


    /**
     *
     * @return
     * String
     */
    public String getTestName() {
        return m_name;
    }


    /**
     * <p>getTestThreadTime.</p>
     *
     * @return a long.
     */
    public long getThreadRuntime() {
        return m_endTime - m_startTime;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "TestThread[id=" + m_threadId
                + ",  startTime=" + m_startTime
                + ",  endTime=" + m_endTime
                + ",  threadRuntime =" + getThreadRuntime()
                + ",  testExecutionTime=" + m_testExecutionTime
                + ",  executionCount=" + m_executionCount
                + ",  averageRuntime=" + averageRuntime()
                + "]";
    }

    private List<EventDetailResult> m_results;

    private final boolean m_randomDelay;

    private final int m_threadId;

    private final String m_name;

    private final TestAction m_runAction;

    private final int m_maxIterations;

    private final long m_delay;

    private long m_testExecutionTime;

    private long m_startTime;

    private long m_endTime;

    private final long m_maxRunTime;

    private int m_executionCount;

    private final TestExecutionListener m_testListener;

}

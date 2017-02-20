package org.elasolutions.utils.test.load;

import java.util.ArrayList;
import java.util.List;

import org.elasolutions.utils.test.TestExecutionListener;

/**
 * <p>Controls the creation and execution of the test.</p>
 *
 * @author malcolm
 * @version $Id: $Id
 */
public class TestCaseExecutor {

    /**
     * Create a new test.
     * @param name name of the test
     * @param maxIterationsPerThread max number of test iterations. (optional.  0 means no limit)
     * @param maxRuntime max runtime of the individual test threads.  (optional.  0 means no limit)
     * @param delayBetweenActionsInMilliseconds between executions
     * @param random if the delay should have a random
     * @return a {@link org.elasolutions.utils.test.performance.TestCaseExecutor} object.
     */
    public static TestCaseExecutor newTest(final String name,
            final int maxIterationsPerThread,
            final long maxRuntime,
            final long delayBetweenActionsInMilliseconds,
            final boolean random) {

        if( maxRuntime<=0l && maxIterationsPerThread<=0 ) {
            throw new IllegalArgumentException(
                "Neither maxIterationsPerThread nor maxRunTime are configured. "
                        + "One or both fields must be configured.");
        }
        return new TestCaseExecutor(name, maxIterationsPerThread, maxRuntime, delayBetweenActionsInMilliseconds, random);
    }

    /**
     * Multiple threads using a single action.
     *
     * @param action to execute
     * @param numberOfThreads to create against the single action.
     * void
     */
    public void run(final int numberOfThreads, final TestAction action) {
        run( numberOfThreads, action, null);
    }


    /**
     * Multiple threads using a single action.
     *
     * @param action to execute
     * @param numberOfThreads to create against the single action.
     * @param testListener is an optional listener to events
     * void
     */
    public void run(final int numberOfThreads, final TestAction action, TestExecutionListener testListener) {
        if(action==null) {
            throw new IllegalArgumentException("Null value for action");
        }
        if(numberOfThreads<1) {
            throw new IllegalArgumentException("Invalid numberOfThreads value="+numberOfThreads);
        }
        // configure testing
        for(int count = 0; count < numberOfThreads; count++) {
            final TestThread thread = new TestThread(m_name, count, m_maxIterationsPerThread,
                m_maxRuntime, m_delayBetweenActionsInMilliseconds, m_random, action, testListener);
            getTestList().add(thread);
        }

        executeTest();
    }


    /**
     * A single thread per-action.
     *
     * @param actions to execute.  A thread will be created for each action in the list.
     * void
     */
    public void run(final List<TestAction>actions) {
        run(actions, null);
    }


    /**
     * A single thread per-action.
     *
     * @param actions to execute.  A thread will be created for each action in the list.
     * @param testListener is an optional listener to events
     * void
     */
    public void run(final List<TestAction>actions, TestExecutionListener testListener) {
        if(actions==null) {
            throw new IllegalArgumentException("Null value for actions");
        }
        if(actions.isEmpty()) {
            throw new IllegalArgumentException("No actions to execute");
        }

        // configure testing
        for(int count = 0; count < actions.size(); count++) {
            TestAction actionToUse = actions.get(count);
            final TestThread thread =
                    new TestThread(m_name, count, m_maxIterationsPerThread,
                        m_maxRuntime, m_delayBetweenActionsInMilliseconds, m_random, actionToUse, testListener);
            getTestList().add(thread);
        }

        executeTest();
    }

    void executeTest() {
        //----------------------------------------------------------------------------
        // begin test
        getResults().setStartTime(System.currentTimeMillis());
        for (final TestThread thread : getTestList()) {
            thread.start();
        }

        //----------------------------------------------------------------------------
        // wait for test to finish
        for (final TestThread thread : getTestList()) {
            try {
                thread.join();
            } catch (final InterruptedException excep) {
                excep.printStackTrace();
            }
        }
        getResults().setEndTime(System.currentTimeMillis());

        //----------------------------------------------------------------------------
        // record results
        long totalAverageThreadTimes = 0;
        int cnt = 1;
        for (final TestThread thread : getTestList()) {
            totalAverageThreadTimes = totalAverageThreadTimes + thread.averageRuntime();
            getResults().addTestThread(thread);
            cnt++;
        }
        getResults().setAverageThreadTime((totalAverageThreadTimes / (cnt - 1)));

        //----------------------------------------------------------------------------
        // ask the tests cleanup
        for (final TestThread thread : getTestList()) {
            thread.cleanup();
        }
    }

    public TestResults getResults() {
        if( m_testResults==null ) {
            m_testResults = TestResults.newResults(m_name, getTestList().size(), m_maxIterationsPerThread, m_maxRuntime);
        }
        return m_testResults;
    }

    /**
     * <p>The test threads used to run the actions.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public TestThread[] getTestThreads() {
        return getTestList().toArray(new TestThread[0]);
    }


    /**
     * <p>getTestList.</p>
     *
     * @return a {@link java.util.List} object.
     */
    private List<TestThread> getTestList() {
        if (m_testsList == null) {
            m_testsList = new ArrayList<TestThread>();
        }
        return m_testsList;
    }

    private List<TestThread> m_testsList;

    private TestResults m_testResults;

    private final String m_name;

    private final int m_maxIterationsPerThread;

    private final long m_maxRuntime;

    private final long m_delayBetweenActionsInMilliseconds;

    private final boolean m_random;

    private TestCaseExecutor(final String name,
            final int maxIterationsPerThread,
            final long maxRuntime,
            final long delayBetweenActionsInMilliseconds,
            final boolean random) {
        m_name = name;
        m_maxIterationsPerThread = maxIterationsPerThread;
        m_maxRuntime = maxRuntime;
        m_delayBetweenActionsInMilliseconds = delayBetweenActionsInMilliseconds;
        m_random = random;
    }

}

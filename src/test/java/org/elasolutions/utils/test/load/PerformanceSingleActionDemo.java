package org.elasolutions.utils.test.load;

import org.elasolutions.utils.test.EventDetailResult;
import org.elasolutions.utils.test.EventStatus;
import org.elasolutions.utils.test.PrintTestExecutionListener;

public class PerformanceSingleActionDemo {

    public static void main(final String[] args) {
        System.out.println("Start: PerformanceSingleActionDemo");
        System.out.println("---------------------------------------------");

        final long start = System.currentTimeMillis();

        final PerformanceSingleActionDemo test = new PerformanceSingleActionDemo();
        test.run();

        System.out.println("---------------------------------------------");
        System.out.println("Runtime: " + (System.currentTimeMillis() - start));
        System.out.println("Finished.");
        System.exit(0);
    }

    private void run() {
        ///////////////////////////////////////////////////////////////////////////////////////////
        // Determine how you want the class to be executed.
        final String testName = "performance demo test";

        final int numberOfThreads = 10;

        final int numberOfActionsPerThread = 50;

        final long maxRuntime = -1; // no limit on the runtime

        final long delayBetweenActionsInMilliseconds = 50;

        final boolean useRandomDelay = true;


        ///////////////////////////////////////////////////////////////////////////////////////////
        // 1 action, multiple threads against the action
        final TestCaseExecutor tester = TestCaseExecutor.newTest(testName, numberOfActionsPerThread, maxRuntime,
            delayBetweenActionsInMilliseconds, useRandomDelay);

        tester.run(numberOfThreads, new TestActionImpl(), new PrintTestExecutionListener());

        // display results
        for (TestThread result : tester.getTestThreads()) {
            System.out.println(result.toString());
            for( EventDetailResult details : result.getResults() ) {
                System.out.println("\t" + details.toString());
            }
        }

        TestResults results = tester.getResults();
        System.out.println("Total runtime = " + results.getTestDuration());
        System.out.println("Average thread execution time= " + results.getAverageThreadTime());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Test: This is the test that will be executed.   The action() contains the code to be executed
    class  TestActionImpl implements TestAction {
        @Override
        public EventStatus action(int threadNumber, int count, final StringBuffer optionalResultMessage) {
            //-----------------------------------------------
            // The count corresponds to the number of times this method has been called.
            // count is useful for things like logging or pull records from a list or file.

            //-----------------------------------------------
            // do something here.
            optionalResultMessage.append("complete");

            // The thread is to simulate activity
            try {
                Thread.sleep(20);
            } catch (InterruptedException excep) {
                excep.printStackTrace();
            }

            //-----------------------------------------------
            // return the status or message of the test.
            return EventStatus.Success;
        }

        @Override
        public void cleanup() {
            // do some clean up here, such as closing database connections.
        }
    }
}

package org.elasolutions.utils.test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogExecutionListener implements TestExecutionListener {

    public LogExecutionListener(Level level) {
        m_level = level;

    }

    @Override
    public void event(TestEvent event, EventStatus status, int threadId,
            int executionCount, long runtime, long totalExecuteTime) {

        if(TestEvent.Start.equals(event)) {
            LOG.log(m_level, String.format("Start:    id=%4d,  cnt=%4d\r\n",
                Integer.valueOf(threadId), Integer.valueOf(executionCount)));
        } else {
            LOG.log(m_level, String.format("Executed: id=%4d,  cnt=%4d,  %s,  runtime=%d,  totalThreadRuntime=%d\r\n",
                Integer.valueOf(threadId), Integer.valueOf(executionCount), status,
                Long.valueOf(runtime), Long.valueOf(totalExecuteTime)));
        }

    }

    private final Level m_level;

    private static final Logger LOG =
            Logger.getLogger(LogExecutionListener.class.getName());
}

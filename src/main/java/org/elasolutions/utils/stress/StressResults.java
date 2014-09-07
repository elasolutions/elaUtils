package org.elasolutions.utils.stress;

public class StressResults {

    public static StressResults newResult(final int threadId, final int count, final long timeIn,
            final String status) {
        return new StressResults(threadId, count, timeIn, status);
    }

    public int getCount() {
        return m_count;
    }

    public long getExcutetime() {
        return m_excutetime;
    }

    public String getStatus() {
        return m_status;
    }

    public int getThreadId() {
        return m_threadId;
    }

    @Override
    public String toString() {
        return "status=" +m_status + ",  count=" + m_count + ",  runtime=" + m_excutetime;
    }

    StressResults(final int threadId, final int count, final long executeTime,
        final String status) {
        m_threadId = threadId;
        m_count = count;
        m_excutetime = executeTime;
        m_status = status;
    }

    private final int m_count;

    private final long m_excutetime;

    private final String m_status;

    private final int m_threadId;
}

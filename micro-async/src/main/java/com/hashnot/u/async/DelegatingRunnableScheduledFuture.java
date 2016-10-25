package com.hashnot.u.async;

import java.util.concurrent.*;

/**
 * @author Rafał Krupiński
 */
public class DelegatingRunnableScheduledFuture<ResultT> implements RunnableScheduledFuture<ResultT> {
    private RunnableScheduledFuture<ResultT> runnableScheduledFuture;

    public DelegatingRunnableScheduledFuture(RunnableScheduledFuture<ResultT> runnableScheduledFuture) {
        this.runnableScheduledFuture = runnableScheduledFuture;
    }

    @Override
    public boolean isPeriodic() {
        return runnableScheduledFuture.isPeriodic();
    }

    @Override
    public void run() {
        runnableScheduledFuture.run();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return runnableScheduledFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return runnableScheduledFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return runnableScheduledFuture.isDone();
    }

    @Override
    public ResultT get() throws InterruptedException, ExecutionException {
        return runnableScheduledFuture.get();
    }

    @Override
    public ResultT get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return runnableScheduledFuture.get(timeout, unit);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return runnableScheduledFuture.getDelay(unit);
    }

    @Override
    public int compareTo(Delayed o) {
        return runnableScheduledFuture.compareTo(o);
    }
}

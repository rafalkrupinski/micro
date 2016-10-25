package com.hashnot.u.async.executor;

import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * {@link ScheduledThreadPoolExecutor} extension that accepts decorator for {@link Runnable} and {@link Callable}.
 * <p>
 * By default, Runnable decorator just wraps Callable decorator.
 *
 * @author Rafał Krupiński
 */
public class DecoratingExecutorService extends ScheduledThreadPoolExecutor {

    private BiFunction<Callable<?>, RunnableScheduledFuture<?>, RunnableScheduledFuture<?>> callableTaskDecorator;
    private Function<Callable<?>, Callable<?>> callableDecorator;

    public DecoratingExecutorService(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return schedule(Executors.callable(command, null), delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return super.schedule(decorateCallable(callable), delay, unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        return callableTaskDecorator == null ? task : (RunnableScheduledFuture<V>) callableTaskDecorator.apply(callable, task);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        return callableTaskDecorator == null ? task : (RunnableScheduledFuture<V>) callableTaskDecorator.apply(Executors.callable(runnable, null), task);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return super.scheduleAtFixedRate(decorate(command), initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(decorate(command), initialDelay, delay, unit);
    }

    @SuppressWarnings("unchecked")
    private <V> Callable<V> decorateCallable(Callable<V> callable) {
        return callableDecorator == null ? callable : (Callable<V>) callableDecorator.apply(callable);
    }

    private Runnable decorate(Runnable command) {
        if (callableDecorator == null)
            return command;

        Callable<Void> callable = Executors.callable(command, null);
        return () -> {
            try {
                callable.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new UnsupportedOperationException("Runnable wrapper thrown Exception other that RuntimeException", e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public <ResultT> void setCallableTaskDecorator(BiFunction<Callable<ResultT>, RunnableScheduledFuture<ResultT>, RunnableScheduledFuture<ResultT>> callableTaskDecorator) {
        this.callableTaskDecorator = (BiFunction) callableTaskDecorator;
    }

    @SuppressWarnings("unchecked")
    public <ResultT> void setCallableDecorator(Function<Callable<ResultT>, Callable<ResultT>> callableDecorator) {
        this.callableDecorator = (Function) callableDecorator;
    }
}

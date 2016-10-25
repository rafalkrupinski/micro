package com.hashnot.u.async.executor;

import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Rafał Krupiński
 */
public class ExecutorFactory implements Supplier<ScheduledExecutorService> {
    private int corePoolSize;
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
    private BiFunction<Callable<?>, RunnableScheduledFuture<?>, RunnableScheduledFuture<?>> callableTaskDecorator;
    private Function<Callable<?>, Callable<?>> callableDecorator;

    @Override
    @SuppressWarnings("unchecked")
    public ScheduledExecutorService get() {
        DecoratingExecutorService decoratingExecutorService = new DecoratingExecutorService(corePoolSize, threadFactory, rejectedExecutionHandler);
        decoratingExecutorService.setCallableDecorator((Function) callableDecorator);
        decoratingExecutorService.setCallableTaskDecorator((BiFunction) callableTaskDecorator);
        return decoratingExecutorService;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    @SuppressWarnings("unchecked")
    public <ResultT> void setCallableDecorator(Function<Callable<ResultT>, Callable<ResultT>> callableDecorator) {
        this.callableDecorator = (Function) callableDecorator;
    }

    public void setCallableTaskDecorator(BiFunction<Callable<?>, RunnableScheduledFuture<?>, RunnableScheduledFuture<?>> callableTaskDecorator) {
        this.callableTaskDecorator = callableTaskDecorator;
    }
}

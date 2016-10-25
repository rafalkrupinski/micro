package com.hashnot.u.async.executor;

import java.util.concurrent.*;

/**
 * Stripped down version of {@link ExecutorService} with only single method returning {@link CompletableFuture} instead of {@link Future}.
 *
 * @author Rafał Krupiński
 * @see ExecutorService
 */
public interface Executor2 {

    /**
     * Submits a value-returning task for execution and returns a
     * CompletableFuture representing the pending results of the task.
     *
     * @param task      the task to submit
     * @param <ResultT> the type of the task's result
     * @return a CompletableFuture representing pending completion of the task
     * @throws RejectedExecutionException if the task cannot be
     *                                    scheduled for execution
     * @throws NullPointerException       if the task is null
     * @see ExecutorService#submit(Callable)
     */
    <ResultT> CompletableFuture<ResultT> accept(Callable<ResultT> task);
}

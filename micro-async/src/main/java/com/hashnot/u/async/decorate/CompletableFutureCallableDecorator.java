package com.hashnot.u.async.decorate;

import com.hashnot.u.async.DelegatingRunnableScheduledFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.function.BiFunction;

/**
 * @author Rafał Krupiński
 */
public class CompletableFutureCallableDecorator<ResultT> implements BiFunction<Callable<ResultT>, RunnableScheduledFuture<ResultT>, RunnableScheduledFuture<ResultT>> {
    @Override
    public RunnableScheduledFuture<ResultT> apply(Callable<ResultT> callable, RunnableScheduledFuture<ResultT> task) {
        return new Task<>(task);
    }

    public static class Task<ResultT> extends DelegatingRunnableScheduledFuture<ResultT> {
        private CompletableFuture<ResultT> result;

        public Task(RunnableScheduledFuture<ResultT> runnableScheduledFuture) {
            super(runnableScheduledFuture);
            result = new CompletableFuture<>();
        }

        @Override
        public void run() {
            super.run();
            try {
                ResultT value = get();
                result.complete(value);
            } catch (InterruptedException e) {
                result.completeExceptionally(e);
            } catch (ExecutionException e) {
                result.completeExceptionally(e.getCause());
            }
        }

        public CompletableFuture<ResultT> getCompletableFuture() {
            return result;
        }

    }
}

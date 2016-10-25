package com.hashnot.u.async.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Rafał Krupiński
 */
public class RunnableExecutor2 implements Executor2 {
    final private static Logger log = LoggerFactory.getLogger(RunnableExecutor2.class);
    private Executor executor;

    public RunnableExecutor2(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <ResultT> CompletableFuture<ResultT> accept(Callable<ResultT> task) {
        CompletableFuture<ResultT> result = new CompletableFuture<>();
        executor.execute(new Task<>(task, result));
        return result;
    }

    private static class Task<ResultT> implements Runnable {
        private Callable<ResultT> callable;
        private CompletableFuture<ResultT> future;

        private Task(Callable<ResultT> callable, CompletableFuture<ResultT> future) {
            this.callable = callable;
            this.future = future;
        }

        @Override
        public void run() {
            try {
                ResultT result = callable.call();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }
    }
}

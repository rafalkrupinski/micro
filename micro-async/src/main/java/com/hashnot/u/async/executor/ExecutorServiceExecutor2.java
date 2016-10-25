package com.hashnot.u.async.executor;

import com.hashnot.u.async.decorate.CompletableFutureCallableDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author Rafał Krupiński
 */
public class ExecutorServiceExecutor2 implements Executor2 {
    final private static Logger log = LoggerFactory.getLogger(ExecutorServiceExecutor2.class);
    private ExecutorService executorService;

    public ExecutorServiceExecutor2(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public <ResultT> CompletableFuture<ResultT> accept(Callable<ResultT> task) {
        Future<ResultT> future = executorService.submit(task);
        if (future instanceof CompletableFutureCallableDecorator.Task) {
            log.debug("Getting existing CompletableFuture");
            return ((CompletableFutureCallableDecorator.Task) future).getCompletableFuture();
        } else {
            log.debug("Using ExecutorService to get the future result");
            CompletableFuture<ResultT> completableFuture = new CompletableFuture<>();
            executorService.submit(new Task<>(future, completableFuture));
            return completableFuture;
        }
    }

    private static class Task<ResultT> implements Callable<ResultT> {
        private final Future<ResultT> future;
        private final CompletableFuture<ResultT> completableFuture;

        public Task(Future<ResultT> future, CompletableFuture<ResultT> completableFuture) {
            this.future = future;
            this.completableFuture = completableFuture;
        }

        @Override
        public ResultT call() throws Exception {
            try {
                ResultT result = future.get();
                completableFuture.complete(result);
                return result;
            } catch (ExecutionException x) {
                completableFuture.completeExceptionally(x.getCause());
                return null;
            }
        }
    }
}

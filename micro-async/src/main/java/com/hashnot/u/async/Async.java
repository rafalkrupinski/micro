package com.hashnot.u.async;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * @author Rafał Krupiński
 */
public class Async {
    public static <T> CompletableFuture<T> call(Callable<T> call, Executor executor) {
        CompletableFuture<T> future = new CompletableFuture<>();
        call(call, executor, (r, t) -> handleResult(future, r, t));
        return future;
    }

    protected static <T> void handleResult(CompletableFuture<T> future, T result, Throwable throwable) {
        if (throwable == null) {
            future.complete(result);
        } else {
            future.completeExceptionally(throwable);
        }
    }

    public static <T> void call(Callable<T> call, Executor executor, BiConsumer<T, Throwable> resultHandler) {
        executor.execute(new CallableWrapper<>(call, resultHandler, ___ASYNC_CALL___()));
    }

    static class CallableWrapper<R> implements Runnable {
        final BiConsumer<R, Throwable> resultHandler;
        final StackTraceElement[] stackTrace;
        final Callable<R> function;

        public CallableWrapper(Callable<R> function, BiConsumer<R, Throwable> resultHandler, StackTraceElement[] stackTrace) {
            this.function = function;
            this.resultHandler = resultHandler;
            this.stackTrace = stackTrace;
        }

        @Override
        public void run() {
            try {
                R result = function.call();
                resultHandler.accept(result, null);
            } catch (Throwable e) {
                updateStackTrace(e, stackTrace);
                resultHandler.accept(null, e);
            }
        }
    }

    protected static StackTraceElement[] ___ASYNC_CALL___() {
        return new Exception().getStackTrace();
    }

    protected static void updateStackTrace(Throwable t, StackTraceElement[] asyncTrace) {
        StackTraceElement[] syncTrace = t.getStackTrace();
        StackTraceElement[] combinedTrace = new StackTraceElement[syncTrace.length + asyncTrace.length];
        System.arraycopy(syncTrace, 0, combinedTrace, 0, syncTrace.length);
        System.arraycopy(asyncTrace, 0, combinedTrace, syncTrace.length, asyncTrace.length);
        t.setStackTrace(combinedTrace);
    }
}

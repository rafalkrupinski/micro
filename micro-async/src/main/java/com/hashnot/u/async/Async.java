package com.hashnot.u.async;

import ___ASYNC_._CALL_;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

/**
 * @author Rafał Krupiński
 */
public class Async {
    final private static Logger log = LoggerFactory.getLogger(Async.class);

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
        executor.execute(new CallableWrapper<>(Thread.currentThread().getName(), call, resultHandler, _CALL_._BORDER___()));
    }

    private static class CallableWrapper<R> implements Runnable {
        final String callerThreadName;
        final BiConsumer<R, Throwable> resultHandler;
        final StackTraceElement[] stackTrace;
        final Callable<R> function;

        CallableWrapper(String callerThreadName, Callable<R> function, BiConsumer<R, Throwable> resultHandler, StackTraceElement[] stackTrace) {
            this.callerThreadName = callerThreadName;
            this.function = function;
            this.resultHandler = resultHandler;
            this.stackTrace = stackTrace;
        }

        @Override
        public void run() {
            log.debug("scheduled by {}", callerThreadName);
            try {
                R result = function.call();
                log.debug("done");
                resultHandler.accept(result, null);
            } catch (Throwable e) {
                log.debug("{} {}", e.getClass().getName(), e.getMessage());
                updateStackTrace(e, stackTrace);
                resultHandler.accept(null, e);
            }
        }
    }

    protected static void updateStackTrace(Throwable t, StackTraceElement[] asyncTrace) {
        StackTraceElement[] syncTrace = t.getStackTrace();
        StackTraceElement[] combinedTrace = new StackTraceElement[syncTrace.length + asyncTrace.length];
        System.arraycopy(syncTrace, 0, combinedTrace, 0, syncTrace.length);
        System.arraycopy(asyncTrace, 0, combinedTrace, syncTrace.length, asyncTrace.length);
        t.setStackTrace(combinedTrace);
    }
}

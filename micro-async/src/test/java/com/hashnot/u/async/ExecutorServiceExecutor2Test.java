package com.hashnot.u.async;

import com.hashnot.u.async.decorate.CompletableFutureCallableDecorator;
import com.hashnot.u.async.decorate.StackTraceCallableDecorator;
import com.hashnot.u.async.executor.Executor2;
import com.hashnot.u.async.executor.ExecutorServiceExecutor2;
import com.hashnot.u.async.executor.ExecutorFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * @author Rafał Krupiński
 */
public class ExecutorServiceExecutor2Test {
    final private static Logger log = LoggerFactory.getLogger(ExecutorServiceExecutor2Test.class);

    @Test
    public void testSimple() throws Exception {
        Executor2 executor = new ExecutorServiceExecutor2(new ExecutorFactory().get());
        Object o = new Object();
        CompletableFuture<Object> future = executor.accept(() -> {
            log.info("call");
            return o;
        });
        assertEquals(o, future.get());
    }

    @Test
    public void testSimpleCompletable() throws Exception {
        ExecutorFactory factory = new ExecutorFactory();
        factory.setCallableTaskDecorator(new CompletableFutureCallableDecorator());
        Executor2 executor = new ExecutorServiceExecutor2(factory.get());
        Object o = new Object();
        CompletableFuture<Object> future = executor.accept(() -> {
            log.info("call");
            return o;
        });
        assertEquals(o, future.get());
    }

    @Test(expected = Exception.class)
    public void testThrow() throws Exception {
        ExecutorFactory factory = new ExecutorFactory();
        factory.setCallableTaskDecorator(new CompletableFutureCallableDecorator());
        factory.setCallableDecorator(new StackTraceCallableDecorator());
        Executor2 executor = new ExecutorServiceExecutor2(factory.get());
        CompletableFuture<Object> future = executor.accept(new ThrowingCallable());
        try {
            future.get();
        } catch (ExecutionException e) {
            log.error("", e);
            throw e;
        }
    }

    @Test
    public void testNested() {
        ExecutorFactory factory = new ExecutorFactory();
        factory.setCorePoolSize(5);
        factory.setCallableTaskDecorator(new CompletableFutureCallableDecorator());
        factory.setCallableDecorator(new StackTraceCallableDecorator());
        Executor2 executor = new ExecutorServiceExecutor2(factory.get());

        CompletableFuture<Object> future1 = executor.accept(() -> {
            log.debug("inside 1");
            CompletableFuture<Object> future = executor.accept(Object::new);
            future.exceptionally(e -> {
                log.warn("exceptionally", e);
                return e;
            });
            return future.get();
        });

        try {
            future1.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Error",e);
        }
    }

    private class ThrowingCallable implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            log.info("throwable.call");
            return doThrowException();
        }

        private Object doThrowException() throws Exception {
            throw new Exception("myexception");
        }
    }
}

package com.hashnot.u.async.threadlocal;

import com.hashnot.u.async.decorate.CompletableFutureCallableDecorator;
import com.hashnot.u.async.executor.Executor2;
import com.hashnot.u.async.executor.ExecutorServiceExecutor2;
import com.hashnot.u.async.executor.ExecutorFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author Rafał Krupiński
 */
public class ThreadLocalPreservingDecoratorTest {
    final private static Logger log = LoggerFactory.getLogger(ThreadLocalPreservingDecoratorTest.class);
    static ThreadLocal<String> a = new ThreadLocal<>();
    static {
        ThreadLocalPreserver.register(a);
    }
    @Test
    public void apply() throws Exception {
        ExecutorFactory factory = new ExecutorFactory();
        factory.setCallableDecorator(new ThreadLocalPreservingDecorator());
        factory.setCallableTaskDecorator(new CompletableFutureCallableDecorator());
        Executor2 executor = new ExecutorServiceExecutor2(factory.get());
        a.set("hello");
        CompletableFuture<String> future = executor.accept(() -> {
            log.info("inside");
            return a.get();
        });
        Assert.assertEquals("hello", future.get());
    }

}
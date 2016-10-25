package com.hashnot.u.async.decorate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author Rafał Krupiński
 */
public class StackTraceCallableDecoratorTest {
    final private static Logger log = LoggerFactory.getLogger(StackTraceCallableDecoratorTest.class);

    @Test
    public void call() throws Exception {
        class Thrower implements Callable<Void> {
            @Override
            public Void call() throws Exception {
                throw new Exception();
            }
        }
        Thrower thrower = new Thrower();
        Function<Callable, Callable> decorator = new StackTraceCallableDecorator();
        Thread thread = new Thread(() -> {
            try {
                decorator.apply(thrower).call();
            } catch (Exception e) {
                log.error("", e);
            }
        });
        thread.start();
        thread.join();
    }

}
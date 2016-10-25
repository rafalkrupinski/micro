package com.hashnot.u.async.threadlocal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * @author Rafał Krupiński
 */
public class ThreadLocalPreservingDecorator<ResultT> implements Function<Callable<ResultT>, Callable<ResultT>> {
    @Override
    public Callable<ResultT> apply(Callable<ResultT> runnable) {
        return new CallableTask<>(runnable, ThreadLocalPreserver.getState());
    }

    private static class CallableTask<ResultT2> implements Callable<ResultT2> {
        final private static Logger log = LoggerFactory.getLogger(CallableTask.class);

        private Callable<ResultT2> callable;
        private ThreadLocalPreserver.State state;
        final private String callerThreadName;

        CallableTask(Callable<ResultT2> callable, ThreadLocalPreserver.State state) {
            this.callable = callable;
            this.state = state;
            callerThreadName = Thread.currentThread().getName();
        }

        @Override
        public ResultT2 call() throws Exception {
            log.debug("Setting state from {}", callerThreadName);
            ThreadLocalPreserver.setState(state);
            try {
                return callable.call();
            } finally {
                ThreadLocalPreserver.clean();
            }
        }
    }
}

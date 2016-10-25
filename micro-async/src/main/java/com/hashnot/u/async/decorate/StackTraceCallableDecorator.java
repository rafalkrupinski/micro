package com.hashnot.u.async.decorate;

import com.hashnot.u.async.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.function.Function;

import static com.hashnot.u.async.ExceptionUtil.truncateAndPrepend;
import static com.hashnot.u.async.ExceptionUtil.updateStackTrace;

/**
 * @author Rafał Krupiński
 */
public class StackTraceCallableDecorator<ResultT> implements Function<Callable<ResultT>, Callable<ResultT>> {
    final private static Logger log = LoggerFactory.getLogger(StackTraceCallableDecorator.class);
    final private static StackTraceElement PREPEND = new StackTraceElement("___ASYNC_CALL_BOUNDARY___", "___ASYNC_ABOVE___", null, -1);

    @Override
    public Callable<ResultT> apply(Callable<ResultT> callable) {
        return new CallableWrapper<>(callable);
    }

    private static class CallableWrapper<T> implements Callable<T> {
        final private StackTraceElement[] stackTrace;
        private Callable<T> callable;

        CallableWrapper(Callable<T> callable) {
            this.callable = callable;
            log.debug("taking stack trace snapshot from {}", Thread.currentThread().getName());
            stackTrace = ExceptionUtil.createStackTrace();
        }

        @Override
        public T call() throws Exception {
            try {
                return callable.call();
            } catch (Exception x) {
                StackTraceElement[] effectiveStackTrace = truncateAndPrepend(stackTrace, 2, PREPEND);
                updateStackTrace(x, effectiveStackTrace);
                throw x;
            }
        }

    }
}

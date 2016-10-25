package com.hashnot.u.async.decorate;

import com.hashnot.u.ratelimit.IRateLimiter;

import java.util.concurrent.Callable;

/**
 * @author Rafał Krupiński
 */
public class RateLimitingDecorator<ResultT> implements CallableDecorator<ResultT> {
    final private IRateLimiter rateLimiter;

    public RateLimitingDecorator(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Callable<ResultT> apply(Callable<ResultT> callable) {
        return () -> {
            rateLimiter.acquire(1);
            return callable.call();
        };
    }
}

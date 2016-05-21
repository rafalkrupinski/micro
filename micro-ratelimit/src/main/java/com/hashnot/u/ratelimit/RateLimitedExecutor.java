package com.hashnot.u.ratelimit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Rafał Krupiński
 */
public class RateLimitedExecutor implements Executor {

    private final Executor backend;
    private final IRateLimiter rateLimiter;
    private Function<Runnable, Runnable> decorateFunc;

    private final int timeout;
    private final TimeUnit timeUnit;
    private BlockingQueue<Runnable> tasks = new LinkedBlockingDeque<>();
    private volatile boolean running = true;

    public RateLimitedExecutor(Executor backend, IRateLimiter rateLimiter) {
        this(backend, rateLimiter, 100, TimeUnit.MILLISECONDS);
    }

    public RateLimitedExecutor(Executor backend, IRateLimiter rateLimiter, int timeout, TimeUnit timeUnit) {
        this.backend = backend;
        this.rateLimiter = rateLimiter;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public void start() {
        backend.execute(() -> {
            while (running) {
                try {
                    Runnable task = tasks.poll(timeout, timeUnit);
                    if (task == null)
                        continue;
                    rateLimiter.acquire(1);
                    backend.execute(task);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    @Override
    public void execute(Runnable command) {
        tasks.add(decorate(command));
    }

    public void shutdown() {
        running = false;
    }

    private Runnable decorate(Runnable runnable) {
        return decorateFunc != null ? decorateFunc.apply(runnable) : runnable;
    }

    public void setDecorateFunc(Function<Runnable, Runnable> decorateFunc) {
        this.decorateFunc = decorateFunc;
    }
}

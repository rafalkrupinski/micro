package com.hashnot.u.ratelimit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author Rafał Krupiński
 */
public class RateLimitedExecutor implements Executor {

    private BlockingQueue<Runnable> tasks = new LinkedBlockingDeque<>();
    private volatile boolean running = true;

    public RateLimitedExecutor(Executor backend, IRateLimiter rateLimiter) {
        this(backend, rateLimiter, 100, TimeUnit.MILLISECONDS);
    }

    public RateLimitedExecutor(Executor backend, IRateLimiter rateLimiter, int timeout, TimeUnit timeUnit) {
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
        tasks.add(command);
    }

    public void shutdown() {
        running = false;
    }
}

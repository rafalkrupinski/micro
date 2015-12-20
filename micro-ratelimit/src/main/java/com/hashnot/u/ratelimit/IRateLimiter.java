package com.hashnot.u.ratelimit;

import java.time.Duration;

/**
 * @author Rafał Krupiński
 */
public interface IRateLimiter {
    Duration acquire(int permits) throws InterruptedException;

    boolean tryAcquire(int permits);
}

package com.hashnot.u.ratelimit;

import com.hashnot.u.compare.Comparables;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.time.Duration.ZERO;

/**
 * @author Rafał Krupiński
 */
public class RateLimiter implements IRateLimiter {
    public static final int NANO_PER_MILLI = 1_000_000;
    final private Clock clock;

    final private Map<RateLimit, RateLimitState> state = new HashMap<>();

    public RateLimiter(RateLimit rateLimit) {
        this(Collections.singleton(rateLimit), Clock.systemDefaultZone());
    }

    public RateLimiter(Collection<RateLimit> rateLimits) {
        this(rateLimits, Clock.systemDefaultZone());
    }

    public RateLimiter(Collection<RateLimit> rateLimits, Clock clock) {
        for (RateLimit rateLimit : rateLimits)
            state.put(rateLimit, new RateLimitState(rateLimit));
        this.clock = clock;
    }

    @Override
    public Duration acquire(int acquire) throws InterruptedException {
        Instant now = clock.instant();

        synchronized (this) {
            Instant actual = clock.instant();
            Duration sleep = Duration.ofSeconds(Long.MIN_VALUE);

            for (Map.Entry<RateLimit, RateLimitState> e : state.entrySet()) {
                RateLimitState rateLimitState = e.getValue();
                sleep = Comparables.max(sleep, rateLimitState.waitTimeForPermit(actual, acquire));
                rateLimitState.add(actual, acquire);
            }

            if (sleep.compareTo(ZERO) > 0)
                Thread.sleep(sleep.toMillis(), sleep.getNano() % NANO_PER_MILLI);

            return sleep.plus(Duration.between(now, actual));
        }
    }

    @Override
    public synchronized boolean tryAcquire(int acquire) {
        synchronized (this) {
            Instant actual = clock.instant();
            Duration sleep = Duration.ofSeconds(Long.MIN_VALUE);

            for (Map.Entry<RateLimit, RateLimitState> e : state.entrySet()) {
                sleep = Comparables.max(sleep, e.getValue().waitTimeForPermit(actual, acquire));
            }

            if (sleep.compareTo(ZERO) > 0)
                return false;

            for (Map.Entry<RateLimit, RateLimitState> e : state.entrySet()) {
                e.getValue().add(actual, acquire);
            }

            return true;
        }
    }

}

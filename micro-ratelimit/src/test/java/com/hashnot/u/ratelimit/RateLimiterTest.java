package com.hashnot.u.ratelimit;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

/**
 * @author Rafał Krupiński
 */
public class RateLimiterTest {

    @Test
    public void testAcquire() throws Exception {
        Clock clock = Mockito.mock(Clock.class);
        IRateLimiter limiter = new RateLimiter(Collections.singleton(new RateLimit(1, Duration.ofSeconds(1))), clock);
        Mockito.when(clock.instant()).thenReturn(Instant.ofEpochSecond(0), Instant.ofEpochSecond(0, 100));

        limiter.acquire(1);
        limiter.acquire(1);
    }

    @Test
    public void testTryAcquire() throws Exception {
        Clock clock = Mockito.mock(Clock.class);
        IRateLimiter limiter = new RateLimiter(Collections.singleton(new RateLimit(1, Duration.ofSeconds(1))), clock);
        Mockito.when(clock.instant()).thenReturn(Instant.ofEpochSecond(0), Instant.ofEpochSecond(0, 100));

        limiter.tryAcquire(1);

        boolean b = limiter.tryAcquire(1);

        Assert.assertFalse(b);

    }
}

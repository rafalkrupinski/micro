package com.hashnot.u.ratelimit;

import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import static java.time.Duration.ZERO;

/**
 * @author Rafał Krupiński
 */
class RateLimitState {
    final private Deque<Pair<Instant, Integer>> permits = new LinkedList<>();

    private int acquired;

    private RateLimit rateLimit;

    RateLimitState(RateLimit rateLimit) {
        this.rateLimit = rateLimit;
    }

    public void add(Instant time, int permit) {
        permits.add(new Pair<>(time, permit));
        acquired += permit;
    }

    public Duration waitTimeForPermit(Instant now, int acquire) {
        if (acquired + acquire <= rateLimit.getLimit())
            return ZERO;

        Instant lowLimit = now.minus(rateLimit.getTimespan());
        int required = rateLimit.getLimit() - acquire;

        //permits acquired during the timespan
        int acquired = 0;

        Instant newestPermitRequiredToExpire = null;

        for (Pair<Instant, Integer> e : this.permits) {
            Instant timestamp = e.getKey();
            if (timestamp.isBefore(lowLimit)) {
                cleanExpiredPermits(e);
                break;
            }

            acquired += e.getValue();
            if (newestPermitRequiredToExpire == null && acquired > required)
                newestPermitRequiredToExpire = timestamp;
        }

        return newestPermitRequiredToExpire == null
                ? ZERO
                : rateLimit.getTimespan().plus(Duration.between(now, newestPermitRequiredToExpire));

    }

    private void cleanExpiredPermits(Pair<Instant, Integer> limit) {
        Iterator<Pair<Instant, Integer>> iter = permits.descendingIterator();
        while (iter.hasNext()) {
            Pair<Instant, Integer> permit = iter.next();
            remove(iter, permit);
            if (permit == limit)
                break;
        }
    }

    private void remove(Iterator<Pair<Instant, Integer>> iter, Pair<Instant, Integer> permit) {
        iter.remove();
        acquired -= permit.getValue();
        assert acquired >= 0;
    }

}

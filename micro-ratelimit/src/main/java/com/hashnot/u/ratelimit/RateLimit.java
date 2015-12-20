package com.hashnot.u.ratelimit;

import java.time.Duration;

/**
 * @author Rafał Krupiński
 */
public class RateLimit {
    final private int limit;
    final private Duration timespan;

    final transient int hashcode;

    public RateLimit(int limit, Duration timespan) {
        this.limit = limit;
        this.timespan = timespan;

        hashcode = limit * 31 + timespan.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && o instanceof RateLimit && equals((RateLimit) o);
    }

    protected boolean equals(RateLimit other) {
        return limit == other.limit && timespan.equals(other.timespan);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    public int getLimit() {
        return limit;
    }

    public Duration getTimespan() {
        return timespan;
    }
}

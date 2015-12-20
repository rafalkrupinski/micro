package com.hashnot.u.range;

import java.util.Comparator;

/**
 * overlapping ranges are considered equal.
 *
 * @author Rafał Krupiński
 */
public class OverlappingRangeComparator<T extends Comparable<T>> implements Comparator<Range<T>> {
    @Override
    public int compare(Range<T> a, Range<T> b) {
        if (a.getBottom().compareTo(b.getTop()) >= 0)
            return 1;
        if (a.getTop().compareTo(b.getBottom()) < 0)
            return -1;
        return 0; // overlapping ranges are considered equal.
    }
}

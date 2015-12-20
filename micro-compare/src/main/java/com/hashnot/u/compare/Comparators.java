package com.hashnot.u.compare;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Rafał Krupiński
 */
public class Comparators {
    public static <T, ComparableType extends Comparable<ComparableType>> Comparator<T> comparator(Function<T, ComparableType> toComparable) {
        return (a, b) -> toComparable.apply(a).compareTo(toComparable.apply(b));
    }
}

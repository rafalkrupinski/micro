package com.hashnot.u.compare;

import java.util.Comparator;

/**
 * @author Rafał Krupiński
 */
public interface CompareOp<T> {
    /**
     * Allows parameters to be null
     */
    boolean eq(T o1, T o2);

    /**
     * Allows parameters to be null
     */
    boolean ne(T o1, T o2);

    boolean lt(T o1, T o2);

    boolean lte(T o1, T o2);

    boolean gt(T o1, T o2);

    /**
     * @return o1 >= o2
     */
    boolean gte(T o1, T o2);

    T max(T o1, T... rest);

    T min(T o1, T... rest);

    static <T> CompareOp<T> from(Comparator<T> from) {
        return new ComparatorOperator<>(from);
    }

    class ComparatorOperator<T> implements CompareOp<T> {
        final private Comparator<T> cmp;

        public ComparatorOperator(Comparator<T> cmp) {
            this.cmp = cmp;
        }

        /**
         * Allows parameters to be null
         */
        public boolean eq(T o1, T o2) {
            return o1 == o2 || o1 != null && o2 != null && cmp.compare(o1, o2) == 0;
        }

        /**
         * Allows parameters to be null
         */
        public boolean ne(T o1, T o2) {
            return !eq(o1, o2);
        }

        public boolean lt(T o1, T o2) {
            return cmp.compare(o1, o2) < 0;
        }

        public boolean lte(T o1, T o2) {
            return cmp.compare(o1, o2) <= 0;
        }

        public boolean gt(T o1, T o2) {
            return cmp.compare(o1, o2) > 0;
        }

        /**
         * @return o1 >= o2
         */
        public boolean gte(T o1, T o2) {
            return cmp.compare(o1, o2) >= 0;
        }

        @Override
        public T max(T o1, T... rest) {
            T result = o1;
            for (T o : rest) {
                if (gt(o, result))
                    result = o;
            }
            return result;
        }

        @Override
        public T min(T o1, T... rest) {
            T result = o1;
            for (T o : rest) {
                if (lt(o, result))
                    result = o;
            }
            return result;
        }
    }

    CompareOp NATURAL = from((Comparator<?>) Comparator.naturalOrder());

    static <T extends Comparable<T>> CompareOp<T> natural() {
        return NATURAL;
    }
}

package com.hashnot.u.random;

import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

public class WeightedRandom<T> {
    private final Random rnd;
    private final TreeMap<Range<Double>, T> ranges = new TreeMap<>();

    public WeightedRandom(Map<T, Double> weightedItems) {
        this(weightedItems, new Random());
    }

    public WeightedRandom(Map<T, Double> weightedItems, Random random) {
        this(weightedItems.entrySet(), Map.Entry::getKey, Map.Entry::getValue, random);
    }

    public <E> WeightedRandom(Iterable<E> weightedItems, Function<E, T> itemFunc, Function<E, Double> weightFunc) {
        this(weightedItems, itemFunc, weightFunc, new Random());
    }

    public <E> WeightedRandom(Iterable<E> weightedItems, Function<E, T> itemFunc, Function<E, Double> weightFunc, Random random) {
        Double bottom = 0d;

        Map<T, Double> normalized = normalize(weightedItems, weightFunc, itemFunc);

        for (Map.Entry<T, Double> wi : normalized.entrySet()) {
            double weight = wi.getValue();
            if (weight > 0) {
                Double top = bottom + weight;
                Range<Double> r = Range.closedOpen(bottom, top);
                if (ranges.containsKey(r)) {
                    T other = ranges.get(r);
                    throw new RuntimeException(String.format("Range %s conflicts with range %s", r, other));
                }
                ranges.put(r, wi.getKey());
                bottom = top;
            }
        }
        rnd = random;
    }

    protected <E> Map<T, Double> normalize(Iterable<E> weightedItems, Function<E, Double> weightFunc, Function<E, T> elemFunc) {
        Map<T, Double> normalized = new HashMap<>();

        double sum = 0d;

        for (E e : weightedItems) {
            Double weight = weightFunc.apply(e);
            assert weight != null;
            sum += weight;
        }

        for (E e : weightedItems) {
            normalized.put(elemFunc.apply(e), weightFunc.apply(e) / sum);
        }
        return normalized;
    }

    public T next() {
        double key = rnd.nextDouble();

        Range<Double> range = Range.singleton(key);
        return ranges.get(range);
    }
}

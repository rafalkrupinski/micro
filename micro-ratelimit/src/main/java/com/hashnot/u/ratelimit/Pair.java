package com.hashnot.u.ratelimit;

/**
 * @author Rafał Krupiński
 */
class Pair<K, V> {
    final private K key;
    final private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

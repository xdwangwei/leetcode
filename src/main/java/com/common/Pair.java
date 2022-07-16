package com.common;

/**
 * @author wangwei
 * @date 2022/5/10 11:45
 * @description: Pair
 */
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }
}

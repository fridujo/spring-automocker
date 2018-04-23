package com.github.fridujo.automocker.utils;

import java.util.HashMap;
import java.util.Map;

public class Maps {

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> build(Class<K> keyClas, Object... keysAndValues) {
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("keysAndValues array must be even");
        }
        Map<K, V> map = new HashMap<>();
        K key = null;
        for (int i = 0; i < keysAndValues.length; i++) {
            if (i % 2 == 0) {
                key = keyClas.cast(keysAndValues[i]);
            } else {
                map.put(key, (V) keysAndValues[i]);
            }
        }
        return map;
    }
}

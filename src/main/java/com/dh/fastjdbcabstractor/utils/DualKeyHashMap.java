package com.dh.fastjdbcabstractor.utils;

import java.util.HashMap;

/**
 * Simple implementation of a dual key hash map
 * 
 * @author AlucardDH
 * 
 * @param <K1> First key
 * @param <K2> Second key
 * @param <V> Value
 */
public class DualKeyHashMap<K1,K2,V> extends HashMap<ComposedKey<K1,K2>,V> {
    
    public V get(K1 key1, K2 key2) {
        return get(new ComposedKey<>(key1,key2));
    }    
    
    public void put(K1 key1, K2 key2, V value) {
        put(new ComposedKey<>(key1,key2), value);
    }
    
    public V removeWithKey(K1 key1, K2 key2) {
        return remove(new ComposedKey<>(key1,key2));
    }
          
}

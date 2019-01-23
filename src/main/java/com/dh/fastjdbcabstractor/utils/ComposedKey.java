/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.utils;

import java.util.Objects;

/**
 * Composite key for the @see com.dh.fastjdbcabstractor.utils.DualKeyHashMap
 * 
 * @author AlucardDH
 */
public class ComposedKey<K1, K2> {

    private K1 key1;
    private K2 key2;

    public ComposedKey(K1 key1, K2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.key1);
        hash = 53 * hash + Objects.hashCode(this.key2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComposedKey<?, ?> other = (ComposedKey<?, ?>) obj;
        if (!Objects.equals(this.key1, other.key1)) {
            return false;
        }
        if (!Objects.equals(this.key2, other.key2)) {
            return false;
        }
        return true;
    }

    public K1 getKey1() {
        return key1;
    }

    public K2 getKey2() {
        return key2;
    }

}

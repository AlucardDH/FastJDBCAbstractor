/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.utils;

import java.util.Collection;

/**
 *
 * @author frup58637
 */
public class Concat {
    
    public static StringBuilder concat(StringBuilder sb,Collection<String> values,String separator,String defaultValue) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        if(values==null || values.isEmpty()) {
            if(defaultValue!=null) {
                sb.append(defaultValue);
            }
        } else {
            boolean first = true;
            for(String value : values) {
                if(!first) {
                    sb.append(separator);
                } else {
                    first = false;
                }
                sb.append(value);
            }
        }
        return sb;
    }
    
}

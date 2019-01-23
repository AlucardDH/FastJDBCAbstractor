/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.converter;

/**
 *
 * @author frup58637
 */
public class MissingConverterException extends Exception {
    
    private final int sqlType;
    private final Class targetClass;

    public MissingConverterException(int sqlType,Class targetClass) {
        super("Missing converter : "+sqlType+" to "+targetClass.getSimpleName());
        this.sqlType = sqlType;
        this.targetClass = targetClass;
    }

    public int getSqlType() {
        return sqlType;
    }

    public Class getTargetClass() {
        return targetClass;
    }
    
       
}

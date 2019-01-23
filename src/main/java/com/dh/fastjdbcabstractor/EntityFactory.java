/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor;

import com.dh.fastjdbcabstractor.converter.ConverterManager;
import com.dh.fastjdbcabstractor.converter.MissingConverterException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to build entities from a ResultSet
 * 
 * Each column of the ResultSet is mapped with a field of the Entity using its name
 * 
 * @author AlucardDH
 * @param <T> Entities class
 */
public class EntityFactory<T> {
    
    private final Class<T> clazz;
    private final HashMap<String,Field> fields = new HashMap<>();

    /**
     * 
     * @param clazz Entities class
     */
    public EntityFactory(Class<T> clazz) {
        this.clazz = clazz;
        Field[] fs = clazz.getFields();
        for(Field f : fs) {
            fields.put(f.getName(),f);
        }
    }
    
    public T newEntity() throws InstantiationException, IllegalAccessException {
        return (T) clazz.newInstance();
    }
    
    /**
     * 
     * @param entity
     * @param row
     * @param columnMapping
     * @throws MissingConverterException when there is no converter from the colmun type to the target class
     * @throws SQLException 
     */
    public void fill(T entity, ResultSet row, Map<String,Integer> columnMapping) throws MissingConverterException, SQLException {
        for(Field f : fields.values()) {
            Integer column = columnMapping.get(f.getName());
            if(column!=null) {
                try {
                    f.set(entity, ConverterManager.getDefaultInstance().convert(row, column, f.getType()));
                } catch(IllegalAccessException iae) {
                    // field is not public! > ignored
                }
            }
        }
    }

}

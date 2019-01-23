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
 *
 * @author frup58637
 */
public class EntityFactory<T> {
    
    private final Class<T> clazz;
    private final HashMap<String,Field> fields = new HashMap<>();

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
    
    public void fill(T entity, ResultSet row, Map<String,Integer> columnMapping) throws IllegalArgumentException, IllegalAccessException, MissingConverterException, SQLException {
        for(Field f : fields.values()) {
            Integer column = columnMapping.get(f.getName());
            if(column!=null) {
                try {
                    f.set(entity, ConverterManager.getDefaultInstance().convert(row, column, f.getType()));
                } catch(IllegalAccessException iae) {
                    
                }
            }
        }
    }

}

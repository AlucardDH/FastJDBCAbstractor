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
    
    private final String parentField;
    private final Class<T> clazz;
    private final HashMap<String,Field> fields = new HashMap<>();
    private final HashMap<Field,EntityFactory<?>> subEntityFactories = new HashMap<>();

    /**
     * 
     * @param clazz Entities class
     */
    public EntityFactory(Class<T> clazz) {
        this(null,clazz);
    }

    public EntityFactory(String parentField, Class<T> clazz) {
        this.parentField = parentField;
        this.clazz = clazz;
        Field[] fs = clazz.getFields();
        String tempParent = parentField==null ? "" : parentField+"_";
        for(Field f : fs) {
            fields.put(tempParent+f.getName(),f);
        }
    }
    
    public T newEntity() throws InstantiationException, IllegalAccessException {
        return (T) clazz.newInstance();
    }
    
    public EntityFactory<?> getSubEntityFactory(Field field) {
        EntityFactory<?> result = subEntityFactories.get(field);
        if(result==null) {
            result = new EntityFactory<>((parentField==null ? "" : parentField+"_")+field.getName(),field.getType());
            subEntityFactories.put(field, result);
        }
        return result;
    }
    
    /**
     * 
     * @param entity
     * @param row
     * @param columnMapping
     * @throws MissingConverterException when there is no converter from the colmun type to the target class
     * @throws SQLException 
     * @throws java.lang.IllegalAccessException 
     * @throws java.lang.InstantiationException 
     */
    public void fill(T entity, ResultSet row, Map<String,Integer> columnMapping) throws MissingConverterException, SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        for(Map.Entry<String,Integer> column : columnMapping.entrySet()) {
            Field f = fields.get(column.getKey());
            if(f!=null) {
                try {
                    f.set(entity, ConverterManager.getDefaultInstance().convert(row, column.getValue(), f.getType()));
                } catch(IllegalAccessException iae) {
                    // field is not public! > ignored
                }
            } else {
                for(Map.Entry<String,Field> field : fields.entrySet()) {
                    if(column.getKey().startsWith(field.getKey()) && field.getValue().get(entity)==null) {
                        EntityFactory ef = getSubEntityFactory(field.getValue());
                        Object o = ef.newEntity();
                        ef.fill(o, row, columnMapping);
                        field.getValue().set(entity, o);
                    }
                }
            }
        }
    }

}

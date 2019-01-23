package com.dh.fastjdbcabstractor.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convert ResultSet column to specified java class
 * 
 * @author AlucardDH
 * @param <T> Target class
 */
public interface TypeConverter<T> {
    
    public T convert(ResultSet resultSet,int column) throws SQLException;
    
}

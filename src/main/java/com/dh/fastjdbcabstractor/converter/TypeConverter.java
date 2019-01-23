/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author frup58637
 */
public interface TypeConverter<T> {
    
    public T convert(ResultSet resultSet,int column) throws SQLException;
    
}

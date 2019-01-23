/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor;

import com.dh.fastjdbcabstractor.converter.MissingConverterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author frup58637
 */
public class FastJDBCAbstractor {
    

    public static <T> List<T>query(Class<T> clazz,Connection connection, String sqlQuery) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, MissingConverterException {
        return query(clazz, connection, sqlQuery, null);
    }
    
    public static <T> List<T>query(Class<T> clazz, Connection connection, String sqlQuery,ParametersSetter parametersSetter) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, MissingConverterException {
        ArrayList<T> result = new ArrayList<>();
        EntityFactory<T> entityFactory = new EntityFactory<>(clazz);
        
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            
            if(parametersSetter!=null) {
                parametersSetter.setParameter(ps);
            }
            
            try(ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metadata = rs.getMetaData();
                HashMap<String,Integer> columnMapping = new HashMap<>();
                for(int column=1;column<=metadata.getColumnCount();column++) {
                    columnMapping.put(metadata.getColumnName(column), column);
                    //System.out.println(column+" : "+metadata.getColumnName(column));
                }
                while(rs.next()) {
                    T entity = entityFactory.newEntity();
                    entityFactory.fill(entity, rs, columnMapping);
                    result.add(entity);
                }
            }
        }
        
        return result;
    }
    
    public interface ParametersSetter {
        public void setParameter(PreparedStatement ps) throws SQLException;
    }
    
}

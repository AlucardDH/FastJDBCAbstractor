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
 * This class expose simplified method to execute sql queries and map the result to a pojo entity
 * 
 * @author AlucardDH
 */
public class FastJDBCAbstractor {
    

    /**
     * Execute SQL queries without parameters
     * 
     * @param <T> Target class for the result entities
     * @param clazz Target class for the result entities
     * @param connection Connection used to query the DB
     * @param sqlQuery SQL query to execute
     * 
     * @return a List of entities
     * 
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws MissingConverterException 
     */
    public static <T> List<T>query(Class<T> clazz,Connection connection, String sqlQuery) throws SQLException, InstantiationException, IllegalAccessException, MissingConverterException {
        return query(clazz, connection, sqlQuery, null);
    }
    
    /**
     * Execute SQL queries without parameters
     * 
     * @param <T> Target class for the result entities
     * @param clazz Target class for the result entities
     * @param connection Connection used to query the DB
     * @param sqlQuery Parametrized SQL query to execute
     * @param parametersSetter Parameters to set in the query 
     * 
     * @return a List of entities
     * 
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws MissingConverterException 
     */
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
    
    /**
     * Use this interface to set values in a parametrized sql query.
     * It will be executed after the statement is created and before the query is executed.
     */
    public interface ParametersSetter {
        public void setParameter(PreparedStatement ps) throws SQLException;
    }
    
}

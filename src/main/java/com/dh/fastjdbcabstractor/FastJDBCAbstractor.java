package com.dh.fastjdbcabstractor;

import com.dh.fastjdbcabstractor.converter.MissingConverterException;
import com.dh.fastjdbcabstractor.querybuilder.Insert;
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
    
    private static final HashMap<Class<?>,EntityFactory<?>> ENTITY_FACTORIES = new HashMap<>();
    
    public static <T> EntityFactory<T> getEntityFactory(Class<T> clazz) {
        EntityFactory<T> result = (EntityFactory<T>) ENTITY_FACTORIES.get(clazz);
        if(result==null) {
            result = new EntityFactory<>(clazz);
            ENTITY_FACTORIES.put(clazz, result);
        }
        return result;
    }
    

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
    public static <T> List<T>executeQuery(Class<T> clazz,Connection connection, String sqlQuery) throws SQLException, InstantiationException, IllegalAccessException, MissingConverterException {
        return executeQuery(clazz, connection, sqlQuery, null);
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
    public static <T> List<T>executeQuery(Class<T> clazz, Connection connection, String sqlQuery,ParametersSetter parametersSetter) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, MissingConverterException {
        ArrayList<T> result = new ArrayList<>();
        EntityFactory<T> entityFactory = getEntityFactory(clazz);
        
        System.out.println(sqlQuery);
        
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            
            if(parametersSetter!=null) {
                parametersSetter.setParameter(ps);
            }
            try(ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metadata = rs.getMetaData();
                HashMap<String,Integer> columnMapping = new HashMap<>();
                for(int column=1;column<=metadata.getColumnCount();column++) {
                    columnMapping.put(metadata.getColumnLabel(column), column);
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
    
    public static int executeUpdate(Connection connection, String sqlQuery) throws SQLException {
        return executeUpdate(connection, sqlQuery, null);
    }
    
    public static int executeUpdate(Connection connection, String sqlQuery, ParametersSetter parametersSetter) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            
            if(parametersSetter!=null) {
                parametersSetter.setParameter(ps);
            }
            
            return ps.executeUpdate();
        }
    }
    
    public static int insertWIthValues(Connection connection, Insert insert) throws SQLException {
        if(insert==null || !insert.hasValues()) {
            throw new IllegalArgumentException("Insert is null or has no value");
        }
        return executeUpdate(connection, insert.build());
    }
    
    /**
     * Use this interface to set values in a parametrized sql query.
     * It will be executed after the statement is created and before the query is executed.
     */
    public interface ParametersSetter {
        public void setParameter(PreparedStatement ps) throws SQLException;
    }
    
}

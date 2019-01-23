/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.converter;


import com.dh.fastjdbcabstractor.utils.DualKeyHashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author frup58637
 */
public class ConverterManager extends DualKeyHashMap<Integer,Class,TypeConverter>{
    
    
    
    private static final int INSTANCE_DEFAULT = 0;
    
    private static final TypeConverter<String> CONVERTER_STRING = new TypeConverter<String>() {
        @Override
        public String convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getString(column);
        }
    };
    private static final TypeConverter<Integer> CONVERTER_INTEGER = new TypeConverter<Integer>() {
        @Override
        public Integer convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getInt(column);
        }
    };
    private static final TypeConverter<Long> CONVERTER_LONG = new TypeConverter<Long>() {
        @Override
        public Long convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getLong(column);
        }
    };
    private static final TypeConverter<Float> CONVERTER_FLOAT = new TypeConverter<Float>() {
        @Override
        public Float convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getFloat(column);
        }
    };
    private static final TypeConverter<Double> CONVERTER_DOUBLE = new TypeConverter<Double>() {
        @Override
        public Double convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getDouble(column);
        }
    };
    
    private static final TypeConverter<java.sql.Date> CONVERTER_SQL_DATE = new TypeConverter<java.sql.Date>() {
        @Override
        public java.sql.Date convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getDate(column);
        }
    };
        
    private static final TypeConverter<Date> CONVERTER_DATE = new TypeConverter<Date>() {
        @Override
        public Date convert(ResultSet resultSet, int column) throws SQLException {
            return new Date(resultSet.getTimestamp(column).getTime());
        }
    };
    
    private static final TypeConverter<Time> CONVERTER_SQL_TIME = new TypeConverter<Time>() {
        @Override
        public Time convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getTime(column);
        }
    };
    private static final TypeConverter<Timestamp> CONVERTER_SQL_TIMESTAMP = new TypeConverter<Timestamp>() {
        @Override
        public Timestamp convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getTimestamp(column);
        }
    };
    private static final TypeConverter<Boolean> CONVERTER_BOOLEAN = new TypeConverter<Boolean>() {
        @Override
        public Boolean convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getBoolean(column);
        }
    };
    private static final TypeConverter<Object> CONVERTER_OBJECT = new TypeConverter<Object>() {
        @Override
        public Object convert(ResultSet resultSet, int column) throws SQLException {
            return resultSet.getObject(column);
        }
    };
    
    private static int instanceType = INSTANCE_DEFAULT;
    
    private static ConverterManager instance;

    public static ConverterManager getDefaultInstance() {
        if(instance==null || instanceType != INSTANCE_DEFAULT) {
            instance = new ConverterManager();
            instanceType = INSTANCE_DEFAULT;
            
            // Strings
            /*
            instance.put(Types.LONGVARCHAR,String.class, CONVERTER_STRING);
            instance.put(Types.LONGNVARCHAR,String.class, CONVERTER_STRING);
            instance.put(Types.VARCHAR,String.class, CONVERTER_STRING);
            instance.put(Types.NVARCHAR,String.class, CONVERTER_STRING);
            instance.put(Types.NCHAR,String.class, CONVERTER_STRING);
            */
            
            // Object
            instance.put(Types.JAVA_OBJECT,Object.class, CONVERTER_OBJECT);
            
            // Numbers
            instance.put(Types.BIGINT,Long.class, CONVERTER_LONG);
            instance.put(Types.BIT,Integer.class, CONVERTER_INTEGER);
            instance.put(Types.BOOLEAN,Boolean.class, CONVERTER_BOOLEAN);
            instance.put(Types.DECIMAL,Float.class, CONVERTER_FLOAT);
            instance.put(Types.DOUBLE,Double.class, CONVERTER_DOUBLE);
            instance.put(Types.FLOAT,Float.class, CONVERTER_FLOAT);
            instance.put(Types.INTEGER,Integer.class, CONVERTER_INTEGER);
            instance.put(Types.NUMERIC,Double.class, CONVERTER_DOUBLE);
            instance.put(Types.REAL,Float.class, CONVERTER_FLOAT);
            instance.put(Types.SMALLINT,Integer.class, CONVERTER_INTEGER);
            instance.put(Types.TINYINT,Integer.class, CONVERTER_INTEGER);
            
            // Date
            instance.put(Types.DATE,java.sql.Date.class, CONVERTER_SQL_DATE);
            instance.put(Types.TIME,Time.class, CONVERTER_SQL_TIME);
            instance.put(Types.TIMESTAMP,Timestamp.class, CONVERTER_SQL_TIMESTAMP);
            instance.put(Types.TIMESTAMP,Date.class, CONVERTER_DATE);
            
            for(Integer type : getStaticInts(Types.class)) {
                instance.put(type,String.class, CONVERTER_STRING);
            }
        }
        return instance;
    }
    
    public Object convert(ResultSet resultSet, int column, Class targetClass) throws MissingConverterException, SQLException {
        int sqlType = resultSet.getMetaData().getColumnType(column);
        
        TypeConverter currentConverter = get(sqlType, targetClass);
        if(currentConverter==null) {
            throw new MissingConverterException(sqlType,targetClass);
        }
        
        return currentConverter.convert(resultSet,column);
    } 
    
    private static List<Integer> getStaticInts(Class<?> c) {
        List<Integer> list  = new ArrayList<Integer>();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().equals(int.class) && Modifier.isStatic(field.getModifiers())) {
                    list.add(field.getInt(null));
                }
            }
            catch (IllegalAccessException e) {
                // Handle exception here
            }
        }
        return list;
    }
    
    
   
    
}

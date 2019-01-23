package com.dh.fastjdbcabstractor.converter;

/**
 * Exception thrown when the converter sqlType to javaClass is missing
 * 
 * @author AlucardDH
 */
public class MissingConverterException extends Exception {
    
    /**
     * Source sql type (from @See java.sql.Types
     */
    private final int sqlType;
    
    /**
     * Target java class
     */
    private final Class targetClass;

    public MissingConverterException(int sqlType,Class targetClass) {
        super("Missing converter : "+sqlType+" to "+targetClass.getSimpleName());
        this.sqlType = sqlType;
        this.targetClass = targetClass;
    }

    /**
     * @return Source sql type (from @See java.sql.Types
     */
    public int getSqlType() {
        return sqlType;
    }

    /**
     * @return Target java class
     */
    public Class getTargetClass() {
        return targetClass;
    }
    
       
}

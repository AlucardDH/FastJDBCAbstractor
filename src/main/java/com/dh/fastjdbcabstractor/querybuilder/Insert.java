package com.dh.fastjdbcabstractor.querybuilder;

import com.dh.fastjdbcabstractor.utils.Concat;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author AlucardDH
 */
public class Insert extends Query {
    
    private final String table;
    private final LinkedHashMap<String,Field> fields = new LinkedHashMap<>();
    private final LinkedList<String> placeholders = new LinkedList<>();
    
    private final Class<?> clazz;
    private final LinkedList<Object> values = new LinkedList<>();
    private ObjectToSQLConverter converter;

    public Insert(Class<?> clazz,String intoTable,ObjectToSQLConverter converter) {
        super();
        if(clazz==null) {
            throw new NullPointerException("Class cannot be null");
        }
        this.table = intoTable;
        this.clazz = clazz;
        this.converter = converter;
        
        Field[] fs = clazz.getFields();
        for(Field f : fs) {
            fields.put(f.getName(),f);
            placeholders.add("?");
        }
    }
    
    public Insert(Class<?> clazz,String intoTable) {
        this(clazz,intoTable,null);
    }
    
    public Insert(String intoTable) {
        super();
        this.table = intoTable;
        this.clazz = null;
    }
    
    public Insert setUseDefaultValueField(String field) {
        if(fields.containsKey(field)) {
            fields.remove(field);
            placeholders.remove();
        }
        return this;
    }
    
    /**
     * 
     * @param field
     * @return 
     * @throws IllegalStateException("Cannot add field when a class is specified")
     */
    public Insert field(String field) {
        if(clazz!=null) {
            throw new IllegalStateException("Cannot add field when a class is specified");
        }
        fields.put(field,null);
        placeholders.add("?");
        return this;
    }
    
    public Insert fields(String... fields) {
        for(String field : fields) {
            field(field);
        }
        return this;
    }
    
    public Insert value(Object value) {
        if(converter==null) {
            throw new IllegalStateException("Cannot add value when no converter is specified");
        }
        this.values.add(value);
        return this;
    }
    
    public Insert value(Object value,ObjectToSQLConverter converter) {
        if(converter!=null) {
            this.converter = converter;
        }
        return value(value);
    }
    
    public Insert values(Collection<?> values) {
        if(converter==null) {
            throw new IllegalStateException("Cannot add value when no converter is specified");
        }
        this.values.addAll(values);
        return this;
    }
    
    public Insert values(Collection<?> values,ObjectToSQLConverter converter) {
        if(converter!=null) {
            this.converter = converter;
        }
        return values(values);
    }
    
    public boolean hasValues() {
        return !values.isEmpty();
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        sb.append("INSERT INTO ");
        sb.append(table);
        if(!fields.isEmpty()) {
            sb.append("(");
            Concat.concat(sb, fields.keySet(), ",", null);
            sb.append(")");
        }
        sb.append(" VALUES");
        if(values.isEmpty() || converter == null) {
            sb.append("(");
            Concat.concat(sb, placeholders, ",", null);
            sb.append(")");
        } else {
            boolean first = true;
            for(Object value : values) {
                if(!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append("(");
                sb.append(converter.toValueString(value));
                sb.append(")");
            }
        }
        return sb;
    }
    
    public abstract static class ObjectToSQLConverter<T> {
        
        public String escape(String str) {
            return "'"+StringEscapeUtils.escapeSql(str)+"'"; 
        }
        
        public abstract String toValueString(T o);
    }
    
}

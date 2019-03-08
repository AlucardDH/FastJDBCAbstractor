package com.dh.fastjdbcabstractor.querybuilder;

import com.dh.fastjdbcabstractor.utils.Concat;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;

/**
 *
 * @author AlucardDH
 */
public class Select extends Query {
    
    private final LinkedHashSet<String> fields = new LinkedHashSet<>();

    public Select() {
    }
    
    public Select(Class<?> clazz) {
        select(clazz);        
    }
    
    public Select(String... fields) {
        select(fields);
    }
    
    public Select select(String... fields) {
        for(String field : fields) {
            this.fields.add(field);
        }
        return this;
    }
    
    public Select select(Class<?> clazz) {
        for(Field f : clazz.getFields()) {
            if(!Modifier.isStatic(f.getModifiers())) {
                fields.add(f.getName());
            }
        }
        return this;
    }
    
    public Select select(String field,Class<?> clazz) {
        fields.remove(field);
        for(Field f : clazz.getFields()) {
            if(!Modifier.isStatic(f.getModifiers())) {
                fields.add(field + "_" + f.getName());
            }
        }
        return this;
    }
    
    public From from(String table) {
        return from(table,null,null,null);
    }
    
    public From from(String table,String as) {
        return from(table,as,null,null);
    }
    
    public From from(String table,String as,String link,String on) {
        return new From(this).from(table,as,link,on);
    }
    
    public From from(Query query,String as) {
        return from(query,as,null,null);
    }
    
    public From from(Query query,String as,String link,String on) {
        return new From(this).from(query, as, link,on);
    }
    
    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        sb.append("SELECT ");
        Concat.concat(sb, fields, ",","*");
        return sb;
    }

    
    
    
    
}

package com.dh.fastjdbcabstractor.querybuilder;

/**
 *
 * @author AlucardDH
 */
public abstract class Query {
    
    private Query parent;

    public Query() {
    }

    public Query(Query parent) {
        this.parent = parent;
    }
    
    public String build() {
        return buildFromChild(null).toString();
    }
    
    public StringBuilder buildFromChild(StringBuilder sb) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        if(parent!=null) {
            parent.buildFromChild(sb);
            sb.append(' ');
        }
        appendTo(sb);
        return sb;
    }
        
    public abstract StringBuilder appendTo(StringBuilder sb);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
    
    
    
}

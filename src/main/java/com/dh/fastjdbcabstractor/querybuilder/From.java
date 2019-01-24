package com.dh.fastjdbcabstractor.querybuilder;

import java.util.LinkedList;

/**
 *
 * @author AlucardDH
 */
public class From extends Query {
    
    private final LinkedList<FromElement> froms = new LinkedList<>();

    public From(Select select) {
        super(select);
    }
    
    public From from(String table) {
        return from(table,null,null,null);
    }
    
    public From from(String table,String as) {
        return from(table,as,null,null);
    }
    
    public From from(String table,String as,String link,String on) {
        froms.add(new FromElement(table,as,link,on));
        return this;
    }
    
    public From from(Query query,String as) {
        return from(query,as,null,null);
    }
    
    public From from(Query query,String as,String link,String on) {
        froms.add(new FromElement(query,as,link,on));
        return this;
    }
    
    public From join(String table,String as, String on) {
        return from(table,as,"JOIN",on);
    }
    
    public Where where(String condition) {
        Where w = new Where(this);
        return w.where(condition);
    }
    
    public Where where(Where.Condition condition) {
        Where w = new Where(this);
        return w.where(condition);
    }
    
    public Order order(String by) {
        Order order = new Order(this);
        order.order(by);
        return order;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        sb.append("FROM ");
        boolean first = true;
        for(FromElement fromElement : froms) {
            fromElement.appendTo(sb, first);
            first = false;
        }
        return sb;
    }
    
    private class FromElement {
        String table;
        Query query;
        String as;
        
        String link;
        String on;

        public FromElement(String table, String as, String link,String on) {
            this.table = table;
            this.as = as;
            this.link = link;
            this.on = on;
        }

        public FromElement(Query query, String as, String link,String on) {
            this.query = query;
            this.as = as;
            this.link = link;
            this.on = on;
        }

        public StringBuilder appendTo(StringBuilder sb, boolean first) {
            if(sb==null) {
                sb = new StringBuilder();
            }
            if(!first) {
                sb.append(link!=null ? ' '+link : ',').append(' ');
            }
            if(table!=null) {
                sb.append(table);
            } else {
                sb.append('(').append(query.build()).append(')');
            }
            if(as!=null) {
                sb.append(" AS ").append(as);
            }
            if(link!=null && on!=null) {
                sb.append(" ON ").append(on);
            }
            return sb;
        }
        
    }
    
    
}

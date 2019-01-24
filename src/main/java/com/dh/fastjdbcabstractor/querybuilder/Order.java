package com.dh.fastjdbcabstractor.querybuilder;

import com.dh.fastjdbcabstractor.utils.Concat;
import java.util.LinkedList;

/**
 *
 * @author AlucardDH
 */
public class Order extends Query {
    
    private final LinkedList<String> orders = new LinkedList<>();
    
    public Order(Query parent) {
        super(parent);
    }
    
    public Order order(String by) {
        orders.add(by);
        return this;
    }

    @Override
    public StringBuilder appendTo(StringBuilder sb) {
        if(sb==null) {
            sb = new StringBuilder();
        }
        sb.append("ORDER BY ");
        Concat.concat(sb, orders, ",", null);
        return sb;
    }
    
}

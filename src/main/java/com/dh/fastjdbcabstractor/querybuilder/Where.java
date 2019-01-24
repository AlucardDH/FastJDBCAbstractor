package com.dh.fastjdbcabstractor.querybuilder;

/**
 *
 * @author AlucardDH
 */
public class Where extends Query {
    
    private Condition condition;

    public Where(From from) {
        super(from);
    }
    
    public Where where(String condition) {
        this.condition = new Condition(condition); 
        return this;
    }
    
    public Where where(Condition condition) {
        this.condition = condition;
        return this;
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
        sb.append("WHERE ");
        condition.appendTo(sb);
        return sb;
    }
    
    public static class Condition {
        private String singleCondition;
        
        private String op;
        private Condition conditionLeft;
        private Condition conditionRight;
        
        private Condition() {}
        
        public Condition(String condition) {
            this.singleCondition = condition;
        }
        
        public static Condition create(String left,String operator, String right) {
            return create(new Condition(left), operator, new Condition(right));
        }
        
        public static Condition create(Condition left,String operator, String right) {
            return create(left, operator, new Condition(right));
        }
        
        public static Condition create(Condition left,String operator, Condition right) {
            Condition c = new Condition();
            c.conditionLeft = left;
            c.conditionRight = right;
            c.op = operator;
            return c;
        }
        
        public static Condition create(String left,String operator, Condition right) {
            return create(new Condition(left), operator, right);
        }
                
        public static Condition and(String left,String right) {
            return create(left, "AND", right);
        }
        
        public static Condition or(String left,String right) {
            return create(left, "OR", right);
        }
        
        public static Condition and(Condition left,String right) {
            return create(left, "AND", right);
        }
        
        public static Condition or(Condition left,String right) {
            return create(left, "OR", right);
        }
        
        public static Condition and(Condition left,Condition right) {
            return create(left, "AND", right);
        }
        
        public static Condition or(Condition left,Condition right) {
            return create(left, "OR", right);
        }
                
        public static Condition and(String left,Condition right) {
            return create(left, "AND", right);
        }
        
        public static Condition or(String left,Condition right) {
            return create(left, "OR", right);
        }
        
        public StringBuilder appendTo(StringBuilder sb) {
            if(sb==null) {
                sb = new StringBuilder();
            }
            if(singleCondition!=null) {
                sb.append(singleCondition);
                return sb;
            } else {
                sb.append('(');
                conditionLeft.appendTo(sb);
                sb.append(") ").append(op).append(" (");
                conditionRight.appendTo(sb);
                sb.append(")");
                return sb;
            }
        }
        
    }
    
}

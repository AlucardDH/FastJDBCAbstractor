package com.dh.fastjdbcabstractor.querybuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlucardDH
 */
public class OrderTest {
    
    public OrderTest() {
    }

    /**
     * Test of order method, of class Order.
     */
    @Test
    public void testOrder() {
        System.out.println("order");
        
        Order instance = new Select().from("foobar").order("foo");
        String expResult = "SELECT * FROM foobar ORDER BY foo";
        assertEquals(expResult, instance.build());
    }
    
}

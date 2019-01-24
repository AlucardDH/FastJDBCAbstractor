package com.dh.fastjdbcabstractor.querybuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlucardDH
 */
public class WhereTest {
    
    public WhereTest() {
    }

    /**
     * Test of where method, of class Where.
     */
    @Test
    public void testWhere_String() {
        System.out.println("where(string)");
        
        Where instance = new Select().from("foobar").where("foo='bar'");
        String expResult = "SELECT * FROM foobar WHERE foo='bar'";
        assertEquals(expResult, instance.build());
    }

    /**
     * Test of where method, of class Where.
     */
    @Test
    public void testWhere_WhereCondition() {
        System.out.println("where(condiftion)");
        
        Where instance = new Select().from("foobar").where(Where.Condition.and("foo='foo'","bar='bar'"));
        String expResult = "SELECT * FROM foobar WHERE (foo='foo') AND (bar='bar')";
        assertEquals(expResult, instance.build());
    }

}

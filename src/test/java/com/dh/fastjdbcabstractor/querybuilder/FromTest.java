package com.dh.fastjdbcabstractor.querybuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlucardDH
 */
public class FromTest {
    
    public FromTest() {
    }

    /**
     * Test of from method, of class From.
     */
    @Test
    public void testFrom_String() {
        System.out.println("from(table)");
        String table = "foobar";
        From instance = new From(new Select());
        String expResult = "SELECT * FROM "+table;
        From result = instance.from(table);
        assertEquals(expResult, result.build());
    }

    /**
     * Test of from method, of class From.
     */
    @Test
    public void testFrom_String_String() {
        System.out.println("from(table,as)");
        String table = "foobar";
        String as = "fb";
        From instance = new From(new Select());
        String expResult = "SELECT * FROM "+table+" AS "+as;
        From result = instance.from(table,as);
        assertEquals(expResult, result.build());
    }

    /**
     * Test of from method, of class From.
     */
    @Test
    public void testFrom_String_String_String() {
        System.out.println("from(table,as,link)");
        String table1 = "foobar1";
        String table2 = "foobar2";
        String as1 = "fb1";
        String as2 = "fb2";
        From instance = new From(new Select());
        String expResult = "SELECT * FROM foobar1 AS fb1 JOIN foobar2 AS fb2 ON fb1.foo=fb2.foo";
        From result = instance.from(table1, as1).join(table2, as2, "fb1.foo=fb2.foo");
        assertEquals(expResult, result.build());
    }

    /**
     * Test of from method, of class From.
     */
    @Test
    public void testFrom_Query_String() {
        System.out.println("from(query,as)");
        Query query = new Select().from(new Select("foo").from("foobar"),"fb");
        String expResult = "SELECT * FROM (SELECT foo FROM foobar) AS fb";
        assertEquals(expResult, query.build());
    }
}

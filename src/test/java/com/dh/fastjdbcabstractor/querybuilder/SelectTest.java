package com.dh.fastjdbcabstractor.querybuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AlucardDH
 */
public class SelectTest {
    
    public SelectTest() {
    }
    
    @Test
    public void testSelect_empty() {
        System.out.println("new Select()");
        
        Select instance = new Select();
        String expResult = "SELECT *";
        
        assertEquals(expResult, instance.toString());
    }

    /**
     * Test of select method, of class Select.
     */
    @Test
    public void testSelect_StringArr() {
        System.out.println("select(string...)");
        
        Select instance = new Select();
        String expResult = "SELECT foo,bar,foo2";
        Select result = instance.select("foo","bar").select("foo2");
        
        assertEquals(expResult, result.toString());
    }
    
        
    private class TestClass {
        public String foo;
        public Integer bar;
        public TestSubClass sub;
    }
    
    private class TestSubClass {
        public String subfoo;
        public String subbar;
    }

    /**
     * Test of select method, of class Select.
     */
    @Test
    public void testSelect_Class() {
        System.out.println("select(class)");
        
        Select instance = new Select(TestClass.class).select("sub",TestSubClass.class);
        
        String expResult = "SELECT foo,bar,sub_subfoo,sub_subbar";
        assertEquals(expResult, instance.toString());
    }
    
}

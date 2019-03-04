/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dh.fastjdbcabstractor.querybuilder;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author frup58637
 */
public class InsertTest {
    
    public InsertTest() {
    }
    
    private class TestClass {
        public int id;
        public String foo;
        public String bar;

        public TestClass(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }

    /**
     * Test of setUseDefaultValueField method, of class Insert.
     */
    @Test
    public void testSetUseDefaultValueField() {
        System.out.println("Test insert");
        
        String table = "table";
        TestClass tc1 = new TestClass("hello","wor'ld");
        TestClass tc2 = new TestClass("foo","bar");
        
        Insert instance = new Insert(TestClass.class,table, new Insert.ObjectToSQLConverter<TestClass>() {
            @Override
            public String toValueString(TestClass o) {
                return escape(o.foo)+","+escape(o.bar);
            }
        }).setUseDefaultValueField("id").value(tc1).value(tc2);
        
        String expResult = "INSERT INTO "+table+"(foo,bar) VALUES('hello','wor''ld'),('foo','bar')";
        assertEquals(expResult, instance.build());
    }

    
}

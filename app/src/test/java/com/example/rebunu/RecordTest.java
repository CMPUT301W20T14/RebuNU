package com.example.rebunu;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for Record
 * @author Zihao Huang
 */
public class RecordTest {

    /**
     * create a record
     */
    Record mockRecord(){
        Record record = new Record();
        record.setId("123");
        record.setType(1);
        return record;
    }

    /**
     * test getters
     */

    @Test
    void testGet(){

        assertEquals("123", mockRecord().getId());
        assertEquals((Integer)1, mockRecord().getType());
    }

    /**
     * test setters
     */
    @Test
    void testSet(){
        Record record = new Record();
        record.setType(2);
        record.setId("333");
        assertEquals((Integer)2, record.getType());
        assertEquals("333", record.getId());

    }

    /**
     * test Exceptions
     */
    @Test
    void testException(){
        Record record = new Record();

        assertThrows(Exception.class, ()->{
            record.setId(null);
        });

        assertThrows(Exception.class, ()->{
            record.setId(null);
        });

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                record.setType(-1);
            }
        });

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                record.setType(null);
            }
        });

        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                record.setType(10);
            }
        });
    }



}

package com.example.rebunu;

import android.location.Location;

import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for Order
 * @author Zihao Huang
 */
public class OrderTest {

    /**
     * create a order
     */

    Order mockOrder(){
        Order o = new Order();
        try{
            o.setRating(true);
            o.setDriverId("123");
            o.setPrice(23);
            o.setRiderId("123");
            o.setStatus(2);
        }catch (Exception e){}

        return o;
    }

    /**
     * test getters
     */
    @Test
    void testGetter(){
        Order o = mockOrder();

        assertTrue(o.getRating());

        assertEquals("123", o.getDriverId());
        assertEquals("123", o.getRiderId());
        assertEquals((Integer)23, o.getPrice());
        assertEquals((Integer)2, o.getStatus());
    }

    /**
     * test setters
     */
    @Test
    void testSetter(){
        Order o = new Order();
        try{
            o.setRating(false);
            o.setRiderId("456");
            o.setDriverId("789");
            o.setPrice(1);
            o.setStatus(3);
        }catch (Exception e){}

        assertEquals("789",o.getDriverId());
        assertEquals("456",o.getRiderId());
        assertFalse(o.getRating());
        assertEquals((Integer)1, o.getPrice());
        assertEquals((Integer)3, o.getStatus());
    }

    /**
     * test Exceptions
     */

    @Test
    void testException(){
        Order o = new Order();

        assertThrows(Exception.class, ()->{
            o.setRating(null);
        });
        assertThrows(Exception.class, ()->{
            o.setDriverId(null);
        });
        assertThrows(Exception.class, ()->{
            o.setDriverId("");
        });
        assertThrows(Exception.class, ()->{
            o.setRiderId(null);
        });
        assertThrows(Exception.class, ()->{
            o.setRiderId("");
        });
        assertThrows(Exception.class, ()->{
            o.setPrice(null);
        });
        assertThrows(Exception.class, ()->{
            o.setPrice(-2);
        });
        assertThrows(Exception.class, ()->{
            o.setStatus(-2);
        });
        assertThrows(Exception.class, ()->{
            o.setStatus(null);
        });
    }






}

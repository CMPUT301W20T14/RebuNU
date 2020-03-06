package com.example.rebunu;

import android.location.Location;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Request
 * @author Lefan Wang
 */

public class RequestTest {
    private Request request;

    /**
     * Test for Request constructor
     */
    @Test
    void fakeRequest(){
        try{
            request = new Request(new Location(""), new Location(""),20,11111111);
        } catch (Exception ignored){}
    }

    /**
     * Test for all getters
     */
    @Test
    public void testGetters(){
        fakeRequest();
        assertEquals((Integer)20, request.getPrice());
        assertEquals((Integer)11111111, request.getRiderId());
    }

    @Test
    public void testSetters(){
        fakeRequest();
        try{
            assertEquals((Integer)20,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setPrice(25);
            assertEquals((Integer)25,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setRiderId(22222222);
            assertEquals((Integer)25,request.getPrice());
            assertEquals((Integer)22222222,request.getRiderId());
        }catch (Exception ignored){}
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testExceptions(){
        fakeRequest();
        Integer nullStatus = null;
        Integer invalidStatus = 4;
        Integer nullPrice = null;
        Integer invalidPrice = -10;
        Integer nullRiderId = null;
        Integer invalidRiderId = -10;

        assertThrows(NullPointerException.class,()->{
            request.setPrice(nullPrice);
        });
        assertThrows(Exception.class,()->{
            request.setPrice(invalidPrice);
        });
        assertThrows(NullPointerException.class,()->{
            request.setRiderId(nullRiderId);
        });
        assertThrows(Exception.class,()->{
            request.setRiderId(invalidRiderId);
        });
    }
}

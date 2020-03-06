package com.example.rebunu;

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
            request = new Request(new Location(120.0,60.0),new Location(125.0,65.0),20,11111111);
        }catch (Exception ignored){}
    }

    /**
     * Test for all getters
     */
    @Test
    public void testGetters(){
        fakeRequest();
        assertEquals((Double)120.0,request.getStart().getLongitude());
        assertEquals((Double)60.0,request.getStart().getLatitude());
        assertEquals((Double)125.0,request.getEnd().getLongitude());
        assertEquals((Double)65.0,request.getEnd().getLatitude());
        assertEquals((Integer)20,request.getPrice());
        assertEquals((Integer)11111111,request.getRiderId());
    }

    @Test
    public void testSetters(){
        fakeRequest();
        try{
            assertEquals((Double)120.0,request.getStart().getLongitude());
            assertEquals((Double)60.0,request.getStart().getLatitude());
            assertEquals((Double)125.0,request.getEnd().getLongitude());
            assertEquals((Double)65.0,request.getEnd().getLatitude());
            assertEquals((Integer)20,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setStart(new Location(100.0,40.0));
            assertEquals((Double)100.0,request.getStart().getLongitude());
            assertEquals((Double)40.0,request.getStart().getLatitude());
            assertEquals((Double)125.0,request.getEnd().getLongitude());
            assertEquals((Double)65.0,request.getEnd().getLatitude());
            assertEquals((Integer)20,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setEnd(new Location(105.0,45.0));
            assertEquals((Double)100.0,request.getStart().getLongitude());
            assertEquals((Double)40.0,request.getStart().getLatitude());
            assertEquals((Double)105.0,request.getEnd().getLongitude());
            assertEquals((Double)45.0,request.getEnd().getLatitude());
            assertEquals((Integer)20,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setPrice(25);
            assertEquals((Double)100.0,request.getStart().getLongitude());
            assertEquals((Double)40.0,request.getStart().getLatitude());
            assertEquals((Double)105.0,request.getEnd().getLongitude());
            assertEquals((Double)45.0,request.getEnd().getLatitude());
            assertEquals((Integer)25,request.getPrice());
            assertEquals((Integer)11111111,request.getRiderId());

            request.setRiderId(22222222);
            assertEquals((Double)100.0,request.getStart().getLongitude());
            assertEquals((Double)40.0,request.getStart().getLatitude());
            assertEquals((Double)105.0,request.getEnd().getLongitude());
            assertEquals((Double)45.0,request.getEnd().getLatitude());
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

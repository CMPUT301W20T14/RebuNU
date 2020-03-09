package com.example.rebunu;

import android.location.Location;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Unit test for Request
 * @author Lefan Wang, Zijian Xi
 */

public class RequestTest {
    private Request request;

    /**
     * mock up a Location object
     * @param longitude a Double
     * @param latitude a Double
     * @return a Location mock up
     */
    public Location fakeLocation(Double longitude, Double latitude) {
        Location location = mock(Location.class);
        doNothing().when(location).setLongitude(anyDouble());
        doNothing().when(location).setLatitude(anyDouble());
        doReturn(longitude).when(location).getLongitude();
        doReturn(latitude).when(location).getLatitude();
        return location;
    }

    /**
     * generates a fake Request object
     * @param start a Location object
     * @param end a Location object
     * @param price an Integer
     * @param riderId an Integer
     */
    void fakeRequest(Location start, Location end, Integer price, String riderId, Boolean noId){
        try{
            request = new Request(start, end, price, riderId, noId);
        } catch (Exception ignored){}
    }

    /**
     * Test for constructor and all getters
     */
    @Test
    public void testConstructorAndGetters(){
        fakeRequest(fakeLocation(10.0, 20.0), (fakeLocation(30.0, 40.0)), 20, "11111111", true);
        assertTrue( request.getStart().getLongitude()==10.0 && request.getStart().getLatitude()==20.0);
        assertTrue(request.getEnd().getLongitude()==30.0 && request.getEnd().getLatitude()==40.0);
        assertEquals((Integer)20, request.getPrice());
        assertEquals("11111111", request.getRiderId());
    }

    @Test
    public void testSetters(){
        fakeRequest(fakeLocation(10.0, 20.0), (fakeLocation(30.0, 40.0)), 20, "11111111", true);
        try{
            assertTrue( request.getStart().getLongitude()==10.0 && request.getStart().getLatitude()==20.0);
            assertTrue(request.getEnd().getLongitude()==30.0 && request.getEnd().getLatitude()==40.0);
            assertEquals((Integer)20,request.getPrice());
            assertEquals("11111111", request.getRiderId());


            request.setPrice(25);
            assertTrue( request.getStart().getLongitude()==10.0 && request.getStart().getLatitude()==20.0);
            assertTrue(request.getEnd().getLongitude()==30.0 && request.getEnd().getLatitude()==40.0);
            assertEquals((Integer)25,request.getPrice());
            assertEquals("11111111", request.getRiderId());

            request.setRiderId("22222222");
            assertTrue( request.getStart().getLongitude()==10.0 && request.getStart().getLatitude()==20.0);
            assertTrue(request.getEnd().getLongitude()==30.0 && request.getEnd().getLatitude()==40.0);
            assertEquals((Integer)25,request.getPrice());
            assertEquals("22222222", request.getRiderId());

            Location start = fakeLocation(100.0, 200.0);
            Location end = fakeLocation(300.0, 400.0);

            request.setStart(start);
            assertTrue( request.getStart().getLongitude()==100.0 && request.getStart().getLatitude()==200.0);
            assertTrue(request.getEnd().getLongitude()==30.0 && request.getEnd().getLatitude()==40.0);
            assertEquals((Integer)25,request.getPrice());
            assertEquals("22222222", request.getRiderId());

            request.setEnd(end);
            assertTrue( request.getStart().getLongitude()==100.0 && request.getStart().getLatitude()==200.0);
            assertTrue(request.getEnd().getLongitude()==300.0 && request.getEnd().getLatitude()==400.0);
            assertEquals((Integer)25,request.getPrice());
            assertEquals("22222222", request.getRiderId());
        }catch (Exception ignored){}
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testExceptions(){
        fakeRequest(fakeLocation(10.0, 20.0), (fakeLocation(30.0, 40.0)), 20, "11111111", true);
        Integer nullStatus = null;
        Integer invalidStatus = 4;
        Integer nullPrice = null;
        Integer invalidPrice = -10;
        String nullRiderId = null;
        Location nullStart = null;
        Location nullEnd = null;

        assertThrows(NullPointerException.class,()->{
            request.setPrice(nullPrice);
        });
        assertThrows(Exception.class,()->{
            request.setPrice(invalidPrice);
        });
        assertThrows(NullPointerException.class,()->{
            request.setRiderId(nullRiderId);
        });
        assertThrows(Exception.class, ()->{
            request.setStart(nullStart);
        });
        assertThrows(Exception.class, ()->{
            request.setEnd(nullEnd);
        });
    }
}

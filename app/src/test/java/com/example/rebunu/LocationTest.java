package com.example.rebunu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Location
 * @author Lefan Wang
 */

class LocationTest {
    private Location location;

    /**
     * Test for Location constructor
     */
    @Test
    void fakeLocation(){
        try{
            this.location = new Location(113.49,53.54);
        } catch (Exception ignored){}
    }

    /**
     * Test for two getters
     */
    @Test
    public void testGetters(){
        fakeLocation();
        assertEquals((Double) 113.49,location.getLongitude());
        assertEquals((Double)53.54,location.getLatitude());
    }

    /**
     * Test for two setters
     */
    @Test
    public void testSetters(){
        fakeLocation();
        try{
            location.setLongitude(103.49);
            location.setLatitude(43.54);
        } catch (Exception ignored){}

        assertEquals((Double)103.49,location.getLongitude());
        assertEquals((Double)43.54,location.getLatitude());
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testException(){
        Location location;
        Double nullDouble = null;
        Double negDouble = -1.0;
        Double outofrangeDouble = 181.0;

        assertThrows(Exception.class,()->{
            new Location(nullDouble,nullDouble);
        });
        assertThrows(Exception.class,()->{
            new Location(negDouble,negDouble);
        });
        assertThrows(Exception.class,()->{
            new Location(outofrangeDouble,outofrangeDouble);
        });
    }
}

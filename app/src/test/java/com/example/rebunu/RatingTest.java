package com.example.rebunu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Rating
 * @author Zijian Xi
 */
class RatingTest {
    private Rating rating;

    /**
     * Test for Rating constructor
     */
    @Test
    void fakeRating() {
        try {
            this.rating = new Rating(1,2);
        } catch (Exception ignored) {}
    }

    /**
     * Test for all getters
     */
    @Test
    public void testGetters() {
        fakeRating();
        assertEquals(1, (int)rating.getThumbsUp());
        assertEquals(2, (int)rating.getThumbsDown());
    }

    /**
     * Test for all setters
     */
    @Test
    public void testSetters() {
        fakeRating();
        try {
            rating.setThumbsUp(10);
            rating.setThumbsDown(20);
        } catch (Exception ignored){}

        assertEquals(10, (int)rating.getThumbsUp());
        assertEquals(20, (int)rating.getThumbsDown());

        rating.thumbsUpPlus();
        assertEquals(11, (int)rating.getThumbsUp());
        rating.thumbsDownPlus();
        assertEquals(21, (int)rating.getThumbsDown());
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testException() {
        Rating rating;
        Integer nullInteger = null;
        assertThrows(Exception.class, ()->{
            new Rating(nullInteger, nullInteger);
        });
        Integer negInteger = -1;
        assertThrows(Exception.class, ()->{
           new Rating(negInteger, negInteger);
        });
    }
}

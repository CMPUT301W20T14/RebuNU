package com.example.rebunu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * Unit test for Profile
 * @author Zijian Xi
 */
public class ProfileTest {
    private Profile profile;

    /**
     * generate a fake profile
     */
    void fakeProfile() {
        try {
            profile = new Profile("1111111111", "fake@fake.fake", "fakeusername", 100, true, new Rating(10, 20), true);
        } catch (Exception ignored){}
    }

    /**
     * Test for constructor and all getters
     */
    @Test
    public void testConstructorAndGetters() {
        fakeProfile();
        assertEquals("1111111111", profile.getPhone());
        assertEquals("fake@fake.fake", profile.getEmail());
        assertEquals("fakeusername", profile.getName());
        assertEquals((Integer) 100, profile.getBalance());
        assertEquals(true, profile.getRole());
        assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
    }

    /**
     * Test for all setters
     */
    @Test
    public void testSetters() {
        fakeProfile();
        try {
            profile.setPhone("2222222222");
            assertEquals("2222222222", profile.getPhone());
            assertEquals("fake@fake.fake", profile.getEmail());
            assertEquals("fakeusername", profile.getName());
            assertEquals((Integer) 100, profile.getBalance());
            assertEquals(true, profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
            profile.setEmail("f@f.f");
            assertEquals("2222222222", profile.getPhone());
            assertEquals("f@f.f", profile.getEmail());
            assertEquals("fakeusername", profile.getName());
            assertEquals((Integer) 100, profile.getBalance());
            assertEquals("driver", profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
            profile.setName("usernamef");
            assertEquals("2222222222", profile.getPhone());
            assertEquals("f@f.f", profile.getEmail());
            assertEquals("usernamef", profile.getName());
            assertEquals((Integer) 100, profile.getBalance());
            assertEquals("driver", profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
            profile.setBalance(200);
            assertEquals("2222222222", profile.getPhone());
            assertEquals("f@f.f", profile.getEmail());
            assertEquals("usernamef", profile.getName());
            assertEquals((Integer) 200, profile.getBalance());
            assertEquals("driver", profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
            profile.setRole(false);
            assertEquals("2222222222", profile.getPhone());
            assertEquals("f@f.f", profile.getEmail());
            assertEquals("usernamef", profile.getName());
            assertEquals((Integer) 200, profile.getBalance());
            assertEquals("customer", profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 10 && profile.getRating().getThumbsDown() == 20));
            profile.setRating(new Rating(20, 10));
            assertEquals("2222222222", profile.getPhone());
            assertEquals("f@f.f", profile.getEmail());
            assertEquals("usernamef", profile.getName());
            assertEquals((Integer) 200, profile.getBalance());
            assertEquals("customer", profile.getRole());
            assertTrue((profile.getRating().getThumbsUp() == 20 && profile.getRating().getThumbsDown() == 10));
        }
        catch (Exception ignored){}
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testExceptions() {
        fakeProfile();
        String nullPhone = null;
        String emptyPhone = "";
        String invalidPhone = "i111111111";
        String nullEmail = null;
        String emptyEmail = "";
        String invalidEmail = "f2f.f";
        String nullUserName = null;
        String emptyUserName ="";
        Integer nullBalance = null;
        Integer invalidBalance = -10;
        Boolean nullRole = null;
        Rating nullRating = null;

        assertThrows(NullPointerException.class, ()->{
            profile.setPhone(nullPhone);
        });
        assertThrows(Exception.class, ()->{
            profile.setPhone(emptyPhone);
        });
        assertThrows(Exception.class, ()->{
            profile.setPhone(invalidPhone);
        });
        assertThrows(NullPointerException.class, ()->{
            profile.setEmail(nullEmail);
        });
        assertThrows(Exception.class, ()->{
            profile.setEmail(emptyEmail);
        });
        assertThrows(Exception.class, ()->{
            profile.setEmail(invalidEmail);
        });
        assertThrows(NullPointerException.class, ()->{
            profile.setName(nullUserName);
        });
        assertThrows(Exception.class, ()->{
            profile.setName(emptyUserName);
        });
        assertThrows(NullPointerException.class, ()->{
            profile.setBalance(nullBalance);
        });
        assertThrows(Exception.class, ()->{
           profile.setBalance(invalidBalance);
        });
        assertThrows(NullPointerException.class, ()->{
            profile.setRole(nullRole);
        });
        assertThrows(NullPointerException.class, ()->{
            profile.setRating(nullRating);
        });
    }
}

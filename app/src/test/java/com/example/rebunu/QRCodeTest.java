package com.example.rebunu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QRCodeTest {
    private QRCode qrCode;

    void fakeQR() {
        try {
            qrCode = new QRCode(123, 234, 345);
        } catch (Exception ignored) {}
    }

    /**
     * Test for constructor and all getters
     */
    @Test
    public void testConstructorAndGetters() {
        fakeQR();
        assertEquals((Integer) 123, qrCode.getDriverId());
        assertEquals((Integer) 234, qrCode.getRiderId());
        assertEquals((Integer) 345,qrCode.getPrice());
    }

    /**
     * Test for all setters
     */
    @Test
    public void testSetters() {
        fakeQR();
        try {
            qrCode.setDriverId(222);
            assertEquals((Integer)222, qrCode.getDriverId());
            assertEquals((Integer)234, qrCode.getRiderId());
            assertEquals((Integer)345, qrCode.getPrice());

            qrCode.setRiderId(333);
            assertEquals((Integer)222, qrCode.getDriverId());
            assertEquals((Integer)333, qrCode.getRiderId());
            assertEquals((Integer)345, qrCode.getPrice());

            qrCode.setPrice(444);
            assertEquals((Integer)222, qrCode.getDriverId());
            assertEquals((Integer)333, qrCode.getRiderId());
            assertEquals((Integer)444, qrCode.getPrice());

        }
        catch (Exception ignored){}
    }

    /**
     * Test for all exceptions thrown
     */
    @Test
    public void testExceptions() {
        fakeQR();
        Integer invalidDriverId = -1;
        Integer invalidRiderId = -1;
        Integer invalidPrice = -1;


        assertThrows(Exception.class, ()->{
            qrCode.setRiderId(invalidRiderId);
        });

        assertThrows(Exception.class, ()->{
            qrCode.setDriverId(invalidDriverId);
        });

        assertThrows(Exception.class, ()->{
            qrCode.setPrice(invalidPrice);
        });


    }

}

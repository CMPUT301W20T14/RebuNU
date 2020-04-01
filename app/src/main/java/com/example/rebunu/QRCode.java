package com.example.rebunu;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * This is the QRCode class implementation.
 * @author Bofeng Chen
 */

// Reference: https://www.jianshu.com/p/b275e818de6a 2017.02.08 03:44:32 by xiaonan
public class QRCode {
    private String driverId;
    private String riderId;
    private Integer price;
    private String transactionID;
    private Bitmap bitmap;
    // private String content;

    public QRCode(String driverId, String riderId, Integer price){
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);

        System.out.println("Today is: " + todayAsString);
        this.driverId = driverId;
        this.riderId = riderId;
        this.price = price;
        this.transactionID = driverId+riderId+todayAsString;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(getContent(), BarcodeFormat.QR_CODE, 700, 700);
            this.bitmap = Bitmap.createBitmap(700, 700, Bitmap.Config.RGB_565);
            for (int x = 0; x < 700; x++) {
                for (int y = 0; y < 700; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Getter for transcationID
     * @return transcationID
     */
    public String transcationID(){
        return transactionID;
    }

    /**
     * Getter for riderId
     * @return riderId
     */
    public String getRiderId(){
        return riderId;
    }

    /**
     * Getter for price
     * @return price
     */
    public Integer getPrice(){
        return price;
    }

    /**
     * Getter for bitmap
     * @return bitmap
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    /**
     * Getter for driverId
     * @return driverId
     */
    public String getDriverId(){
        return driverId;
    }

    /**
     * Getter for content
     * @return content
     */
    public String getContent() {
        return this.driverId + " "
                + this.riderId + " "
                + this.price + "";
    }

    /**
     * Setter for driverId
     * @param driverId a positive Integer
     * @throws Exception invalid number
     */
    public void setDriverId(String driverId) throws Exception{
        if (driverId != null){
            this.driverId = driverId;
        } else {
            throw new Exception("Invalid driverId.");
        }
    }

    /**
     * Setter for riderId
     * @param riderId a positive Integer
     * @throws Exception invalid number
     */
    public void setRiderId(String riderId) throws Exception{
        if (riderId != null){
            this.riderId = riderId;
        } else {
            throw new Exception("Invalid riderId.");
        }
    }

    /**
     * Setter for price
     * @param price a String
     * @throws Exception null exception or empty exception
     */
    public void setPrice(Integer price) throws Exception{
        if (price >= 0){
            this.price = price;
        } else {
            throw new Exception("Invalid price.");
        }
    }
}

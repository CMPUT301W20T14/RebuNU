package com.example.rebunu;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;


/**
 * This is the QRCode class implementation.
 * @author Bofeng Chen
 */
public class QRCode {
    private Integer driverId;
    private Integer riderId;
    private Integer price;
    private String content;

    /**
     * Constructor for QRCode
     * @param content a string
     */
    public Bitmap QRCode(String content){

        QRCodeWriter qrCodeWriter = new QRCodeWriter();


        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(this.content, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++){
                for (int y = 0; y < 200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Getter for driverId
     * @return driverId
     */
    public Integer getDriverId(){
        return driverId;
    }

    /**
     * Getter for riderId
     * @return riderId
     */
    public Integer getRiderId(){
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
     * Getter for content
     * @return content
     */
    public String getContent(){ return content; }


    /**
     * Setter for driverId
     * @param driverId a positive Integer
     * @throws Exception invalid number
     */
    public void setDriverId(Integer driverId)throws Exception{
        if (driverId >= 0){
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
    public void setRiderId(Integer riderId)throws Exception{
        if (riderId >= 0){
            this.riderId = riderId;
        } else {
            throw new Exception("Invalid riderId.");
        }
    }

    /**
     * Setter for price
     * @param price a String
     * @throws NullPointerException null exception or empty exception
     */
    public void setPrice(Integer price)throws Exception{
        if (price >= 0){
            this.price = price;
        } else {
            throw new Exception("Invalid price.");
        }
    }

    /**
     * Setter for content
     * @param content a String
     * @throws NullPointerException null exception or empty exception
     */
    public void setContent(String content){
        this.content = this.driverId + " "
                     + this.riderId + " "
                     + this.price + "";
    }
}

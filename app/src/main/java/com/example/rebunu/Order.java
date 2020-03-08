package com.example.rebunu;

import android.location.Location;

/**
 * This is the Order class implementation.
 * @author Bofeng Chen, Zihao Huang
 */
public class Order extends Record{
    private QRCode qr;
    private Boolean ratingOfThisOrder; //True for thumbsUp, False for thumbsDown
    private Integer driveId;
    private Location start;
    private Location end;
    private Integer price;
    private Integer riderId;
    private Integer status;
    private Database db = new Database();

    /**
     * Constructor for Order
     * @see Record
     */

    public Order(){
        this.qr = null;
        this.ratingOfThisOrder = null;
        this.driveId = -1;
        this.riderId = -1;
        this.start = null;
        this.end = null;
        this.price = -1;
        this.status = -1;
    }

    /**
     * Getters for all the params
     */
    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getRiderId(){
        return riderId;
    }

    public Integer getStatus(){ return  status;}

    public QRCode getQRCode() {
        return qr;
    }

    public Boolean getRating() {
        return ratingOfThisOrder;
    }

    public Integer getDriveId() {
        return driveId;
    }



    /**
     * Setter for qr
     * @param qr a two-dimensional array
     * @throws NullPointerException null exception
     */
    public void setQRCode(QRCode qr) throws Exception {
        if (qr != null) {
            this.qr = qr;
            return;
        }
        throw new NullPointerException("qr is null.");
    }

    /**
     * Setter for rating
     * @param ratingOfThisOrder (true or false for thumbsUp)
     */
    public void setRating(Boolean ratingOfThisOrder){
        this.ratingOfThisOrder = ratingOfThisOrder;
        Profile driverProfile = (Profile) db.queryById(driveId, 1);
        Rating rating = driverProfile.getRating();
        if(ratingOfThisOrder == null){
            return;
        }
        if(ratingOfThisOrder){
            rating.thumbsUpPlus();
            driverProfile.setRating(rating);
            db.modify(driverProfile);

        }else{
            rating.thumbsDownPlus();
            driverProfile.setRating(rating);
            db.modify(driverProfile);

        }

    }

    /**
     * Setter for driveId
     * @param driveId a positive integer
     * @throws NullPointerException null exception
     */
    public void setDriveId(Integer driveId) throws Exception {
        if (driveId == null) {
            throw new NullPointerException("driveId is null.");
        } else {
            if (driveId < 0) {
                throw new Exception("Invalid driveId.");
            } else {
                this.driveId = driveId;
            }
        }
    }



    /**
     * setter for setStart
     * @param start a Location object
     * @throws NullPointerException null exception
     */
    public void setStart(Location start) throws NullPointerException{
        if (start != null){
            this.start = start;
            return;
        }
        throw new NullPointerException("Invalid start");
    }

    /**
     * setter for setEnd
     * @param end a Location object
     * @throws NullPointerException null exception
     */
    public void setEnd(Location end) throws NullPointerException{
        if (end != null){
            this.end = end;
            return;
        }
        throw new NullPointerException("Invalid end");
    }

    /**
     * setter for setPrice
     * @param price a positive integer
     * @throws Exception null or negative exceptions
     */
    public void setPrice(Integer price) throws Exception{
        if(price == null){
            throw new NullPointerException("Price is null");
        }
        else{
            if(price > 0){
                this.price = price;
            }
            else{
                throw  new Exception("Invalid price");
            }
        }
    }

    /**
     * setter for setRiderId
     * @param riderId a positive integer
     * @throws Exception null or negative exceptions
     */
    public void setRiderId(Integer riderId)throws Exception{
        if(riderId == null){
            throw new NullPointerException("RiderId is null");
        }
        else{
            if(riderId > 0){
                this.riderId = riderId;
            }
            else{
                throw  new Exception("Invalid riderId");
            }
        }
    }


    public void setStatus(Integer status){
        if(status != 1 && status != 2 && status != 3){
            this.status = status;
        }
    }

}




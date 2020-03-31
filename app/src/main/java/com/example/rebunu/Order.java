package com.example.rebunu;

import android.location.Location;

/**
 * This is the Order class implementation.
 * @author Bofeng Chen, Zihao Huang
 */
public class Order extends Record{
//    private QRCode qr;
    private Boolean ratingOfThisOrder; //True for thumbsUp, False for thumbsDown
    private String driverId;
    private Location start;
    private Location end;
    private Integer price;
    private String riderId;
    private Integer status;
    //private Database db = new Database();

    /**
     * Constructor for Order
     * @see Record
     */

    public Order(){
        this.ratingOfThisOrder = null;
        this.driverId = "";
        this.riderId = "";
        this.start = null;
        this.end = null;
        this.price = -1;
        this.status = -1;
        this.setType(3);
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

    public String getRiderId(){
        return riderId;
    }

    public Integer getStatus(){ return  status;}

    public Boolean getRating() {
        return ratingOfThisOrder;
    }

    public String getDriverId() {
        return driverId;
    }



    /**
     * Setter for rating
     * @param ratingOfThisOrder (true or false for thumbsUp)
     * @throws NullPointerException
     */

    public void setRating(Boolean ratingOfThisOrder){
        if(ratingOfThisOrder == null){
            throw new NullPointerException();
        }
        this.ratingOfThisOrder = ratingOfThisOrder;

    }

    /**
     * Setter for driverId
     * @param driverId a positive integer
     * @throws NullPointerException null exception
     */
    public void setDriverId(String driverId) throws Exception {
        if (driverId == null) {
            throw new NullPointerException("driverId is null.");
        } else {
            if (driverId.isEmpty()) {
                throw new Exception("Invalid driverId.");
            } else {
                this.driverId = driverId;
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
    public void setRiderId(String riderId)throws Exception{
        if(riderId == null){
            throw new NullPointerException("RiderId is null");
        } else{
            if(riderId.isEmpty()){
                throw  new Exception("Invalid riderId");
            } else{
                this.riderId = riderId;
            }
        }
    }

    /**
     * setter for setRiderId
     * @param status
     * @throws Exception null or negative exceptions
     */


    public void setStatus(Integer status){
        if(status == 1 || status == 2 || status == 3 || status == 4 || status == 5 || status == -1){
            this.status = status;
        }else{
            throw new IllegalArgumentException();
        }
    }

}




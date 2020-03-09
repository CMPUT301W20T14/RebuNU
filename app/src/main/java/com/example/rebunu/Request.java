package com.example.rebunu;

import android.location.Location;

/**
 * Request class
 * @author Lefan Wang
 * @see Location
 */

public class Request extends Record {
    private Location start;
    private Location end;
    private Integer price;
    private String riderId;

    /**
     *
     * @param start a Location object
     * @param end a Location object
     * @param price a positive integer
     * @param riderId a positive integer
     * @throws Exception null, empty or invalid number exceptions
     */

    public Request(Location start, Location end, Integer price, String riderId) throws Exception{
        this.setStart(start);
        this.setEnd(end);
        this.setPrice(price);
        this.setRiderId(riderId);
        setType(2);
    }

    public Request(Location start, Location end, Integer price, String riderId, Boolean noId) throws Exception{
        super(noId);
        this.setStart(start);
        this.setEnd(end);
        this.setPrice(price);
        this.setRiderId(riderId);
        setType(2);
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
        }
        else{
            this.riderId = riderId;
//            if(riderId > 0){
//                this.riderId = riderId;
//            }
//            else{
//                throw  new Exception("Invalid riderId");
//            }
        }
    }
}

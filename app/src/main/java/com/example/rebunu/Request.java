package com.example.rebunu;

/**
 * Request class
 * @author Lefan Wang
 * @see Location
 */

public class Request extends Record {
    private Integer status;
    private Location start;
    private Location end;
    private Integer price;
    private Integer riderId;

    /**
     *
     * @param status a integer with the value 1, 2 or 3
     * @param start a Location object
     * @param end a Location object
     * @param price a positive integer
     * @param riderId a positive integer
     * @throws Exception null, empty or invalid number exceptions
     */

    public Request(Integer status, Location start, Location end, Integer price, Integer riderId) throws Exception{
        setStart(start);
        setEnd(end);
        setPrice(price);
        setRiderId(riderId);
        setStatus(status);

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

    public Integer getStatus(){
        return status;
    }

    /**
     * setter for setStart
     * @param start a Location object
     * @throws NullPointerException null exception
     */
    public void setStart(Location start) throws NullPointerException{
        if (start.getLatitude() != null & start.getLongitude() != null){
            this.start = start;
        }
        throw new NullPointerException("Invalid start");

    }

    /**
     * setter for setEnd
     * @param end a Location object
     * @throws NullPointerException null exception
     */
    public void setEnd(Location end) throws NullPointerException{
        if (end.getLatitude() != null & end.getLongitude() != null){
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

    /**
     * setter for status
     * @param status a integer with the value 1, 2 or 3
     * @throws Exception null or not from (1,2,3) exceptions
     */
    public void setStatus(Integer status) throws Exception{
        if(status == null){
            throw new NullPointerException("Status is null");
        }
        else{
            if(status == 1 || status == 2 || status ==3){
                this.status = status;
            }
            else{
                throw new Exception("Invalid status");
            }
        }
    }
}

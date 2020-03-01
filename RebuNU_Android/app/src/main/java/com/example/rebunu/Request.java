package com.example.rebunu;

public class Request extends Record {
    private int status;
    private Location start;
    private Location end;
    private int price;
    private int riderId;

    Location getStart() {
        return this.start;
    }

    Location getEnd() {
        return this.end;
    }

    int getPrice() {
        return this.price;
    }

    void setStart(Location start){
        this.start = start;
    }

    void setEnd(Location end){
        this.end = end;
    }

    void setPrice(int price){
        this.price = price;
    }

    int getRiderId(){return this.riderId;}

    void setRiderId(int riderId){
        this.riderId = riderId;
    }

    int getStatus(){return this.status;}

    void setStatus(int status){
        this.status = status;
    }

}

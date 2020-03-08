package com.example.rebunu;

import android.location.Location;

/**
 * @author Shanyan Xue, Zijian Xi
 */
public class Rider extends User{

    public Rider() throws Exception {}

    public Integer CreateRequest (Location start, Location end, Integer price, Integer riderId) throws
            Exception {
        try {
            Request newRequest = new Request(start, end, price, riderId);
            Database db = new Database();
            db.add(newRequest);
            return newRequest.getId();
        } catch (Exception e) {throw e;}
    }

    public Request FetchRequest(){
        Database db = new Database();
        Request fetchRequest = db.getRequestIdByRiderId(getUserId());
        return fetchRequest;
    }

    public void cancelRequest() throws Exception{
        Database db = new Database();
        Order order = new Order();
        Request request = FetchRequest();
        if(request != null) {
            Request r = (Request)request;
            order.setStart(r.getStart());
            order.setEnd(r.getEnd());
            order.setPrice(r.getPrice());
            order.setRiderId(r.getRiderId());
            order.setStatus(1);
            db.add((Record) order);
            db.delete(request);
        } else {
            throw new Exception("Request not exist");
        }
    }

    public void confirm(Order order) throws Exception{
        if(order != null) {
            Database db = new Database();
            order.setStatus(3);
            db.modify((Record) order);
        } else {
            throw new Exception("Order not exist");
        }
    }

    public void decline(Order order) throws Exception {
        if(order != null) {
            Database db = new Database();
            order.setStatus(2);
            db.modify((Record) order);
        } else {
            throw new Exception("Order not exist");
        }
    }
}

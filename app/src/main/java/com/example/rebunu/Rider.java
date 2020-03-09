package com.example.rebunu;

import android.location.Location;

/**
 * @author Shanyan Xue, Zijian Xi
 */
public class Rider extends User{

    public Rider(Boolean force) throws Exception {
        super(force);
    }

    public Request CreateRequest (Location start, Location end, Integer price, String riderId) throws
            Exception {
        Request newRequest = new Request(start, end, price, riderId);
        Database db = new Database();
        String id = db.add(newRequest);
        newRequest.setId(id);
        return newRequest;
    }

//    public Request FetchRequest(){
//        Database db = new Database();
//        Integer requestId = db.getRequestIdByRiderId(getUserId());
//        return (Request) db.queryById(requestId, 2);
//    }
//
//    public void cancelRequest() throws Exception{
//        Database1 db = new Database1();
//        Order order = new Order();
//        Request request = FetchRequest();
//        if(request != null) {
//            order.setStart(request.getStart());
//            order.setEnd(request.getEnd());
//            order.setPrice(request.getPrice());
//            order.setRiderId(request.getRiderId());
//            order.setStatus(1);
//            db.add((Record) order);
//            db.delete(request);
//        } else {
//            throw new Exception("Request not exist");
//        }
//    }
//
//    public void confirm(Order order) throws Exception{
//        if(order != null) {
//            Database1 db = new Database1();
//            order.setStatus(3);
//            db.modify((Record) order);
//        } else {
//            throw new Exception("Order not exist");
//        }
//    }
//
//    public void decline(Order order) throws Exception {
//        if(order != null) {
//            Database1 db = new Database1();
//            order.setStatus(2);
//            db.modify((Record) order);
//        } else {
//            throw new Exception("Order not exist");
//        }
//    }
}

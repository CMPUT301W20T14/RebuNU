package com.example.rebunu;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Shanye Xue, Zijian Xi
 * ...
 */
public class Driver extends User {
    public Driver() throws Exception {}

    public ArrayList<Integer> getNearAvailableRequest(){
        Database db = new Database();
        HashMap<Integer, ArrayList<Double>> queryResult;
        ArrayList<Integer> filteredResult = new ArrayList<>();
        float[] distance = new float[1];
        ArrayList<Double> coords;

//Whether the db.getAllRequestLocation() return null

        queryResult = db.getAllRequestLocation();
        for(Integer s: queryResult.keySet()) {
            coords = queryResult.get(s);
            try {
                Location.distanceBetween(coords.get(0), coords.get(1),
                        coords.get(2), coords.get(3), distance);
                if(distance[0] < 5000.0) {
                    filteredResult.add(s);
                }
            } catch (Exception ignored){}
        }
        return filteredResult;
    }

    public Order acceptRequest(Integer idx) throws Exception{
        Database db = new Database();
        Record queryResult = db.queryById(idx, 2);
        if(queryResult != null) {
            Request r = (Request)queryResult;
            Order order = new Order();
            order.setStart(r.getStart());
            order.setEnd(r.getEnd());
            order.setPrice(r.getPrice());
            order.setRiderId(r.getRiderId());
            order.setDriverId(getUserId());
            db.add((order));
            db.delete(queryResult);
            return order;
        } else {
            throw new Exception("Request not exist");
        }
    }
}



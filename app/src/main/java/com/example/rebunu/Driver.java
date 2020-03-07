package com.example.rebunu;


public class Driver {
    private ArrayList<Request> nearAvaliableRequest;
    private Request acceptRequest;


    public ArrayList<Request> getNearAvaliableRequest(){
        return nearAvaliableRequest;
    }

    public Request acceptRequest(){
        return acceptRequest;
    }
}


package com.example.rebunu;

import android.location.Location;

public class User {
    private Location location;
    private Integer Id;

    public User(Location location, int Id) throws Exception{
        setLocation(location);
    }

    public Location getLocation(){return location;}


    public void setLocation()throws Exception{
        if (location != null){
            this.location = location;
            return;
        }
        throw new Exception("location is null");
    }


    public Integer getProfile(){return Profile;}


}


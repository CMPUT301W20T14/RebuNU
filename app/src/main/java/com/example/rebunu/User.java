package com.example.rebunu;

import android.location.Location;

/**
 * @author Shanye Xue, Zijian Xi
 * ...
 */
public class User {
    private Location location;
    private Integer userId;

    /**
     * @param location longtitude and latitude
     * @param Id positive integers
     * @throws Exception null exception
     */
    public User(Location location, Integer Id) throws Exception {
        setLocation(location);
        if(Id != null) {
            if(Id >0) {
                this.userId = Id;
            } else {
                throw new Exception("Invalid userId");
            }
        } else {
            throw new Exception("Null userId");
        }
    }

    protected User() throws Exception{
        if(location == null || userId == null) {
            throw new Exception("You cannot consturct empty User");
        }
    }

    public Location getLocation(){
        return location;
    }


    public void setLocation(Location location) throws Exception{
        if (location != null){
            this.location = location;
            return;
        }
        throw new Exception("location is null");
    }


    public Profile getProfile() {
        Profile profile;
        Database db = new Database();
        profile = (Profile) db.queryById(getUserId(), 1);
        return profile;
    }

    public Integer getUserId() {
        return userId;
    }

}


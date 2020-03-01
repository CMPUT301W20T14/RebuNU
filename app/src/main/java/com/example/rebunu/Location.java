package com.example.rebunu;

public class Location {
    private double longitude;
    private double latitude;

    Location(double longitude , double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    void setLongitude(double longitude){
        this.longitude = longitude;
    }

    void setLatitude(double latitude){
        this.latitude = latitude;
    }

    double getLongitude() {
        return this.longitude;
    }

    double getLatitude() {
        return this.latitude;
    }
}

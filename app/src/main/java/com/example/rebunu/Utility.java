package com.example.rebunu;

import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utility {
    public static Integer getEstimatePrice(Request request, @Nullable Float priceFactor) {
        float[] dis = new float[1];
        Integer price = 0;

        Location start = request.getStart();
        Location end = request.getEnd();
        Location.distanceBetween(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude(), dis);

        if(priceFactor != null) {
            price = (Integer)((Float)(dis[0] * priceFactor)).intValue();
        } else {
            price = (Integer)((Float)(dis[0])).intValue();
        }
        return price;
    }

    public static Location latLngToLocation(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLatitude());
    }

    public static ArrayList<Location> mockSurrounding(){
        ArrayList<Location> nearByCars = new ArrayList<>();

        double[] lat = {53.525564, 53.525296, 53.525695, 53.526441, 53.525612};
        double[] lng = {-113.521412, -113.520166, -113.521335, -113.519962, -113.521459};

        for(int i=0; i<5; i++) {
            Location location = new Location("");
            location.setLatitude(lat[i]);
            location.setLongitude(lng[i]);
            nearByCars.add(location);
        }
        return nearByCars;
    }
}

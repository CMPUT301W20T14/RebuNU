package com.example.rebunu;

import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * utility method
 * @author zijian xi
 */

public class Utility {
    public static Map<String, Object> dataMap = null;
    public static String dataId = null;

    /**
     * estimate price based on distance
     * @param start
     * @param end
     * @param priceFactor
     * @return price
     */

    public static Integer getEstimatePrice(Location start, Location end, @Nullable Integer priceFactor) {
        float[] dis = new float[1];
        Integer price = 0;

        Location.distanceBetween(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude(), dis);

        if(priceFactor != null) {
            price = (Float.valueOf((dis[0] / 1000f) * priceFactor)).intValue();
        } else {
            price = (Float.valueOf((dis[0] / 1000f) * 100)).intValue();
        }
        return 6 + price;
    }

    /**
     * convert latlng to location
     * @param latLng
     * @return location
     */

    public static Location latLngToLocation(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    /**
     * convert location to latlng
     * @param location
     * @return latlng
     */

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * convert GeoPoint to location
     * @param geoPoint
     * @return location
     */

    public static Location geoPointToLocation(GeoPoint geoPoint) {
        Location location = new Location("");
        location.setLatitude(geoPoint.getLatitude());
        location.setLongitude(geoPoint.getLongitude());
        return location;
    }

    /**
     * get nearby cars
     * @return nearByCars
     */

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

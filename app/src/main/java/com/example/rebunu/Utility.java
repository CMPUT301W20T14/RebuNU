package com.example.rebunu;

import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public static Map<String, Object> dataMap = null;
    public static String dataId = null;

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

    public static Location latLngToLocation(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static Location geoPointToLocation(GeoPoint geoPoint) {
        Location location = new Location("");
        location.setLatitude(geoPoint.getLatitude());
        location.setLongitude(geoPoint.getLongitude());
        return location;
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

    public static void pushMockedRequestsToDatabase() {
        ArrayList<Location> nearByCars = mockSurrounding();
        Database db = new Database();
        for(int i=0; i<5; i++) {
            try {
                Request request = new Request(nearByCars.get(i), nearByCars.get(4-i), i+6, "mocked");
                db.add(request);
            } catch (Exception ignored){}
        }
    }
}

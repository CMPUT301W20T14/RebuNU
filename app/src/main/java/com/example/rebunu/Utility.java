package com.example.rebunu;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * utility method
 * @author zijian xi
 */

public class Utility {
    public static Map<String, Object> dataMap = null;
    public static String dataId = null;
    public static Location currentLocation = null;

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

    public static String parseOrderStatus(Integer status, Boolean isVerbose) {
        String stat = "complete";
        if(!isVerbose){
            if(status != 5) {
                stat = "Incomplete";
            } else {
                stat = "Complete";
            }
        } else {
            if(status == -1) {
                stat = "Order accepted, but no further action has been taken";
            } else if(status == 1) {
                stat = "Order cancelled by rider before driver pick up";
            } else if(status == 2) {
                stat = "Order cancelled by rider after confirmed by driver";
            } else if (status == 3) {
                stat = "Order confirmed by rider and driver";
            } else if (status == 4) {
                stat = "Order cancelled by rider after order confirmed by rider and driver";
            } else if (status == 5) {
                stat = "Order complete successfully";
            } else {
                stat = "Order incomplete";
            }
        }
        return stat;
    }

    public static Order documentSnapshotToOrder(QueryDocumentSnapshot document) {
        Order order = new Order();
        try {
            String orderId = (String) document.getId();
            Boolean rating = (Boolean) document.getData().get("rating");
            String driverId = (String) document.getData().get("driverId");
            Long status = (Long) document.getData().get("status");
            ArrayList<GeoPoint> pos = (ArrayList<GeoPoint>) document.getData().get("pos");
            Long price = (Long) document.getData().get("price");
            String riderId = (String) document.getData().get("riderId");

            if(rating == null) {
                rating = true;
            }

            if(driverId == null) {
                driverId = "Not applicable";
            }

            order.setId(orderId);
            order.setDriverId(driverId);
            order.setRating(rating);
            order.setStatus(status.intValue());
            order.setPrice(price.intValue());
            order.setRiderId(riderId);

            GeoPoint start = pos.get(0);
            GeoPoint end = pos.get(1);
            order.setStart(Utility.latLngToLocation(new LatLng(start.getLatitude(), start.getLongitude())));
            order.setEnd(Utility.latLngToLocation(new LatLng(end.getLatitude(), end.getLongitude())));
        } catch (Exception ignored) {}
        return order;
    }

    public static String LocationToAddress(Location pos, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = String.format("%.5f", pos.getLatitude()) + ", " + String.format("%.5f", pos.getLongitude());
        try {
            List<Address> addresses = geocoder.getFromLocation(pos.getLatitude(), pos.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String[] splitedAddress = Objects.requireNonNull(address.split(","));
            result = splitedAddress[0] + ", " + splitedAddress[1];
        } catch (Exception ignored) {}
        return result;
    }

    public static HashMap<String, String> processOrderToStringHashMap(Order order, Context context) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("orderId", order.getId());
        stringHashMap.put("start", Utility.LocationToAddress(order.getStart(), context));
        stringHashMap.put("end", Utility.LocationToAddress(order.getEnd(), context));
        stringHashMap.put("riderId", order.getRiderId());
        stringHashMap.put("price", order.getPrice().toString());
        stringHashMap.put("status", Utility.parseOrderStatus(order.getStatus(), true));
        stringHashMap.put("driverId", order.getDriverId());
        if(order.getDriverId().equals("Not applicable")) {
            stringHashMap.put("rating", "Not applicable");
        } else {
            if(order.getRating()) {
                stringHashMap.put("rating", "Good");
            } else {
                stringHashMap.put("rating", "Bad");
            }
        }
        return stringHashMap;
    }
}

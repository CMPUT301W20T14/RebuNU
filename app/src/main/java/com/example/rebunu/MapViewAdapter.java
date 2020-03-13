package com.example.rebunu;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zijian Xi
 * @see com.google.android.gms.maps.MapView
 * MapView Adapter
 * Can update request marker on a MapView
 */
@SuppressWarnings("unchecked")
public class MapViewAdapter {
    public static void updateRequestOnMap(GoogleMap gmap, Map<String, Map<String, Object>> recordIdToDataMap, Location currentLocation, Context applicationContext, Resources resources) {
        float[] distance = new float[1];
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // if no request to show, just relocate camera to current location and zoom out
        if (recordIdToDataMap.isEmpty()) {
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 13));
            return;
        }

        for (Map<String, Object> dataMap : recordIdToDataMap.values()) {
            ArrayList<GeoPoint> pos = (ArrayList<GeoPoint>) Objects.requireNonNull(dataMap.get("pos"));
            dataMap.put("state", "multiple");

            // calculate distance between request beginning location and current location
            Location.distanceBetween(pos.get(0).getLatitude(), pos.get(0).getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), distance);
            // update request icon on map
            try {
                Marker marker = gmap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.available_request_icon_small))
                        .position(new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude()))
                        .title("Distance: " + Double.valueOf(distance[0]).intValue() + " Meters. " +
                                "Geolocation: (" + pos.get(0).getLatitude() + ", " + pos.get(0).getLongitude() + ")"));
                dataMap.put("marker", marker);
                marker.setTag(dataMap);
                builder.include(marker.getPosition());
            } catch (Exception e) {
                Toast.makeText(applicationContext, "Update request on map failed, due to: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
            // https://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers answered Feb 12 '13 at 8:53 by andr
            // https://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers answered Jun 24 '16 at 12:19 by Zumry Mohamed
            // zoom to show all the markers, auto bounding and fitting
            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), (int) (resources.getDisplayMetrics().widthPixels * 0.30)));
        }
    }
}

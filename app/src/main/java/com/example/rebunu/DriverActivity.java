package com.example.rebunu;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Driver screen
 * @author Zijian Xi, Zihao Huang
 */

// Since I am clear that I am using correct down-casting, I will suppress type unchecked warnings.
@SuppressWarnings("unchecked")
public class DriverActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Reference: https://www.zoftino.com/android-mapview-tutorial posted on November 14, 2017
    private MapView mapView;
    private GoogleMap gmap;
    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private String floatingButtonStatus = "SHOW_ALL_REQUEST";
    private LocationManager locationManager;
    private Criteria criteria;
    private Map<String, Map<String, Object>> recordIdToDataMap = new HashMap<>();

    public void updateRequestOnMap() {
        float[] distance = new float[1];
        Location currentLocation;

        // check if we have permission, if not, ask for permission
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, TAG_CODE_PERMISSION_LOCATION);
        }
        currentLocation = Objects.requireNonNull(locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false))));

        for(Map<String, Object> dataMap: recordIdToDataMap.values()) {
            ArrayList<GeoPoint> pos = (ArrayList<GeoPoint>) Objects.requireNonNull(dataMap.get("pos"));
            //Integer price = ((Long) Objects.requireNonNull(dataMap.get("price"))).intValue();
            //String riderId = (String) Objects.requireNonNull(dataMap.get("riderId"));
            //String recordId = (String) Objects.requireNonNull(dataMap.get("recordId"));
            dataMap.put("state", "multiple");

            // calculate distance between request beginning location and current location
            Location.distanceBetween(pos.get(0).getLatitude(), pos.get(0).getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), distance);
            // update request icon on map
            try {
                Marker marker = gmap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.available_request_icon_small))
                    .position(new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude()))
                    .title("Distance: " + Double.valueOf(distance[0]).intValue() + " Meters. " +
                        "Geolocation: (" + pos.get(0).getLatitude() + ", " + pos.get(0).getLongitude()+ ")"));
                dataMap.put("marker", marker);
                marker.setTag(dataMap);
            } catch (Exception e){
               Toast.makeText(getApplicationContext(), "Update request on map failed, due to: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        // initialise database for later query
        Database db = new Database();

        Button button_searchNearby_floating = findViewById(R.id.driver_button_searchNearby_floating);
        mapView = findViewById(R.id.driver_mapView);

        button_searchNearby_floating.setOnClickListener(v -> {
            // before update, clear all current markers on the map
            gmap.clear();
            // and clear their selection state(ie, selected will show with darker color, unselected will show with lighter color)
            recordIdToDataMap.clear();

            // check if we have permission, if not, ask for permission
            if (!(ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(DriverActivity.this, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, TAG_CODE_PERMISSION_LOCATION);
            }
            // get current location
            Location currentLocation = Objects.requireNonNull(locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false))));
            // and relocate camera to show current location
            gmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));

            // search all request with center = current location and radius = 5 kilometers
            GeoQuery geoQuery = db.geoFirestore.queryAtLocation(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()), 5.0);
            geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                @Override
                public void onDocumentEntered(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {
                    // if a request satisfy our requirements
                    Map<String, Object> dataMap = Objects.requireNonNull(documentSnapshot.getData());
                    dataMap.put("recordId", documentSnapshot.getId());
                    recordIdToDataMap.put(documentSnapshot.getId(), dataMap);
                    updateRequestOnMap();
                }
                @Override
                public void onDocumentExited(@NonNull DocumentSnapshot documentSnapshot) {
                    // if a request no longer satisfy our requirements, ie, cancelled or out of radius
                    Toast.makeText(getApplicationContext(), documentSnapshot.getId(), Toast.LENGTH_SHORT).show();
                    try {
                        recordIdToDataMap.remove(documentSnapshot.getId());
                        updateRequestOnMap();
                    } catch (Exception ignored){}
                }
                @Override
                public void onDocumentMoved(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {}
                @Override
                public void onDocumentChanged(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {}
                @Override
                public void onGeoQueryReady() {}
                @Override
                public void onGeoQueryError(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Searching failed, due to: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Reference: https://developer.android.com/training/permissions/requesting.html Posted on 2019-12-27.
//        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
//                        PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(this, new String[] {
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            }, TAG_CODE_PERMISSION_LOCATION);
//        }
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

//        button_searchNearby_floating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                button_searchNearby_floating.setVisibility(button_searchNearby_floating.GONE);
//
//            }
//        });

//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//            if (intentResult != null) {
//                if (intentResult.getContents() == null){
//
//                }
//                else {
//
//                }
//            }
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        // minimal zoom out scale
        gmap.setMinZoomPreference(16);
        gmap.setMyLocationEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        // compass will only show if map has been rotated
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        // check if we have permission, if not, ask for permission
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, TAG_CODE_PERMISSION_LOCATION);
        }
        // get current location
        // Reference: https://stackoverflow.com/questions/36878087/get-current-location-lat-long-in-android-google-map-when-app-start Posted on Apr 27 '16 at 21:04 by Dijkstra
        Location currentLocation = Objects.requireNonNull(locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false))));

        // set current location in the middle of the camera, or to say, the visible area's center is user's current location
        gmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
        gmap.setOnMarkerClickListener(marker -> {
            Map<String, Object> dataMap = Objects.requireNonNull((Map<String, Object>) marker.getTag());
            String state = Objects.requireNonNull((String) dataMap.get("state"));
            if(state.equals("multiple")) {
                // no matter what, we know what marker has been selected :)
                // and retrieve bind information correspondingly
                ArrayList<GeoPoint> pos = Objects.requireNonNull((ArrayList<GeoPoint>) dataMap.get("pos"));
                Integer price = Objects.requireNonNull((Long) dataMap.get("price")).intValue();
                String recId = Objects.requireNonNull((String) dataMap.get("recordId"));
                String riderId = Objects.requireNonNull((String) dataMap.get("riderId"));
                GeoPoint start = pos.get(0);
                GeoPoint end = pos.get(1);

                Button button_searchNearby_floating = findViewById(R.id.driver_button_searchNearby_floating);
                ConstraintLayout layout_request_accepted = findViewById(R.id.driver_layout_request_accepted);
                Button button_hide_request_accepted = findViewById(R.id.driver_button_hide_request_accepted);
                TextView textview_fromWhere_request_accepted = findViewById(R.id.driver_textview_fromWhere_request_accepted);
                TextView textview_to_request_accepted = findViewById(R.id.driver_textview_to_request_accepted);
                TextView textview_estimatedRateNumeric_request_accepted = findViewById(R.id.driver_textview_estimatedRateNumeric_request_accepted);
                Button button_accept_request_accepted = findViewById(R.id.driver_button_accept_request_accepted);

                // this is for driver to accepted request
                button_accept_request_accepted.setOnClickListener(v -> {
                    Order order = new Order();
                    try {
                        // initialise a new Order object, we do not set status now, because it is not decided by rider to accept
                        order.setStart(Utility.geoPointToLocation(start));
                        order.setEnd(Utility.geoPointToLocation(end));
                        order.setPrice(price);
                        order.setDriverId((String) Objects.requireNonNull(getIntent().getExtras()).get("profileId"));
                        order.setRiderId(riderId);
                        order.setId(recId);

                        // update the view
                        button_hide_request_accepted.setVisibility(View.GONE);
                        button_accept_request_accepted.setVisibility(View.GONE);
                        ProgressBar progressbar_request_accepted = findViewById(R.id.driver_progressbar_request_accepted);
                        TextView textview_confirming_request_accepted = findViewById(R.id.driver_textview_confirming_request_accepted);
                        progressbar_request_accepted.setVisibility(View.VISIBLE);
                        textview_confirming_request_accepted.setVisibility(View.VISIBLE);

                        Database db = new Database();
                        // add correspond order
                        String realId = db.add(order);
                        Toast.makeText(getApplicationContext(), recId + " <-> " + realId, Toast.LENGTH_SHORT).show();
                        // delete correspond request
                        db.deleteById(recId, 2);
                        // and monitor document
                        // a DocumentSnapshot listener, this is how we can monitor a specific record
                        db.orders.document(recId).addSnapshotListener((documentSnapshot, e) -> {
                            if(e != null){
                                //
                                return;
                            }
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                int status = ((Long) Objects.requireNonNull(Objects.requireNonNull(documentSnapshot.getData()).get("status"))).intValue();
                                if(status == 3) {
                                    // proceed
                                    Toast.makeText(getApplicationContext(), "Both agreed", Toast.LENGTH_SHORT).show();
                                } else if (status == 2) {
                                    // user cancelled the order
                                    Toast.makeText(getApplicationContext(), "User cancelled", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                button_hide_request_accepted.setOnClickListener(v -> {
                    if(floatingButtonStatus.equals("SHOW_ALL_REQUEST")) {
                        // update the view
                        layout_request_accepted.setVisibility(View.GONE);
                        button_searchNearby_floating.setVisibility(View.VISIBLE);
                        button_searchNearby_floating.performClick();
                    }
                });

                // update the view
                button_searchNearby_floating.setVisibility(View.GONE);
                layout_request_accepted.setVisibility(View.VISIBLE);

                String from = "(" + start.getLatitude() + ", " + start.getLongitude() + ")";
                String to = "(" + end.getLatitude() + ", " + end.getLongitude() + ")";
                textview_fromWhere_request_accepted.setText(from);
                textview_to_request_accepted.setText(to);
                textview_estimatedRateNumeric_request_accepted.setText(price.toString());

                // before we show pick up and drop locations, we want to make sure there is no other request marker(car icon)
                // so we clear the map view
                gmap.clear();
                Marker startMarker = gmap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick))
                        .position(new LatLng(pos.get(0).getLatitude(), pos.get(0).getLongitude()))
                        .title("start"));
                Marker endMarker = gmap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop))
                        .position(new LatLng(pos.get(1).getLatitude(), pos.get(1).getLongitude()))
                        .title("end"));
                Map<String, Object> tag = new HashMap<>();
                tag.put("state", "two");
                startMarker.setTag(tag);
                endMarker.setTag(tag);
            } else {
                marker.showInfoWindow();
            }
            return true;
        });
    }
}



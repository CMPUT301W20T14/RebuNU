package com.example.rebunu;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Driver screen
 * @author Zijian Xi, Zihao Huang
 */

// Since we are clear that I am using correct down-casting, we will suppress type "unchecked" warnings.
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
    private Location currentLocation = Utility.currentLocation;
    private GeoQuery geoQuery;
    //DrawerLayout
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Boolean prevFlag = false;
    private int prevButtonState;
    private int prevLayoutState;
    //DrawerLayout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        // ########### ONLY FOR TEST ###########
        // Utility.pushMockedRequestsToDatabase();
        // #####################################

        //DrawerLayout
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        Button button_searchNearby_floating = findViewById(R.id.driver_button_searchNearby_floating);
        mapView = findViewById(R.id.driver_mapView);
        TextView drawer_header_textview_username;

        // initialise database for later query
        Database db = new Database();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                prevFlag = true;
                prevButtonState = button_searchNearby_floating.getVisibility();
                if(button_searchNearby_floating.getVisibility() == VISIBLE) {
                    button_searchNearby_floating.setVisibility(GONE);
                }
                ConstraintLayout layout_request_accepted = findViewById(R.id.driver_layout_request_accepted);
                prevLayoutState = layout_request_accepted.getVisibility();
                if(layout_request_accepted.getVisibility() == VISIBLE) {
                    layout_request_accepted.setVisibility(GONE);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                button_searchNearby_floating.setVisibility(prevButtonState);
                ConstraintLayout layout_request_accepted = findViewById(R.id.driver_layout_request_accepted);
                layout_request_accepted.setVisibility(prevLayoutState);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //get header
        View harderLayout = navigationView.getHeaderView(0);
        //get TextView
        drawer_header_textview_username = harderLayout.findViewById(R.id.drawer_header_textview_username);

        // get username
        Database dbPro = new Database();
        String driverId = (String) Objects.requireNonNull(getIntent().getExtras()).get("profileId");
        dbPro.profiles.document(driverId).addSnapshotListener((documentSnapshot, e) -> {
            if(e != null){
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()){
                drawer_header_textview_username.setText((String) documentSnapshot.get("name"));
            }
        });
        // Menu
        navigationView.setNavigationItemSelectedListener(item -> {

            switch(item.getTitle().toString()){
                case "Profile":
                    Intent userInformationIntent = new Intent(DriverActivity.this, UserInformationActivity.class);
                    userInformationIntent.putExtra("userId", driverId);
                    startActivity(userInformationIntent);
                    break;

                case "Order":

                    Intent orderListActivity = new Intent(DriverActivity.this, OrderListActivity.class);
                    orderListActivity.putExtra("userId",driverId);
                    orderListActivity.putExtra("role", true);
                    startActivity(orderListActivity);

                    break;

                case "Logout":
                    finish();
            }
            return false;
        });



        button_searchNearby_floating.setOnClickListener(v -> {
            if (floatingButtonStatus.equals("SHOW_ALL_REQUEST")) {
                // before update, clear all current markers on the map
                gmap.clear();
                // and clear their selection state(ie, selected will show with darker color, unselected will show with lighter color)
                recordIdToDataMap.clear();

                // check if we have permission, if not, ask for permission
                if (!(ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(DriverActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, TAG_CODE_PERMISSION_LOCATION);
                }
                // get current location
                try {
                    currentLocation = Objects.requireNonNull(locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false))));
                } catch (Exception ignored) {
                    currentLocation = Utility.currentLocation;
                }
                // and relocate camera to show current location
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));

                // search all request with center = current location and radius = 5 kilometers
                geoQuery = db.geoFirestore.queryAtLocation(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()), 5.0);
                geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                    @Override
                    public void onDocumentEntered(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {
                        // if a request satisfy our requirements
                        Map<String, Object> dataMap = Objects.requireNonNull(documentSnapshot.getData());
                        dataMap.put("recordId", documentSnapshot.getId());
                        recordIdToDataMap.put(documentSnapshot.getId(), dataMap);
                        gmap.clear();
                        //updateRequestOnMap();
                        MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                    }

                    @Override
                    public void onDocumentExited(@NonNull DocumentSnapshot documentSnapshot) {
                        // if a request no longer satisfy our requirements, ie, cancelled or out of radius
                        try {
                            recordIdToDataMap.remove(documentSnapshot.getId());
                            gmap.clear();
                            //updateRequestOnMap();
                            MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onDocumentMoved(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {
                    }

                    @Override
                    public void onDocumentChanged(@NonNull DocumentSnapshot documentSnapshot, @NonNull GeoPoint geoPoint) {
                    }

                    @Override
                    public void onGeoQueryReady() {
                        if(recordIdToDataMap.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "No request found near your location.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onGeoQueryError(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "A problem occurs during searching, please retry." + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if(floatingButtonStatus.equals("ON_THE_WAY")) {
                button_searchNearby_floating.setText(R.string.search_nearby);
                button_searchNearby_floating.setVisibility(GONE);
                ConstraintLayout layout_request_accepted = findViewById(R.id.driver_layout_request_accepted);
                layout_request_accepted.setVisibility(VISIBLE);
            }
        });
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null){
                Toast.makeText(getApplicationContext(), "Scanning cancelled.", Toast.LENGTH_SHORT).show();
            }
            else {
                // driver, rider, price
                String raw = intentResult.getContents();
                String[] processed = raw.split(" ");
                String driverId = processed[0];
                String riderId = processed[1];
                Integer price = Integer.parseInt(processed[2]);
                new Database().transaction(driverId, riderId, price);
                Toast.makeText(getApplicationContext(), "Payment successful.", Toast.LENGTH_SHORT).show();
                Button accept_request_scan_to_get_paid =  findViewById(R.id.driver_button_accept_request_scan_to_get_paid);
                ConstraintLayout layout_request_accepted = findViewById(R.id.driver_layout_request_accepted);
                Button button_accept_request_accepted = findViewById(R.id.driver_button_accept_request_accepted);
                Button button_hide_request_accepted = findViewById(R.id.driver_button_hide_request_accepted);
                Button button_searchNearby_floating = findViewById(R.id.driver_button_searchNearby_floating);
                accept_request_scan_to_get_paid.setVisibility(GONE);
                layout_request_accepted.setVisibility(GONE);
                button_accept_request_accepted.setVisibility(VISIBLE);
                button_hide_request_accepted.setVisibility(VISIBLE);
                button_searchNearby_floating.setText(R.string.search_nearby);
                floatingButtonStatus = "SHOW_ALL_REQUEST";
                button_searchNearby_floating.setVisibility(VISIBLE);
                gmap.clear();
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

        gmap = googleMap;
        // minimal zoom out scale
        gmap.setMinZoomPreference(10);
        gmap.setMyLocationEnabled(true);

        // ui setting for google map view
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        // compass will only show if map has been rotated
        uiSettings.setCompassEnabled(true);
        View button_compass = mapView.findViewWithTag("GoogleMapCompass");
        View button_location = mapView.findViewWithTag("GoogleMapMyLocationButton");
        RelativeLayout.LayoutParams params_my_location = (RelativeLayout.LayoutParams) button_location.getLayoutParams();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button_compass.getLayoutParams();

        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
        params.topMargin = params_my_location.topMargin + 120 + params_my_location.topMargin;
        params.rightMargin = params_my_location.rightMargin + 5;
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        // get current location
        // Reference: https://stackoverflow.com/questions/36878087/get-current-location-lat-long-in-android-google-map-when-app-start Posted on Apr 27 '16 at 21:04 by Dijkstra
        try {
            currentLocation = Objects.requireNonNull(locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false))));
        } catch (Exception e) {
            currentLocation = Utility.currentLocation;
        }
        // set current location in the middle of the camera, or to say, the visible area's center is user's current location
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
        gmap.setOnMarkerClickListener(marker -> {
            Map<String, Object> dataMap = Objects.requireNonNull((Map<String, Object>) marker.getTag());
            String state = Objects.requireNonNull((String) dataMap.get("state"));
            String recordId = Objects.requireNonNull((String) dataMap.get("recordId"));

            new Database().requests.document(recordId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // normal request
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
                        Button button_accept_request_arrived_at_destination = findViewById(R.id.driver_button_accept_request_arrived_at_destination);
                        Button button_accept_request_scan_to_get_paid = findViewById(R.id.driver_button_accept_request_scan_to_get_paid);

                        // this is for driver to accepted request
                        button_accept_request_accepted.setOnClickListener(v -> {
                            geoQuery.removeAllListeners();
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
                                button_hide_request_accepted.setVisibility(GONE);
                                button_accept_request_accepted.setVisibility(GONE);
                                ProgressBar progressbar_request_accepted = findViewById(R.id.driver_progressbar_request_accepted);
                                TextView textview_confirming_request_accepted = findViewById(R.id.driver_textview_confirming_request_accepted);
                                progressbar_request_accepted.setVisibility(View.VISIBLE);
                                textview_confirming_request_accepted.setVisibility(View.VISIBLE);

                                Database db = new Database();
                                // add correspond order
                                String realId = db.add(order);
                                // delete correspond request and monitor document
                                db.deleteById(recId, 2);
                                // a DocumentSnapshot listener, this is how we can monitor a specific record
                                db.orders.document(recId).addSnapshotListener((documentSnapshot, e) -> {
                                    if(e != null){
                                        Toast.makeText(getApplicationContext(),
                                                "A problem occurs during placing your order, please retry.",
                                                Toast.LENGTH_SHORT).show();
                                        layout_request_accepted.setVisibility(GONE);
                                        button_searchNearby_floating.setVisibility(View.VISIBLE);
                                        gmap.clear();
                                        //updateRequestOnMap();
                                        MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                                        return;
                                    }
                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                        int status = ((Long) Objects.requireNonNull(Objects.requireNonNull(documentSnapshot.getData()).get("status"))).intValue();
                                        if(status == 3) {
                                            // both agreed
                                            progressbar_request_accepted.setVisibility(GONE);
                                            textview_confirming_request_accepted.setVisibility(GONE);
                                            button_accept_request_arrived_at_destination.setVisibility(View.VISIBLE);
                                            //HERE OOO
                                            floatingButtonStatus = "ON_THE_WAY";
                                            button_hide_request_accepted.setVisibility(VISIBLE);
                                            button_accept_request_arrived_at_destination.setOnClickListener(v0 -> {
                                                button_accept_request_arrived_at_destination.setVisibility(GONE);
                                                button_accept_request_scan_to_get_paid.setVisibility(VISIBLE);
                                                order.setStatus(5);
                                                db.modifyOrder(order);
                                            });
                                            button_accept_request_scan_to_get_paid.setOnClickListener(v1 -> {
                                                // asking for camera permission
                                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
                                                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                                                intentIntegrator.initiateScan();
                                            });

                                            db.orders.document(recId).addSnapshotListener((documentSnapshot_status4, e_status4) -> {
                                                if (e_status4 != null) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "A problem occurs during placing your order, please retry.",
                                                            Toast.LENGTH_SHORT).show();
                                                    layout_request_accepted.setVisibility(GONE);
                                                    button_searchNearby_floating.setVisibility(VISIBLE);
                                                    button_accept_request_arrived_at_destination.setVisibility(GONE);
                                                    button_accept_request_accepted.setVisibility(VISIBLE);
                                                    button_hide_request_accepted.setVisibility(VISIBLE);
                                                    gmap.clear();
                                                    MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                                                    return;
                                                }
                                                if (documentSnapshot_status4 != null && documentSnapshot_status4.exists()) {
                                                    int status_4 = ((Long) Objects.requireNonNull(Objects.requireNonNull(documentSnapshot_status4.getData()).get("status"))).intValue();
                                                    if(status_4 == 4) {
                                                        Toast.makeText(getApplicationContext(), "User cancelled the request.", Toast.LENGTH_SHORT).show();
                                                        if(prevFlag) {
                                                            if (prevLayoutState == VISIBLE) {
                                                                prevLayoutState = GONE;
                                                            } else {
                                                                layout_request_accepted.setVisibility(GONE);
                                                            }
                                                        } else {
                                                            layout_request_accepted.setVisibility(GONE);
                                                        }
                                                        floatingButtonStatus = "SHOW_ALL_REQUEST";
                                                        button_searchNearby_floating.setText(R.string.search_nearby);
                                                        if(prevFlag) {
                                                            if (prevButtonState == GONE) {
                                                                prevButtonState = VISIBLE;
                                                            } else {
                                                                button_searchNearby_floating.setVisibility(VISIBLE);
                                                            }
                                                        } else {
                                                            button_searchNearby_floating.setVisibility(VISIBLE);
                                                        }
                                                        button_accept_request_arrived_at_destination.setVisibility(GONE);
                                                        button_accept_request_accepted.setVisibility(VISIBLE);
                                                        button_hide_request_accepted.setVisibility(VISIBLE);
                                                        gmap.clear();
                                                        MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                                                    }
                                                }
                                            });
                                        } else if (status == 2) {
                                            // user cancelled the order
                                            Toast.makeText(getApplicationContext(), "User cancelled the request.", Toast.LENGTH_SHORT).show();
                                            if(floatingButtonStatus.equals("SHOW_ALL_REQUEST")) {
                                                // update the view
                                                if(prevFlag) {
                                                    if (prevLayoutState == VISIBLE) {
                                                        prevLayoutState = GONE;
                                                    } else {
                                                        layout_request_accepted.setVisibility(GONE);
                                                    }
                                                } else {
                                                    layout_request_accepted.setVisibility(GONE);
                                                }

                                                if(prevFlag) {
                                                    if (prevButtonState == GONE) {
                                                        prevButtonState = VISIBLE;
                                                    } else {
                                                        button_searchNearby_floating.setVisibility(VISIBLE);
                                                    }
                                                } else {
                                                    button_searchNearby_floating.setVisibility(VISIBLE);
                                                }

                                                progressbar_request_accepted.setVisibility(GONE);
                                                textview_confirming_request_accepted.setVisibility(GONE);
                                                button_accept_request_accepted.setVisibility(View.VISIBLE);
                                                button_hide_request_accepted.setVisibility(View.VISIBLE);
                                                gmap.clear();
                                                //updateRequestOnMap();
                                                MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                                            }
                                        }
                                    }
                                });
                            } catch (Exception e){
                                Toast.makeText(getApplicationContext(), "A problem occurs during accept request, please retry." , Toast.LENGTH_SHORT).show();
                            }
                        });

                        button_hide_request_accepted.setOnClickListener(v -> {
                            // when click the cross
                            if(floatingButtonStatus.equals("SHOW_ALL_REQUEST")) {
                                // update the view
                                layout_request_accepted.setVisibility(GONE);
                                button_searchNearby_floating.setVisibility(View.VISIBLE);
                                gmap.clear();
                                //updateRequestOnMap();
                                MapViewAdapter.updateRequestOnMap(gmap, recordIdToDataMap, currentLocation, getApplicationContext(), getResources());
                            } else if(floatingButtonStatus.equals("ON_THE_WAY")) {
                                layout_request_accepted.setVisibility(GONE);
                                button_searchNearby_floating.setText("SHOW DETAIL");
                                button_searchNearby_floating.setVisibility(VISIBLE);
                                //<FLAG>
                            }
                        });

                        // update the view
                        button_searchNearby_floating.setVisibility(GONE);
                        layout_request_accepted.setVisibility(View.VISIBLE);
                        // https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude answered Feb 23 '12 at 8:09 by user370305
                        Geocoder geocoder = new Geocoder(DriverActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(start.getLatitude(), start.getLongitude(), 1);
                            String startAddress = addresses.get(0).getAddressLine(0);
                            addresses = geocoder.getFromLocation(end.getLatitude(), end.getLongitude(), 1);
                            String endAddress = addresses.get(0).getAddressLine(0);
                            String[] splitedStartAddress = Objects.requireNonNull(startAddress.split(","));
                            String[] splitedEndAddress = Objects.requireNonNull(endAddress.split(","));
                            // show only street address and province
                            textview_fromWhere_request_accepted.setText(splitedStartAddress[0] + ", " + splitedStartAddress[1]);
                            textview_to_request_accepted.setText(splitedEndAddress[0] + ", " + splitedEndAddress[1]);
                        } catch (Exception ignored){
                            // fail-safe
                            try {
                                List<Address> addresses = geocoder.getFromLocation(start.getLatitude(), start.getLongitude(), 1);
                                String startAddress = addresses.get(0).getAddressLine(0);
                                addresses = geocoder.getFromLocation(end.getLatitude(), end.getLongitude(), 1);
                                String endAddress = addresses.get(0).getAddressLine(0);
                                String[] splitedStartAddress = Objects.requireNonNull(startAddress.split(","));
                                String[] splitedEndAddress = Objects.requireNonNull(endAddress.split(","));
                                // show only street address and province
                                textview_fromWhere_request_accepted.setText(splitedStartAddress[0] + ", " + splitedStartAddress[1]);
                                textview_to_request_accepted.setText(splitedEndAddress[0] + ", " + splitedEndAddress[1]);
                            } catch (Exception failsafe){
                                // if error occurs, will show coordinates instead
                                String from = "(" + start.getLatitude() + ", " + start.getLongitude() + ")";
                                String to = "(" + end.getLatitude() + ", " + end.getLongitude() + ")";
                                textview_fromWhere_request_accepted.setText(from);
                                textview_to_request_accepted.setText(to);
                            }
                        }
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
                        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                        latLngBuilder.include(startMarker.getPosition());
                        latLngBuilder.include(endMarker.getPosition());
                        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                                (int) (getResources().getDisplayMetrics().widthPixels * 0.30)));
                    } else {
                        marker.showInfoWindow();
                    }
                } else {
                    // abnormal request, actually cancelled by rider, but due to network latency,
                    // the driver clicked that one
                    Toast.makeText(getApplicationContext(), "Request has already been cancelled by rider.", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        });
    }
}



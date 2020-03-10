package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Zijian Xi
 */
public class RiderActivity extends AppCompatActivity implements OnMapReadyCallback{
    // Reference: https://www.zoftino.com/android-mapview-tutorial posted on November 14, 2017
    private MapView mapView;
    private GoogleMap gmap;
    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private String floatingButtonStatus = "VISIBLE";
    private LocationManager locationManager;
    private Criteria criteria;

    public void updateMap(ArrayList<Location> locations) {
        gmap.clear();
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, TAG_CODE_PERMISSION_LOCATION);
        }

        Location currentLocation = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false)));
        float[] distance = new float[1];

       for(Location l: locations) {
           try {
               Location.distanceBetween(l.getLatitude(), l.getLongitude(), currentLocation.getLatitude(), currentLocation.getLongitude(), distance);
               gmap.addMarker(new MarkerOptions()
                       .position(Utility.locationToLatLng(l))
                       .title(String.valueOf(distance[0]) + "Meters"));
           } catch (Exception ignored){};
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        Toast.makeText(getApplicationContext(), (String) getIntent().getExtras().get("profileId") + getIntent().getExtras().get("role").toString(), Toast.LENGTH_SHORT).show();

        ConstraintLayout layout;
        Button button_postRequest;
        Button button_postRequest_floating;
        Button button_hide;

        layout = findViewById(R.id.postRequest_layout);
        mapView = findViewById(R.id.postRequest_mapView);
        button_postRequest = findViewById(R.id.postRequest_button_postRequest);
        button_postRequest_floating = findViewById(R.id.postRequest_button_postRequest_floating);
        button_hide = findViewById(R.id.postRequest_button_hide);

        LinearLayout EstimateRateLayout = findViewById(R.id.postRequest_estimated_rate_layout);
        EditText endText = findViewById(R.id.postRequest_edittext_to);
        EstimateRateLayout.setVisibility(LinearLayout.GONE);
        TextView EstimateRateText = findViewById(R.id.postRequest_textview_estimatedRateNumeric);
        Location test_start = Utility.latLngToLocation(new LatLng(60.00,100.00));
        Location test_end = Utility.latLngToLocation(new LatLng(65.00,105.00));



        // Reference: https://developer.android.com/training/permissions/requesting.html Posted on 2019-12-27.
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, TAG_CODE_PERMISSION_LOCATION);
        }

        layout.setVisibility(ConstraintLayout.GONE);
        button_postRequest_floating.setVisibility(Button.VISIBLE);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

        button_postRequest_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingButtonStatus.equals("VISIBLE")) {
                    layout.setVisibility(ConstraintLayout.VISIBLE);
                    button_postRequest_floating.setVisibility(Button.GONE);
                    floatingButtonStatus = "GONE";
                    Integer price = Utility.getEstimatePrice(test_start,test_end,(float)2.5);
                    endText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            EstimateRateText.setText(price);
                            EstimateRateLayout.setVisibility(LinearLayout.VISIBLE);
                        }
                    });
                }
            }
        });

        button_postRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double[] lat = {53.525564, 53.525296, 53.525695, 53.526441, 53.525612};
                double[] lng = {-113.521412, -113.520166, -113.521335, -113.519962, -113.521459};
                ArrayList<Request> rs = new ArrayList<>();
                Database db = new Database();

                //test add
//                for (Integer i = 0; i<5; i++){
//                    try{
//                        User a = new Rider(true);
//
//                        Location la = Utility.latLngToLocation(new LatLng(lat[i],lng[i]));
//                        Location lb = Utility.latLngToLocation(new LatLng(lat[4-i],lng[4-i]));
//                        Request r = ((Rider)a).CreateRequest(la,lb,10+i,i.toString());
//                        rs.add(r);
////                        Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
//
//
//                    }catch (Exception e){Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();}
//
//                }

//              test delete
//                for(Request r: rs){
//                    try{
//                        db.delete(r);
//                    }catch (Exception e){Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();}
//
//
//                }
                //delete record that not in database
//                try{
//                    Location la = Utility.latLngToLocation(new LatLng(lat[0],lng[0]));
//                    Location lb = Utility.latLngToLocation(new LatLng(lat[1],lng[1]));
//                    Request newRequest = new Request(la, lb, 67, "8");
//                    newRequest.setId("12345678");
//                    db.delete(newRequest);
//                }catch (Exception e){Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();}

                //test modify
//                try{
//                    Location la = Utility.latLngToLocation(new LatLng(lat[0],lng[0]));
//                    Location lb = Utility.latLngToLocation(new LatLng(lat[1],lng[1]));
//                    Request newRequest = new Request(la, lb, 67, "8");
//                    newRequest.setId("23rr2r43");
//                    db.modify(newRequest);
//                }catch (Exception e){Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();}

//                test register
//                try{
//                    HashMap<String, Object> test = new HashMap<>();
//                    test.put("phone","999");
//                    test.put("email", "999");
//                    test.put("balance",0);
//                    test.put("name", "999");
//                    test.put("role",true);
//                    test.put("password", "999");
//                    ArrayList<Integer> rating = new ArrayList<>();
//                    rating.add(0);
//                    rating.add(0);
//                    test.put("rating", rating);
////                    String id1 = db.register(null);
//                    String id2 = db.register(test);
////                    Toast.makeText(getApplicationContext(),id1,Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),id2,Toast.LENGTH_SHORT).show();
//                }catch(Exception e){
//                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
//                }

                //test query
//                try{
////                    String phone  = (String) db.profiles.document("8E9Kj6fiTCW70myD58On").get().getResult().get("phone");
//
//                    DocumentReference docRef = db.profiles.document("345");
//
//
//                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    Toast.makeText(getApplicationContext(),document.get("phone").toString(),Toast.LENGTH_SHORT).show();
//                                    Log.d("RebuNu", "DocumentSnapshot data: " + document.getData());
//                                } else {
//                                    Toast.makeText(getApplicationContext(),"No such record",Toast.LENGTH_SHORT).show();
//
//                                    Log.d("RebuNu", "No such document");
//                                }
//                            } else {
//                                Log.d("RebuNu", "get failed with ", task.getException());
//                            }
//                        }
//                    });
//
////                    Toast.makeText(getApplicationContext(),phone,Toast.LENGTH_SHORT).show();
//                }catch(Exception e){
//                    Toast.makeText(getApplicationContext(),"fuck",Toast.LENGTH_SHORT).show();
//                }

                //test queryById
                //try{
//                    Profile p = (Profile) db.queryById("8E9Kj6fiTCW70myD58On",1);
//                    Toast.makeText(getApplicationContext(),p.getPhone(),Toast.LENGTH_SHORT).show();
                //}catch (Exception e){
                //    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                //}




                if (floatingButtonStatus.equals("GONE")) {
                    layout.setVisibility(ConstraintLayout.GONE);
                    button_postRequest_floating.setVisibility(Button.VISIBLE);
                    floatingButtonStatus = "VISIBLE";
                }
                ArrayList<Location> location = Utility.mockSurrounding();
                updateMap(location);
            }
        });

        button_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingButtonStatus.equals("GONE")) {
                    layout.setVisibility(ConstraintLayout.GONE);
                    button_postRequest_floating.setVisibility(Button.VISIBLE);
                    floatingButtonStatus = "VISIBLE";
                }
            }
        });

        mapView.setOnClickListener(new View.OnClickListener() {
            // not sure working yet..
            @Override
            public void onClick(View v) {
                layout.setVisibility(ConstraintLayout.VISIBLE);
            }
        });
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(16);
        gmap.setMyLocationEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

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
        assert locationManager != null;
        Location currentLocation = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false)));
        assert currentLocation != null;
        double lat = currentLocation.getLatitude();
        double lon = currentLocation.getLongitude();
        LatLng cur = new LatLng(lat, lon);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(cur));
    }
}

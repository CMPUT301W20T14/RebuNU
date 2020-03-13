package com.example.rebunu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.w3c.dom.Text;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Rider screen
 * @author Zijian Xi, Zihao Huang
 */
public class RiderActivity extends AppCompatActivity implements OnMapReadyCallback{
    // Reference: https://www.zoftino.com/android-mapview-tutorial posted on November 14, 2017
    private MapView mapView;
    private GoogleMap gmap;
    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
//    private String floatingButtonStatus = "VISIBLE";
    Boolean cancell_clicked = false;
    private LocationManager locationManager;
    private Criteria criteria;
    Integer flag = 0;
    Request myRequest = null;
    String driverId = null;



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

        ConstraintLayout postRequest_layout;
        LinearLayout postRequest_estimated_rate_layout;
        ConstraintLayout wait_responding_layout;
        ConstraintLayout rider_layout_request_confirmed;
        ConstraintLayout rider_layout_request_accepted;
        ConstraintLayout rider_layout_information;
        ConstraintLayout rider_layout_qrcode;
        ConstraintLayout rider_layout_rating;


        Button button_postRequest;
        Button button_postRequest_floating;
        Button button_hide;
        Button button_tips;
        Button rider_button_tips_request_confirmed;
        Button rider_button_cancel_post_request;
        Button rider_button_hide_request_confirmed = findViewById(R.id.rider_button_hide_request_confirmed);
        Button rider_button_accept_request_confirmed = findViewById(R.id.rider_button_accept_request_confirmed);
        Button rider_button_decline_request_confirmed = findViewById(R.id.rider_button_decline_request_confirmed);
        Button rider_button_hide_request_accepted = findViewById(R.id.rider_button_hide_request_accepted);
        Button rider_button_decline_request_accepted = findViewById(R.id.rider_button_decline_request_accepted);
        Button rider_button_tips_request_accepted = findViewById(R.id.rider_button_tips_request_accepted);
        Button rider_button_contact_request_accepted = findViewById(R.id.rider_button_contact_request_accepted);
        Button rider_button_hide_information = findViewById(R.id.rider_button_hide_information);
        Button rider_button_hide_qrcode = findViewById(R.id.rider_button_hide_qrcode);
        Button rider_button_payYourTrip_rating = findViewById(R.id.rider_button_payYourTrip_rating);
        Button rider_button_hide_rating = findViewById(R.id.rider_button_hide_rating);


        TextView postRequest_textview_estimatedRateNumeric;
        TextView rider_textview_estimatedRateNumeric_request_confirmed;
        TextView rider_textview_estimatedRateNumeric_request_accepted;
        TextView rider_textview_fromWhere_request_accepted;
        TextView rider_textview_to_request_accepted;
        TextView rider_textview_name_request_accepted;
        TextView rider_textview_like_request_accepted;
        TextView rider_textview_dislike_request_accepted;
        TextView rider_textview_name_information;
        TextView rider_textview_like_information;
        TextView rider_textview_dislike_information;
//        TextView rider_textview_phone;
//        TextView rider_textview_email;
        ImageView rider_imageview_qrcode;


        EditText postRequest_edittext_from;
        EditText postRequest_edittext_to;
        EditText rider_edittext_from_request_confirmed;
        EditText rider_edittext_to_request_confirmed;



        postRequest_layout = findViewById(R.id.postRequest_layout);
        postRequest_estimated_rate_layout = findViewById(R.id.postRequest_estimated_rate_layout);
        wait_responding_layout = findViewById(R.id.wait_responding_layout);
        rider_layout_request_confirmed = findViewById(R.id.rider_layout_request_confirmed);
        rider_layout_request_accepted = findViewById(R.id.rider_layout_request_accepted);
        rider_layout_information = findViewById(R.id.rider_layout_information);
        rider_layout_qrcode = findViewById(R.id.rider_layout_qrcode);
        rider_layout_rating = findViewById(R.id.rider_layout_rating);



        mapView = findViewById(R.id.postRequest_mapView);
        button_postRequest = findViewById(R.id.postRequest_button_postRequest);
        button_postRequest_floating = findViewById(R.id.postRequest_button_postRequest_floating);
        button_hide = findViewById(R.id.postRequest_button_hide);
        button_tips = findViewById(R.id.postRequest_button_tips);
        rider_button_tips_request_confirmed = findViewById(R.id.rider_button_tips_request_confirmed);
//        button_accept = findViewById(R.id.rider_button_accept_request_accepted);
        rider_button_cancel_post_request = findViewById(R.id.rider_button_cancel_post_request);


        postRequest_textview_estimatedRateNumeric = findViewById(R.id.postRequest_textview_estimatedRateNumeric);
        rider_textview_estimatedRateNumeric_request_confirmed = findViewById(R.id.rider_textview_estimatedRateNumeric_request_confirmed);
        rider_textview_estimatedRateNumeric_request_accepted = findViewById(R.id.rider_textview_estimatedRateNumeric_request_accepted);
        rider_textview_fromWhere_request_accepted = findViewById(R.id.rider_textview_fromWhere_request_accepted);
        rider_textview_to_request_accepted = findViewById(R.id.rider_textview_to_request_accepted);
        rider_textview_name_request_accepted = findViewById(R.id.rider_textview_name_request_accepted);
        rider_textview_like_request_accepted = findViewById(R.id.rider_textview_like_request_accepted);
        rider_textview_dislike_request_accepted = findViewById(R.id.rider_textview_dislike_request_accepted);
        rider_textview_name_information = findViewById(R.id.rider_textview_name_information);
        rider_textview_like_information = findViewById(R.id.rider_textview_like_information);
        rider_textview_dislike_information = findViewById(R.id.rider_textview_dislike_information);
//        rider_textview_phone = findViewById(R.id.rider_textview_phone);
//        rider_textview_email = findViewById(R.id.rider_textview_email);
        rider_imageview_qrcode = findViewById(R.id.rider_imageview_qrcode);



        postRequest_edittext_from = findViewById(R.id.postRequest_edittext_from);
        postRequest_edittext_to = findViewById(R.id.postRequest_edittext_to);
        rider_edittext_from_request_confirmed = findViewById(R.id.rider_edittext_from_request_confirmed);
        rider_edittext_to_request_confirmed = findViewById(R.id.rider_edittext_to_request_confirmed);


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

        postRequest_layout.setVisibility(ConstraintLayout.GONE);
        postRequest_estimated_rate_layout.setVisibility(LinearLayout.GONE);
        wait_responding_layout.setVisibility(ConstraintLayout.GONE);
        rider_layout_request_confirmed.setVisibility(ConstraintLayout.GONE);
        rider_layout_request_accepted.setVisibility(ConstraintLayout.GONE);
        rider_layout_information.setVisibility(ConstraintLayout.GONE);
        rider_layout_rating.setVisibility(ConstraintLayout.GONE);
        rider_layout_qrcode.setVisibility(ConstraintLayout.GONE);

        button_postRequest_floating.setVisibility(Button.VISIBLE);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

        button_postRequest_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    postRequest_estimated_rate_layout.setVisibility(LinearLayout.GONE);
                    wait_responding_layout.setVisibility(ConstraintLayout.GONE);
                    postRequest_layout.setVisibility(ConstraintLayout.VISIBLE);
                    button_postRequest_floating.setVisibility(Button.GONE);
                    button_postRequest.setVisibility(Button.VISIBLE);
//                    rider_layout_rating.setVisibility(ConstraintLayout.VISIBLE);
//                    button_postRequest_floating.setVisibility(Button.GONE);
                    flag = 1;
                    return;
                }
                if(flag == 1){
                    postRequest_layout.setVisibility(ConstraintLayout.VISIBLE);
                    button_postRequest_floating.setVisibility(Button.GONE);
                    return;
                }
                if(flag == 2){
                    rider_layout_request_confirmed.setVisibility(ConstraintLayout.VISIBLE);
                    button_postRequest_floating.setVisibility(Button.GONE);
                    return;
                }
                if(flag == 3){
                    rider_layout_request_accepted.setVisibility(ConstraintLayout.VISIBLE);
                    button_postRequest_floating.setVisibility(Button.GONE);
                    return;
                }

            }
        });

        postRequest_edittext_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(!postRequest_edittext_from.getText().toString().isEmpty() && !postRequest_edittext_to.getText().toString().isEmpty()) {
                        String rawStart = postRequest_edittext_from.getText().toString();
                        String rawEnd = postRequest_edittext_to.getText().toString();
                        String[] splitedStart = rawStart.split(",");
                        String[] splitedEnd = rawEnd.split(",");
                        Location startPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedStart[0])),(Double.valueOf(splitedStart[1]))));
                        Location endPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedEnd[0])),(Double.valueOf(splitedEnd[1]))));
                        postRequest_textview_estimatedRateNumeric.setText(Utility.getEstimatePrice(startPos, endPos, null).toString());
                    }
                }catch (Exception ignored){
                    //Toast.makeText(getApplicationContext(), ignored.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        postRequest_edittext_to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(!postRequest_edittext_from.getText().toString().isEmpty() && !postRequest_edittext_to.getText().toString().isEmpty()) {
                        String rawStart = postRequest_edittext_from.getText().toString();
                        String rawEnd = postRequest_edittext_to.getText().toString();
                        String[] splitedStart = rawStart.split(",");
                        String[] splitedEnd = rawEnd.split(",");
                        Location startPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedStart[0])),(Double.valueOf(splitedStart[1]))));
                        Location endPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedEnd[0])),(Double.valueOf(splitedEnd[1]))));

                        postRequest_estimated_rate_layout.setVisibility(LinearLayout.VISIBLE);
                        postRequest_textview_estimatedRateNumeric.setText(Utility.getEstimatePrice(startPos, endPos, null).toString());
                    }
                }catch (Exception ignored){
                    //Toast.makeText(getApplicationContext(), ignored.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    postRequest_textview_estimatedRateNumeric.setText(new Integer(Integer.parseInt(postRequest_textview_estimatedRateNumeric.getText().toString()) + 1).toString());
                }catch (Exception e){
                    postRequest_textview_estimatedRateNumeric.setText("1");
                }
            }
        });
        rider_button_tips_request_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    rider_textview_estimatedRateNumeric_request_confirmed.setText(new Integer(Integer.parseInt(rider_textview_estimatedRateNumeric_request_confirmed.getText().toString()) + 1).toString());
                }catch (Exception e){
                    rider_textview_estimatedRateNumeric_request_confirmed.setText("1");
                }
            }
        });
        rider_button_tips_request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    rider_textview_estimatedRateNumeric_request_accepted.setText(new Integer(Integer.parseInt(rider_textview_estimatedRateNumeric_request_confirmed.getText().toString()) + 1).toString());
                    //rider_textview_estimatedRateNumeric_request_confirmed.setText(new Integer(Integer.parseInt(rider_textview_estimatedRateNumeric_request_confirmed.getText().toString()) + 1).toString());
                }catch (Exception e){
                    rider_textview_estimatedRateNumeric_request_accepted.setText("1");
                }
            }
        });

        rider_button_payYourTrip_rating.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                rider_layout_rating.setVisibility(ConstraintLayout.GONE);
                QRCode qrCode = new QRCode();
                try {
                    qrCode.setDriverId(666);
                    qrCode.setRiderId(777);
                    qrCode.setPrice(888);
                    qrCode.setContent();
                    rider_imageview_qrcode.setImageBitmap(qrCode.QRCode(qrCode.getContent()));

                }catch (Exception ignored){}
                rider_layout_qrcode.setVisibility(ConstraintLayout.VISIBLE);

            }
        });



        button_postRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancell_clicked = false;

                try {
                    if(!postRequest_edittext_from.getText().toString().isEmpty() && !postRequest_edittext_to.getText().toString().isEmpty()) {
                        String rawStart = postRequest_edittext_from.getText().toString();
                        String rawEnd = postRequest_edittext_to.getText().toString();
                        String[] splitedStart = rawStart.split(",");
                        String[] splitedEnd = rawEnd.split(",");
                        Location startPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedStart[0])),(Double.valueOf(splitedStart[1]))));
                        Location endPos = Utility.latLngToLocation(new LatLng((Double.valueOf(splitedEnd[0])),(Double.valueOf(splitedEnd[1]))));


                        String riderId = getIntent().getExtras().get("profileId").toString();

                        myRequest = new Request(startPos,endPos,Integer.parseInt(postRequest_textview_estimatedRateNumeric.getText().toString()),riderId);
                        Database dbr = new Database();
                        String id = dbr.add(myRequest);
                        myRequest.setId(id);

//                        Toast.makeText(getApplicationContext(),myRequest.getId(),Toast.LENGTH_SHORT).show();



                        Database db = new Database();
                        final DocumentReference reqRef = db.requests.document(myRequest.getId());

                        ListenerRegistration LG = reqRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("", "Listen failed.", e);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
//                                    if (flag){
//                                        Toast.makeText(getApplicationContext(),"Changed!!", Toast.LENGTH_SHORT).show();
//                                        flag = false;
//
//
//                                    }
//                                    flag = true;
//                                    Log.d("", "Current data: " + snapshot.getData());
                                } else {
//                                    Toast.makeText(getApplicationContext(),"Accepted!!", Toast.LENGTH_SHORT).show();

                                    if(!cancell_clicked){
                                        postRequest_layout.setVisibility(ConstraintLayout.GONE);
                                        rider_layout_request_confirmed.setVisibility(ConstraintLayout.VISIBLE);

                                        rider_edittext_from_request_confirmed.setText(postRequest_edittext_from.getText().toString());
                                        rider_edittext_to_request_confirmed.setText(postRequest_edittext_to.getText().toString());
                                        rider_textview_estimatedRateNumeric_request_confirmed.setText(postRequest_textview_estimatedRateNumeric.getText().toString());

                                        Database dbo = new Database();
                                        DocumentReference ordRef = dbo.orders.document(myRequest.getId());
                                        ordRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        driverId = (String) document.get("driverId");
                                                        Database dbp = new Database();
                                                        DocumentReference proRef = dbp.profiles.document(driverId);
                                                        proRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        TextView rider_textview_name_request_confirmed = findViewById(R.id.rider_textview_name_request_confirmed);
                                                                        TextView rider_textview_like_request_confirmed = findViewById(R.id.rider_textview_like_request_confirmed);
                                                                        TextView rider_textview_dislike_request_confirmed = findViewById(R.id.rider_textview_dislike_request_confirmed);

                                                                        ArrayList<Long> rating = new ArrayList<>();
                                                                        rating = (ArrayList<Long>)document.get("rating");

                                                                        rider_textview_name_request_confirmed.setText((String)document.get("name"));
                                                                        rider_textview_like_request_confirmed.setText(rating.get(0).toString());
                                                                        rider_textview_dislike_request_confirmed.setText(rating.get(1).toString());


//                                                                        Toast.makeText(getApplicationContext(),(String)document.get("name"), Toast.LENGTH_SHORT).show();
//                                                                        Toast.makeText(getApplicationContext(),((ArrayList<Integer>)document.get("rating")).get(0).toString(), Toast.LENGTH_SHORT).show();



                                                                        Log.d("", " Success");
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(),"Not found!", Toast.LENGTH_SHORT).show();
                                                                        Log.d("", "No such document");
                                                                        return;
                                                                    }
                                                                } else {
                                                                    Log.d("", "get failed with ", task.getException());
                                                                    Toast.makeText(getApplicationContext(), "Oops, little problem occured, please try again...", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            }
                                                        });

                                                        Log.d("", " Success");
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"Not found!", Toast.LENGTH_SHORT).show();
                                                        Log.d("", "No such document");
                                                        return;
                                                    }
                                                } else {
                                                    Log.d("", "get failed with ", task.getException());
                                                    Toast.makeText(getApplicationContext(), "Oops, little problem occured, please try again...", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                        });
                                    }


                                }
                            }
                        });




                    }else{
                        postRequest_edittext_from.setError(getResources().getString(R.string.invalid_input));
                        postRequest_edittext_to.setError(getResources().getString(R.string.invalid_input));
                    }
                }catch (Exception ignored){
                    Toast.makeText(getApplicationContext(), ignored.toString(), Toast.LENGTH_SHORT).show();
                }

//                double[] lat = {53.525564, 53.525296, 53.525695, 53.526441, 53.525612};
//                double[] lng = {-113.521412, -113.520166, -113.521335, -113.519962, -113.521459};
//                ArrayList<Request> rs = new ArrayList<>();
//                Database db = new Database();

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


//                if (floatingButtonStatus.equals("GONE")) {
//                    postRequest_layout.setVisibility(ConstraintLayout.GONE);
//                    button_postRequest_floating.setVisibility(Button.VISIBLE);
//                    floatingButtonStatus = "VISIBLE";
//                }
                button_postRequest.setVisibility(Button.GONE);
                wait_responding_layout.setVisibility(ConstraintLayout.VISIBLE);
                ArrayList<Location> location = Utility.mockSurrounding();
                updateMap(location);
            }
        });

        rider_button_cancel_post_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancell_clicked = true;
                Database db = new Database();
                db.delete(myRequest);
                db.modifyOrderStatus(myRequest.getId(), 1);

                postRequest_edittext_from.setText("");
                postRequest_edittext_to.setText("");
                postRequest_textview_estimatedRateNumeric.setText("");
                postRequest_layout.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                button_postRequest.setVisibility(Button.GONE);

                flag = 0;
            }
        });


        rider_button_accept_request_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database();
                db.modifyOrderStatus(myRequest.getId(), 3);
                rider_layout_request_confirmed.setVisibility(ConstraintLayout.GONE);
                rider_layout_request_accepted.setVisibility(ConstraintLayout.VISIBLE);

//                rider_edittext_from_request_confirmed.setText(postRequest_edittext_from.getText().toString());
//                rider_edittext_to_request_confirmed.setText(postRequest_edittext_to.getText().toString());
//                rider_textview_estimatedRateNumeric_request_confirmed.setText(postRequest_textview_estimatedRateNumeric.getText().toString());
                TextView rider_textview_name_request_confirmed = findViewById(R.id.rider_textview_name_request_confirmed);
                TextView rider_textview_like_request_confirmed = findViewById(R.id.rider_textview_like_request_confirmed);
                TextView rider_textview_dislike_request_confirmed = findViewById(R.id.rider_textview_dislike_request_confirmed);

//                rider_textview_fromWhere_request_accepted = findViewById(R.id.rider_textview_fromWhere_request_accepted);
//                rider_textview_to_request_accepted = findViewById(R.id.rider_textview_to_request_accepted);
//                rider_textview_name_request_accepted = findViewById(R.id.rider_textview_name_request_accepted);
//                rider_textview_like_request_accepted = findViewById(R.id.rider_textview_like_request_accepted);
//                rider_textview_dislike_request_accepted = findViewById(R.id.rider_textview_dislike_request_accepted);
//                rider_textview_estimatedRateNumeric_request_accepted

                rider_textview_fromWhere_request_accepted.setText(rider_edittext_from_request_confirmed.getText().toString());
                rider_textview_to_request_accepted.setText(rider_edittext_to_request_confirmed.getText().toString());
                rider_textview_estimatedRateNumeric_request_accepted.setText(rider_textview_estimatedRateNumeric_request_confirmed.getText().toString());

                rider_textview_name_request_accepted.setText(rider_textview_name_request_confirmed.getText().toString());
                rider_textview_like_request_accepted.setText(rider_textview_like_request_confirmed.getText().toString());
                rider_textview_dislike_request_accepted.setText(rider_textview_dislike_request_confirmed.getText().toString());




            }
        });

        rider_button_decline_request_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancell_clicked = true;
                Database db = new Database();
                db.modifyOrderStatus(myRequest.getId(), 2);

                postRequest_edittext_from.setText("");
                postRequest_edittext_to.setText("");
                postRequest_textview_estimatedRateNumeric.setText("");
                postRequest_layout.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                button_postRequest.setVisibility(Button.GONE);
                rider_layout_request_confirmed.setVisibility(ConstraintLayout.GONE);


                flag = 0;

            }
        });

        rider_button_decline_request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancell_clicked = true;
                Database db = new Database();
                db.modifyOrderStatus(myRequest.getId(), 4);

                postRequest_edittext_from.setText("");
                postRequest_edittext_to.setText("");
                postRequest_textview_estimatedRateNumeric.setText("");

                postRequest_layout.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                button_postRequest.setVisibility(Button.GONE);
                rider_layout_request_confirmed.setVisibility(ConstraintLayout.GONE);
                rider_layout_request_accepted.setVisibility(ConstraintLayout.GONE);

                flag = 0;

            }
        });


        rider_button_contact_request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_layout_information.setVisibility(ConstraintLayout.VISIBLE);
                rider_layout_request_accepted.setVisibility(ConstraintLayout.GONE);

//                TextView rider_textview_name_information;
//                TextView rider_textview_like_information;
//                TextView rider_textview_dislike_information;

//                rider_textview_name_request_accepted.setText(rider_textview_name_request_confirmed.getText().toString());
//                rider_textview_like_request_accepted.setText(rider_textview_like_request_confirmed.getText().toString());
//                rider_textview_dislike_request_accepted.setText(rider_textview_dislike_request_confirmed.getText().toString());

                rider_textview_name_information.setText(rider_textview_name_request_accepted.getText().toString());
                rider_textview_like_information.setText(rider_textview_like_request_accepted.getText().toString());
                rider_textview_dislike_information.setText(rider_textview_dislike_request_accepted.getText().toString());

                Database dbp = new Database();
                dbp.profiles.document(driverId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

//                                rider_textview_phone;
//                                rider_textview_email;
                                TextView rider_textview_phone = findViewById(R.id.rider_textview_phone);
                                TextView rider_textview_email = findViewById(R.id.rider_textview_email);
                                rider_textview_phone.setText((String) document.get("phone"));
                                rider_textview_email.setText((String) document.get("email"));



                                Log.d("", " Success");
                            } else {
                                Toast.makeText(getApplicationContext(),"Not found!", Toast.LENGTH_SHORT).show();
                                Log.d("", "No such document");
                                return;
                            }
                        } else {
                            Log.d("", "get failed with ", task.getException());
                            Toast.makeText(getApplicationContext(), "Oops, little problem occured, please try again...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        });


        button_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest_layout.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                flag = 1;
//                if (floatingButtonStatus.equals("GONE")) {
//                    postRequest_layout.setVisibility(ConstraintLayout.GONE);
//                    button_postRequest_floating.setVisibility(Button.VISIBLE);
//                    floatingButtonStatus = "VISIBLE";
//                }
            }
        });

        rider_button_hide_request_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_layout_request_confirmed.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                flag = 2;
            }
        });

        rider_button_hide_request_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_layout_request_accepted.setVisibility(ConstraintLayout.GONE);
                button_postRequest_floating.setVisibility(Button.VISIBLE);
                flag = 3;
            }
        });

        rider_button_hide_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_layout_information.setVisibility(ConstraintLayout.GONE);
                rider_layout_request_accepted.setVisibility(ConstraintLayout.VISIBLE);

            }
        });

        rider_button_hide_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rider_layout_rating.setVisibility(ConstraintLayout.GONE);

            }
        });

        rider_button_hide_qrcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rider_layout_qrcode.setVisibility(ConstraintLayout.GONE);

            }
        });


        mapView.setOnClickListener(new View.OnClickListener() {
            // not sure working yet..
            @Override
            public void onClick(View v) {
                postRequest_layout.setVisibility(ConstraintLayout.VISIBLE);
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

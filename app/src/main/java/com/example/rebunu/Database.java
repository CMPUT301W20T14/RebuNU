package com.example.rebunu;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class that create a database for this app
 * @author Zihao Huang
 * @see Record
 */

public class Database {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference signUps = db.collection("SignUps");

    CollectionReference profiles = db.collection("Records").document("1").collection("profiles");
    CollectionReference requests = db.collection("Records").document("2").collection("requests");
    CollectionReference orders = db.collection("Records").document("3").collection("orders");

    String TAG = "RebuNU";

    /**
     *  To see whether the database contain this record
     * @param record
     * @return true/false
     */

    public boolean contain(Record record){
        if(record.getType() == 1){
            try{
                DocumentReference doc = profiles.document(record.getId().toString());
            }catch (Exception e){ return false;}
        }else{
            if(record.getType() == 2){
                try{
                    DocumentReference doc = requests.document(record.getId().toString());
                }catch (Exception e){ return false;}
            }else{
                try{
                    DocumentReference doc = orders.document(record.getId().toString());
                }catch (Exception e){ return false;}
            }
        }
        return true;
    }

    /**
     * add signUp to database
     * @param id
     * @param username
     * @param password
     *
     */

    public void addSignUp(Integer id, String username, String password){
        HashMap<String, Object> signUp = new HashMap<>();
        signUp.put("id",id);
        signUp.put("username",username);
        signUp.put("password",password);
        signUps
                .document(username)
                .set(signUp);

    }

    /**
     *
     * @param username
     * @param password
     * @return true(username password match), false (otherwise)
     */

    public Boolean auth(String username, String password){
        Integer result; // if 0, then no username found; if 1, password true; if -1, password wrong;
        try{
            DocumentReference doc = signUps.document(username);
            if (doc.get().getResult().get("password").equals(password)){
                result = 1;
                return true;
            }else{
                result = -1;
                return false;
            }
        }catch (Exception e){
            result = 0;
            return false;
        }


    }

    /**
     * Adds a record to the database
     * @param record
     * @throws IllegalArgumentException
     */

    public void add(Record record){
        if(this.contain(record)){
            throw new IllegalArgumentException();
        }else{

            switch(record.getType()) {
                case 1:
                    Profile p = (Profile) record;
                    //create a document for this profile

                    HashMap<String, Object> profile = new HashMap<>();
                    profile.put("id", p.getId());
                    profile.put("phone", p.getPhone());
                    profile.put("email", p.getEmail());
                    profile.put("username", p.getUsername());
                    profile.put("balance", p.getBalance());
                    profile.put("role", p.getRole());
                    profiles
                            .document(p.getId().toString())
                            .set(profile);

                    if(p.getRating() != null){
                        //create subDocument for rating under this profile
                        DocumentReference profileSubDoc = profiles.document(p.getId().toString()).collection("ratings").document("rating");
                        HashMap<String, Integer> rating = new HashMap<>();
                        rating.put("thumbsUp", p.getRating().getThumbsUp());
                        rating.put("thumbsDown", p.getRating().getThumbsDown());
                        profileSubDoc.set(rating);

                    }

                    break;
                case 2:
                    Request r = (Request) record;
                    //create a document for this request
                    HashMap<String, Object> request = new HashMap<>();
                    request.put("id", r.getId());
                    request.put("price", r.getPrice());
                    request.put("rideId", r.getRiderId());
                    requests
                            .document(r.getId().toString())
                            .set(request);
                    //create subDocument for locations under this request
                    DocumentReference requestStartLocation = requests.document(r.getId().toString()).collection("locations").document("start");
                    HashMap<String, Double> start = new HashMap<>();
                    start.put("longitude",r.getStart().getLongitude());
                    start.put("latitude", r.getStart().getLatitude());
                    requestStartLocation.set(start);

                    DocumentReference requestEndLocation = requests.document(r.getId().toString()).collection("locations").document("end");
                    HashMap<String, Double> end = new HashMap<>();
                    end.put("longitude",r.getEnd().getLongitude());
                    end.put("latitude", r.getEnd().getLatitude());
                    requestEndLocation.set(end);

                    break;

                case 3:
                    Order o = (Order) record;
                    //create a document for this order
                    HashMap<String, Object> order = new HashMap<>();
                    order.put("id", o.getId());
                    order.put("status", o.getStatus());
                    order.put("price", o.getPrice());
                    order.put("riderId", o.getRiderId());
                    if(o.getRating() != null){
                        order.put("ratingOfThisOrder", o.getRating());
                    }
                    if(o.getDriverId() != null){
                        order.put("driverId", o.getDriverId());
                    }

                    orders
                            .document(o.getId().toString())
                            .set(order);
                    //create subDocument for locations under this order
                    DocumentReference orderStartLocation = orders.document(o.getId().toString()).collection("locations").document("start");
                    HashMap<String, Double> orderStart = new HashMap<>();
                    orderStart.put("longitude",o.getStart().getLongitude());
                    orderStart.put("latitude", o.getStart().getLatitude());
                    orderStartLocation.set(orderStart);

                    DocumentReference orderEndLocation = db.collection("Records").document(o.getId().toString()).collection("locations").document("end");
                    HashMap<String, Double> orderEnd = new HashMap<>();
                    orderEnd.put("longitude",o.getEnd().getLongitude());
                    orderEnd.put("latitude", o.getEnd().getLatitude());
                    orderEndLocation.set(orderEnd);

                    break;


            }

        }


    }

    /**
     * delete a record from the database
     * @param record
     * @throws IllegalArgumentException
     */

    public void delete(Record record){
        if(!(contain(record))){
            throw new IllegalArgumentException();
        }else{

            switch(record.getType()){
                case 1:
                    profiles
                            .document(record.getId().toString())
                            .delete();

                    break;

                case 2:
                    requests
                            .document(record.getId().toString())
                            .delete();

                    break;

                case 3:
                    orders
                            .document(record.getId().toString())
                            .delete();
                    break;

            }

        }




    }

    /**
     * modefy a record in the database
     * @param record
     * @throws IllegalArgumentException
     */

    public void modify(final Record record){
        if(!(this.contain(record))){
            throw new IllegalArgumentException("No such record");
        }else{

            switch(record.getType()) {
                case 1:
                    Profile p = (Profile) record;
                    final DocumentReference proDocRef = profiles.document(record.getId().toString());

                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(proDocRef);

                            transaction.update(proDocRef, "phone", p.getPhone());
                            transaction.update(proDocRef, "email", p.getEmail());
                            transaction.update(proDocRef, "username", p.getUsername());
                            transaction.update(proDocRef, "balance", p.getBalance());
                            transaction.update(proDocRef, "role", p.getRole());

                            if(p.getRating() != null){
                                HashMap<String, Integer> rating = new HashMap<>();
                                rating.put("thumbsUp", p.getRating().getThumbsUp());
                                rating.put("thumbsDown", p.getRating().getThumbsDown());
                                proDocRef.collection("ratings").document("rating").set(rating);

                            }

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Transaction success!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Transaction failure.", e);
                                }
                            });

                    break;

                case 2:
                    final DocumentReference reqDocRef = requests.document(record.getId().toString());
                    Request r = (Request)record;

                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(reqDocRef);
                            transaction.update(reqDocRef, "price", r.getPrice());
                            transaction.update(reqDocRef, "rideId", r.getRiderId());

                            HashMap<String, Double> start = new HashMap<>();
                            start.put("longitude",r.getStart().getLongitude());
                            start.put("latitude", r.getStart().getLatitude());
                            reqDocRef.collection("locations").document("start").set(start);
                            HashMap<String, Double> end = new HashMap<>();
                            end.put("longitude",r.getEnd().getLongitude());
                            end.put("latitude", r.getEnd().getLatitude());
                            reqDocRef.collection("locations").document("end").set(end);

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Transaction success!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Transaction failure.", e);
                                }
                            });

                    break;

//                case 3:
//                    final DocumentReference ordDocRef = orders.document(record.getId().toString());
//
//                    db.runTransaction(new Transaction.Function<Void>() {
//                        @Override
//                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                            DocumentSnapshot snapshot = transaction.get(ordDocRef);
//                            transaction.update(ordDocRef, "driverId", record.getDriverId());
//
//                            HashMap<String, Object> qr = new HashMap<>();
//                            //qr.put(key,value);
//                            ordDocRef.collection("other").document("qr").set(qr);
//
//                            HashMap<String, Object> orderRating = new HashMap<>();
//                            orderRating.put("thumbsUp", record.getRating().getThumbsUp());
//                            orderRating.put("thumbsDown", record.getRating().getThumbsDown());
//                            ordDocRef.collection("other").document("rating").set(orderRating);
//
//                            // Success
//                            return null;
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "Transaction success!");
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Transaction failure.", e);
//                                }
//                            });
//
//                    break;

                case 3:
                    throw new IllegalArgumentException("Order cannot be modified");

            }

        }


    }

    /**
     * query record by its id and type
     * @param id
     * @param type
     * @throws IllegalArgumentException
     * @return record
     */

    public Record queryById(Integer id, Integer type){
        try {

            if (type == 1) {
                DocumentSnapshot docPro = profiles.document(id.toString()).get().getResult();

                String phone = (String) docPro.getData().get("phone");
                String email = (String) docPro.getData().get("email");
                String username = (String) docPro.getData().get("username");
                Double balance = (Double) docPro.getData().get("balance");
                String role = (String) docPro.getData().get("role");

                try{
                    DocumentSnapshot docRating = profiles.document(id.toString()).collection("ratings").document("rating").get().getResult();
                    Integer thumbsUp = (Integer) docRating.getData().get("thumbsUp");
                    Integer thumbsDown = (Integer) docRating.getData().get("thumbsDown");
                    Rating rating = new Rating(thumbsUp, thumbsDown);
                    Profile profile = new Profile(phone, email, username, balance, role, rating);
                    profile.setId(id);
                    profile.setType(type);
                    return profile;

                }catch (Exception e){
                    Profile profile = new Profile(phone, email, username, balance, role, null);
                    profile.setId(id);
                    profile.setType(type);
                    return profile;
                }

            } else {

                if (type == 2) {
                    DocumentSnapshot docReq = requests.document(id.toString()).get().getResult();
                    DocumentSnapshot docStart = requests.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = requests.document(id.toString()).collection("locations").document("end").get().getResult();

                    Integer price = (Integer) docReq.getData().get("price");
                    Integer riderId = (Integer) docReq.getData().get("riderId");
                    Location start = new Location("");
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));
                    Location end = new Location("");
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));

                    Request request = new Request(start, end, price, riderId);

                    request.setId(id);
                    request.setType(type);

                    return request;
                } else {

                    DocumentSnapshot docOrd = orders.document(id.toString()).get().getResult();
                    DocumentSnapshot docStart = orders.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = orders.document(id.toString()).collection("locations").document("end").get().getResult();

                    Location start = new Location("");
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));

                    Location end = new Location("");
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));

                    Order order = new Order();
                    order.setStatus((Integer) docOrd.getData().get("status"));
                    order.setPrice((Integer) docOrd.getData().get("price"));
                    order.setRiderId((Integer) docOrd.getData().get("riderId"));
                    order.setStart(start);
                    order.setEnd(end);
                    order.setId(id);
                    order.setType(type);
                    try{
                        Integer driveId = (Integer) docOrd.getData().get("driverId");
                        order.setDriverId(driveId);
                    }catch (Exception e){}
                    try{
                        order.setRating(docOrd.getBoolean("ratingOfThisOrder"));
                    }catch (Exception e){}

                    return order;
                }
            }
        }catch (Exception e){return null;}

    }

    /**
     * generate a unique id for record
     * @return id
     */

    public Integer generateUniqueId(){
        Integer id = 1;

        try{
            Query queryPro = profiles.orderBy("id").limit(1);
            if((queryPro.get().getResult() != null) && (!(queryPro.get().getResult().isEmpty()))){
                Integer id1  = Integer.parseInt(queryPro.get().getResult().getDocuments().get(0).getId()) + 1;
                if(id1 > id){id = id1;}
            }
        }catch (Exception e){}

        try{
            Query queryReq = requests.orderBy("id").limit(1);
            if((queryReq.get().getResult() != null) && (!(queryReq.get().getResult().isEmpty()))){
                Integer id2 = Integer.parseInt(queryReq.get().getResult().getDocuments().get(0).getId()) + 1;
                if(id2 > id){id = id2;}
            }
        }catch (Exception e){}

        try{
            Query queryOrd = orders.orderBy("id").limit(1);
            if((queryOrd.get().getResult() != null) && (!(queryOrd.get().getResult().isEmpty()))){
                Integer id3 = Integer.parseInt(queryOrd.get().getResult().getDocuments().get(0).getId()) + 1;
                if(id3 > id){id = id3;}
            }
        }catch (Exception e){}

        return id;
    }

    /**
     * get all request
     * @return Hashmap: key is request id, value is an ArrayList of Double: {startLat, startLon, endLat, endLon}
     */

    public HashMap<Integer, ArrayList<Double>> getAllRequestLocation(){
        HashMap<Integer, ArrayList<Double>> allLocations = new HashMap<>();

        if(requests.get().getResult() == null){
            return null;

        }else{
            for (DocumentSnapshot ds: requests.get().getResult().getDocuments()){
                ArrayList<Double> location = new ArrayList<>();
                String id = ds.getId();

                DocumentSnapshot docStart = requests.document(id).collection("locations").document("start").get().getResult();
                DocumentSnapshot docEnd = requests.document(id).collection("locations").document("end").get().getResult();
                Double startLat = docStart.getDouble("latitude");
                Double startLon = docStart.getDouble("longitude");
                Double endLat = docEnd.getDouble("latitude");
                Double endLon = docEnd.getDouble("longitude");
                location.add(startLat);
                location.add(startLon);
                location.add(endLat);
                location.add(endLon);
                allLocations.put(Integer.parseInt(id), location);

            }

            return allLocations;

        }


    }

    /**
     * get request id by given rider id
     * @param id
     * @return requestId
     */

    public Integer getRequestIdByRiderId(Integer id){
        Query query = requests.whereEqualTo("riderId", id);
        if(query.get().getResult() == null){
            return null;
        }
        Integer requestId = Integer.parseInt(query.get().getResult().getDocuments().get(0).getId());

        return requestId;

    }








}

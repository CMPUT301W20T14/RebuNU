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
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;

/**
 * This is a class that create a database for this app
 * @author Zihao Huang
 * @see Record
 */

public class Database {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
     * Adds a record to the database
     * @param record
     * @throws IllegalArgumentException
     */

    public void add(Record record){
        if(this.contain(record)){
            throw new IllegalArgumentException();
        }
        switch(record.getType()) {
            case 1:

                //create a document for this profile
                HashMap<String, Object> profile = new HashMap<>();
                profile.put("id", record.getId());
                profile.put("phone", record.getPhone());
                profile.put("email", record.getEmail());
                profile.put("username", record.getUsername());
                profile.put("balance", record.getBalance());
                profile.put("role", record.getRole());
                profiles
                    .document(record.getId().toString())
                    .set(profile);
                //create subDocument for rating under this profile
                DocumentReference profileSubDoc = profiles.document(record.getId().toString()).collection("ratings").document("rating");
                HashMap<String, Integer> rating = new HashMap<>();
                rating.put("thumbsUp", record.getRating().getThumbsUp());
                rating.put("thumbsDown", record.getRating().getThumbsDown());
                profileSubDoc.set(rating);

                break;
            case 2:
                //create a document for this request
                HashMap<String, Object> request = new HashMap<>();
                request.put("id", record.getId());
                request.put("status", record.getStatus());
                request.put("price", record.getPrice());
                request.put("rideId", record.getRideId());
                requests
                        .document(record.getId().toString())
                        .set(request);
                //create subDocument for locations under this request
                DocumentReference requestStartLocation = requests.document(record.getId().toString()).collection("locations").document("start");
                HashMap<String, Double> start = new HashMap<>();
                start.put("name", record.getStart().getProvider());
                start.put("longitude",record.getStart().getLongitude());
                start.put("latitude", record.getStart().getLatitude());
                requestStartLocation.set(start);

                DocumentReference requestEndLocation = requests.document(record.getId().toString()).collection("locations").document("end");
                HashMap<String, Double> end = new HashMap<>();
                end.put("name", record.getStart().getProvider());
                end.put("longitude",record.getEnd().getLongitude());
                end.put("latitude", record.getEnd().getLatitude());
                requestEndLocation.set(end);

                break;

            case 3:
                //create a document for this order
                HashMap<String, Object> order = new HashMap<>();
                order.put("id", record.getId());
                order.put("status", record.getStatus());
                order.put("price", record.getPrice());
                order.put("rideId", record.getRideId());
                order.put("driverId", record.getDriverId());
                orders
                        .document(record.getId().toString())
                        .set(order);
                //create subDocument for locations under this order
                DocumentReference orderStartLocation = orders.document(record.getId().toString()).collection("locations").document("start");
                HashMap<String, Double> orderStart = new HashMap<>();
                orderStart.put("name", record.getStart().getProvider());
                orderStart.put("longitude",record.getStart().getLongitude());
                orderStart.put("latitude", record.getStart().getLatitude());
                orderStartLocation.set(orderStart);

                DocumentReference orderEndLocation = db.collection("Records").document(record.getId().toString()).collection("locations").document("end");
                HashMap<String, Double> orderEnd = new HashMap<>();
                orderEnd.put("name", record.getStart().getProvider());
                orderEnd.put("longitude",record.getEnd().getLongitude());
                orderEnd.put("latitude", record.getEnd().getLatitude());
                orderEndLocation.set(orderEnd);


//                //create subDocument for qr under this order
//                DocumentReference orderSubDocQr = orders.document(record.getId().toString()).collection("other").document("qr");
//                HashMap<String, Object> qr = new HashMap<>();
//                    //qr.put(key,value);
//                orderSubDocQr.set(qr);

                //create subDocument for rating under this order
                DocumentReference orderSubDocRating = db.collection("Records").document(record.getId().toString()).collection("other").document("rating");
                HashMap<String, Object> orderRating = new HashMap<>();
                orderRating.put("thumbsUp", record.getRating().getThumbsUp());
                orderRating.put("thumbsDown", record.getRating().getThumbsDown());
                orderSubDocRating.set(orderRating);
//                orders
//                        .document(orderSubDocRating.getPath())
//                        .set(orderRating);

                break;


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
        }

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

    /**
     * modefy a record in the database
     * @param record
     * @throws IllegalArgumentException
     */

    public void modify(final Record record){
        if(!(this.contain(record))){
            throw new IllegalArgumentException();
        }
        switch(record.getType()) {
            case 1:
                final DocumentReference proDocRef = profiles.document(record.getId().toString());

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(proDocRef);

                        transaction.update(proDocRef, "phone", record.getPhone());
                        transaction.update(proDocRef, "email", record.getEmail());
                        transaction.update(proDocRef, "username", record.getUsername());
                        transaction.update(proDocRef, "balance", record.getBalance());
                        transaction.update(proDocRef, "role", record.getRole());

                        HashMap<String, Integer> rating = new HashMap<>();
                        rating.put("thumbsUp", record.getRating().getThumbsUp());
                        rating.put("thumbsDown", record.getRating().getThumbsDown());
                        proDocRef.collection("ratings").document("rating").set(rating);

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

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(reqDocRef);
                        transaction.update(reqDocRef, "status", record.getStatus());
                        transaction.update(reqDocRef, "price", record.getPrice());
                        transaction.update(reqDocRef, "rideId", record.getRideId());

                        HashMap<String, Double> start = new HashMap<>();
                        start.put("name",record.getStart().getProvider());
                        start.put("longitude",record.getStart().getLongitude());
                        start.put("latitude", record.getStart().getLatitude());
                        reqDocRef.collection("locations").document("start").set(start);
                        HashMap<String, Double> end = new HashMap<>();
                        end.put("name",record.getEnd().getProvider());
                        end.put("longitude",record.getEnd().getLongitude());
                        end.put("latitude", record.getEnd().getLatitude());
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

            case 3:
                final DocumentReference ordDocRef = orders.document(record.getId().toString());

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(ordDocRef);
                        transaction.update(ordDocRef, "driverId", record.getDriverId());

                        HashMap<String, Object> qr = new HashMap<>();
                        //qr.put(key,value);
                        ordDocRef.collection("other").document("qr").set(qr);

                        HashMap<String, Object> orderRating = new HashMap<>();
                        orderRating.put("thumbsUp", record.getRating().getThumbsUp());
                        orderRating.put("thumbsDown", record.getRating().getThumbsDown());
                        ordDocRef.collection("other").document("rating").set(orderRating);

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
        }

    }

    /**
     * query record by its id and type
     * @param id
     * @param type
     * @throws IllegalArgumentException
     */

    public Record queryById(Integer id, Integer type){
        try {

            if (type == 1) {
                DocumentSnapshot docPro = profiles.document(id.toString()).get().getResult();
                DocumentSnapshot docRating = profiles.document(id.toString()).collection("ratings").document("rating").get().getResult();

                String phone = (String) docPro.getData().get("phone");
                String email = (String) docPro.getData().get("email");
                String username = (String) docPro.getData().get("username");
                Double balance = (Double) docPro.getData().get("balance");
                String role = (String) docPro.getData().get("role");
                Integer thumbsUp = (Integer) docRating.getData().get("thumbsUp");
                Integer thumbsDown = (Integer) docRating.getData().get("thumbsDown");
                Rating rating = new Rating(thumbsUp, thumbsDown);
                Profile profile = new Profile(phone, email, username, balance, role, rating);
                profile.setId(id);
                profile.setType(type);

                return profile;
            } else {

                if (type == 2) {
                    DocumentSnapshot docReq = requests.document(id.toString()).get().getResult();
                    DocumentSnapshot docStart = requests.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = requests.document(id.toString()).collection("locations").document("end").get().getResult();

                    Integer status = (Integer) docReq.getData().get("status");
                    Integer price = (Integer) docReq.getData().get("price");
                    Integer riderId = (Integer) docReq.getData().get("riderId");
                    String startProvider = (String) docStart.getData().get("name");
                    Location start = new Location(startProvider);
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));
                    String endProvider = (String) docEnd.getData().get("name");
                    Location end = new Location(endProvider);
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));

                    Request request = new Request(status, start, end, price, riderId);

                    request.setId(id);
                    request.setType(type);

                    return request;
                } else {

                    DocumentSnapshot docOrd = orders.document(id.toString()).get().getResult();
                    DocumentSnapshot docQr = orders.document(id.toString()).collection("other").document("qr").get().getResult();
                    DocumentSnapshot docRating1 = orders.document(id.toString()).collection("other").document("rating").get().getResult();
                    Integer driveId = (Integer) docOrd.getData().get("driverId");


                    Integer thumbsUp = (Integer) docRating1.getData().get("thumbsUp");
                    Integer thumbsDown = (Integer) docRating1.getData().get("thumbsDown");
                    Rating rating1 = new Rating(thumbsUp, thumbsDown);


                    DocumentSnapshot docStart = orders.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = orders.document(id.toString()).collection("locations").document("end").get().getResult();

                    String startProvider = (String)docStart.getData().get("name");
                    Location start = new Location(startProvider);
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));

                    String endProvider = (String)docEnd.getData().get("name");
                    Location end = new Location(endProvider);
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));

                    Order order = new Order(NULL, rating1, driveId);
                    order.setStatus((Integer) docOrd.getData().get("status"));
                    order.setPrice((Integer) docOrd.getData().get("price"));
                    order.setRiderId((Integer) docOrd.getData().get("riderId"));
                    order.setStart(start);
                    order.setEnd(end);
                    order.setId(id);
                    order.setType(type);

                    return order;
                }
            }
        }catch (Exception e){throw new IllegalArgumentException();}

    }

    /**
     * generate a unique id for recoed
     * @return id
     */

    public Integer generateUniqueId(){
        Integer id = 1;
        Query queryPro = profiles.orderBy("id").limit(1);
        Query queryReq = requests.orderBy("id").limit(1);
        Query queryOrd = orders.orderBy("id").limit(1);

        if(!(queryPro.get().getResult().isEmpty())){
            Integer id1  = Integer.parseInt(queryPro.get().getResult().getDocuments().get(0).getId()) + 1;
            if(id1 > id){id = id1;}
        }
        if(!(queryReq.get().getResult().isEmpty())){
            Integer id2 = Integer.parseInt(queryReq.get().getResult().getDocuments().get(0).getId()) + 1;
            if(id2 > id){id = id2;}
        }
        if(!(queryOrd.get().getResult().isEmpty())){
            Integer id3 = Integer.parseInt(queryOrd.get().getResult().getDocuments().get(0).getId()) + 1;
            if(id3 > id){id = id3;}
        }

        return id;
    }




}

package com.example.rebunu;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
                profile.put("phone", record.getPhone);
                profile.put("email", record.getEmail);
                profile.put("username", record.getUsername);
                profile.put("balance", record.getBalance);
                profile.put("role", record.getRole);
                profiles
                    .document(record.getId().toString())
                    .set(profile);
                //create subDocument for rating under this profile
                DocumentReference profileSubDoc = profiles.document(record.getId().toString()).collection("ratings").document("rating");
                HashMap<String, Integer> rating = new HashMap<>();
                rating.put("thumbsUp", record.getRating().getThumbsUp());
                rating.put("thumbsDown", record.getRating().getThumbsDown());
                profileSubDoc.set(rating);
//                profiles
//                    .document(profileSubDoc.getPath())
//                    .set(rating);

                break;
            case 2:
                //create a document for this request
                HashMap<String, Object> request = new HashMap<>();
                request.put("status", record.getStatus);
                request.put("price", record.getPrice);
                request.put("rideId", record.getRideId);
                requests
                        .document(record.getId().toString())
                        .set(request);
                //create subDocument for locations under this request
                DocumentReference requestStartLocation = requests.document(record.getId().toString()).collection("locations").document("start");
                HashMap<String, Double> start = new HashMap<>();
                start.put("longitude",record.getStart().getLongitude());
                start.put("latitude", record.getStart().getLatitude());
                requestStartLocation.set(start);
//                requests
//                        .document(requestStartLocation.getPath())
//                        .set(start);
                DocumentReference requestEndLocation = requests.document(record.getId().toString()).collection("locations").document("end");
                HashMap<String, Double> end = new HashMap<>();
                end.put("longitude",record.getEnd().getLongitude());
                end.put("latitude", record.getEnd().getLatitude());
                requestEndLocation.set(end);
//                requests
//                        .document(requestEndLocation.getPath())
//                        .set(end);

                break;

            case 3:
                //create a document for this order
                HashMap<String, Object> order = new HashMap<>();
                order.put("status", record.getStatus);
                order.put("price", record.getPrice);
                order.put("rideId", record.getRideId);
                order.put("driverId", record.getDriverId());
                orders
                        .document(record.getId().toString())
                        .set(order);
                //create subDocument for locations under this order
                DocumentReference orderStartLocation = orders.document(record.getId().toString()).collection("locations").document("start");
                HashMap<String, Double> orderStart = new HashMap<>();
                orderStart.put("longitude",record.getStart().getLongitude());
                orderStart.put("latitude", record.getStart().getLatitude());
                orderStartLocation.set(orderStart);
//                orders
//                        .document(orderStartLocation.getPath())
//                        .set(orderStart);
                DocumentReference orderEndLocation = db.collection("Records").document(record.getId().toString()).collection("locations").document("end");
                HashMap<String, Double> orderEnd = new HashMap<>();
                orderEnd.put("longitude",record.getEnd().getLongitude());
                orderEnd.put("latitude", record.getEnd().getLatitude());
                orderEndLocation.set(orderEnd);
//                orders
//                        .document(orderEndLocation.getPath())
//                        .set(orderEnd);


                //create subDocument for qr under this order
                DocumentReference orderSubDocQr = orders.document(record.getId().toString()).collection("other").document("qr");
                HashMap<String, Object> qr = new HashMap<>();
                    //qr.put(key,value);
                orderSubDocQr.set(qr);
//                orders
//                        .document(orderSubDocQr.getPath())
//                        .set(qr);
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
                        start.put("longitude",record.getStart().getLongitude());
                        start.put("latitude", record.getStart().getLatitude());
                        reqDocRef.collection("locations").document("start").set(start);
                        HashMap<String, Double> end = new HashMap<>();
                        start.put("longitude",record.getEnd().getLongitude());
                        start.put("latitude", record.getEnd().getLatitude());
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
                Profile profile = new Profile();
                DocumentSnapshot docPro = profiles.document(id.toString()).get().getResult();
                DocumentSnapshot docRating = profiles.document(id.toString()).collection("ratings").document("rating").get().getResult();

                profile.setPhone((String) docPro.getData().get("phone"));
                profile.setEmail((String) docPro.getData().get("email"));
                profile.setUsername((String) docPro.getData().get("username"));
                profile.setBalance((Double) docPro.getData().get("balance"));
                profile.setRole((String) docPro.getData().get("role"));
                Integer thumbsUp = (Integer) docRating.getData().get("thumbsUp");
                Integer thumbsDown = (Integer) docRating.getData().get("thumbsDown");
                Rating rating = new Rating();
                rating.setThumbsUp(thumbsUp);
                rating.setThumbsDown(thumbsDown);
                profile.setRating(rating);
                profile.setId(id);
                profile.setType(type);

                return profile;
            } else {

                if (type == 2) {
                    Request request = new Request();
                    DocumentSnapshot docReq = requests.document(id.toString()).get().getResult();
                    DocumentSnapshot docStart = requests.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = requests.document(id.toString()).collection("locations").document("end").get().getResult();

                    request.setStatus((Integer) docReq.getData().get("status"));
                    request.setPrice((Integer) docReq.getData().get("price"));
                    request.setRiderId((Integer) docReq.getData().get("riderId"));
                    Location start = new Location();
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));
                    Location end = new Location();
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));
                    request.setStart(start);
                    request.setEnd(end);
                    request.setId(id);
                    request.setType(type);

                    return request;
                } else {
                    Order order = new Order();
                    DocumentSnapshot docOrd = orders.document(id.toString()).get().getResult();
                    DocumentSnapshot docQr = orders.document(id.toString()).collection("other").document("qr").get().getResult();
                    DocumentSnapshot docRating1 = orders.document(id.toString()).collection("other").document("rating").get().getResult();
                    order.setDriverId((Integer) docOrd.getData().get("driverId"));
                    QRCode qr = new QRCode();
                    //set QRCode attributes
                    order.setQRCode(qr);
                    Rating rating1 = new Rating();
                    rate.setThumbsUp((Integer) docRating1.getData().get("thumbsUp"));
                    rate.setThumbsDown((Integer) docRating1.getData().get("thumbsDown"));
                    order.setRating(rating1);

                    DocumentSnapshot docStart = orders.document(id.toString()).collection("locations").document("start").get().getResult();
                    DocumentSnapshot docEnd = orders.document(id.toString()).collection("locations").document("end").get().getResult();

                    order.setStatus((Integer) docOrd.getData().get("status"));
                    order.setPrice((Integer) docOrd.getData().get("price"));
                    order.setRiderId((Integer) docOrd.getData().get("riderId"));
                    Location start = new Location();
                    start.setLongitude((Double) docStart.getData().get("longitude"));
                    start.setLatitude((Double) docStart.getData().get("latitude"));
                    Location end = new Location();
                    end.setLongitude((Double) docEnd.getData().get("longitude"));
                    end.setLatitude((Double) docEnd.getData().get("latitude"));
                    order.setStart(start);
                    order.setEnd(end);
                    order.setId(id);
                    order.setType(type);

                    return order;
                }
            }
        }catch (Exception e){throw new IllegalArgumentException();}

    }


}

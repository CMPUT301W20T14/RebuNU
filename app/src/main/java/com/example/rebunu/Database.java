package com.example.rebunu;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    Boolean exist;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference profiles = db.collection("profiles");
    CollectionReference requests = db.collection("requests");
    CollectionReference orders = db.collection("orders");
    CollectionReference auth= db.collection("auth");

    String TAG = "RebuNu";

    public void addAuth(String username, String password, String profileId){
        HashMap<String, String> au = new HashMap<>();

        au.put("password",password);
        au.put("profileId", profileId);

        auth
                .document(username)
                .set(au)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "auth storing is successful");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "auth saving failed");
                    }
                });

    }

    public String addProfile(Profile p){
        HashMap<String,Object> profile = new HashMap<>();

        profile.put("balance",p.getBalance());
        profile.put("email", p.getEmail());
        profile.put("phone", p.getPhone());
        profile.put("role", p.getRole());
        profile.put("username", p.getUsername());

        if(p.getRole()){
            ArrayList<Integer> rating = new ArrayList<>();
            rating.add(p.getRating().getThumbsUp());
            rating.add(p.getRating().getThumbsDown());

            profile.put("rating",rating);
        }else{
            profile.put("rating",new ArrayList<>());
        }

        DocumentReference docRef = profiles.document();
        docRef
                .set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "profile storing is successful");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "profile saving failed");
                    }
                });
        return docRef.getId();

    }

    public String addRequest(Request r){
        HashMap<String, Object> request = new HashMap<>();

        ArrayList<GeoPoint> pos = new ArrayList<>();
        GeoPoint start = new GeoPoint(r.getStart().getLatitude(), r.getStart().getLongitude());
        GeoPoint end= new GeoPoint(r.getEnd().getLatitude(), r.getEnd().getLongitude());

        pos.add(start);
        pos.add(end);

        request.put("price",r.getPrice());
        request.put("riderId", r.getRiderId());
        request.put("pos", pos);

        DocumentReference docRef = requests.document();

        docRef
                .set(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "request storing is successful");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "request saving failed");
                    }
                });

        return docRef.getId();


    }

    public String addOrder(Order o){
        HashMap<String , Object> order = new HashMap<>();

        ArrayList<GeoPoint> pos = new ArrayList<>();
        GeoPoint start = new GeoPoint(o.getStart().getLatitude(), o.getStart().getLongitude());
        GeoPoint end= new GeoPoint(o.getEnd().getLatitude(), o.getEnd().getLongitude());

        pos.add(start);
        pos.add(end);

        order.put("price",o.getPrice());
        order.put("riderId", o.getRiderId());
        order.put("pos", pos);
        order.put("rating",o.getRating());
        order.put("status",o.getStatus());
        order.put("driverId",o.getDriverId());

        DocumentReference docRef = orders.document();

        docRef
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "order storing is successful");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "order saving failed");
                    }
                });

        return docRef.getId();

    }

//    public Boolean contains(Record record){
//
//        switch(record.getType()){
////            case 1:
////
////                DocumentReference docIdRef = profiles.document(record.getId());
////                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////                    @Override
////                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                        if (task.isSuccessful()) {
////                            exist = true;
//////                            DocumentSnapshot document = task.getResult();
//////                            if (document==null) {
//////
//////                                exist = true;
//////
//////                                Log.d(TAG, "Document exists!");
//////                            } else {
//////                                Log.d(TAG, "Document does not exist!");
//////
//////                                exist = false;
//////                            }
////                        } else {
////                            exist = false;
////                            Log.d(TAG, "Failed with: ", task.getException());
////                        }
////                    }
////                });
////
////                return exist;
//
//
//                case 2:
//
//                    DocumentReference docReqRef = requests.document(record.getId());
//                    docReqRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task == null){
//                                exist = true;
//                            }else{
//                                exist = false;
//                            }
////                            if (task.isSuccessful()) {
////                                DocumentSnapshot document = task.getResult();
////                                exist = true;
//////                                if (document == null) {
//////
//////                                    exist = true;
//////
//////                                    Log.d(TAG, "Document exists!");
//////                                } else {
//////                                    Log.d(TAG, "Document does not exist!");
//////
//////                                    exist = false;
//////                                }
////                            } else {
////                                exist = false;
////                                Log.d(TAG, "Failed with: ", task.getException());
////                            }
//                        }
//                    });
//
//                    return exist;
//
////                    try{
////                        String riderId = (String) requests.document(record.getId()).get().getResult().get("riderId");
////                        return true;
////                    }catch (Exception e){return false;}
////
////                    case 3:
////
////                        DocumentReference docOrdRef = orders.document(record.getId());
////                        docOrdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////                            @Override
////                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                                if (task.isSuccessful()) {
////                                    DocumentSnapshot document = task.getResult();
////                                    if (document==null) {
////
////                                        exist = true;
////
////                                        Log.d(TAG, "Document exists!");
////                                    } else {
////                                        Log.d(TAG, "Document does not exist!");
////
////                                        exist = false;
////                                    }
////                                } else {
////                                    Log.d(TAG, "Failed with: ", task.getException());
////                                }
////                            }
////                        });
////                        return exist;
//
//
//
////                        try{
////                            String riderId = (String) orders.document(record.getId()).get().getResult().get("riderId");
////                            return true;
////                        }catch (Exception e){return false;}
//
//
//
//        }
//        return exist;
//
//
//    }

    public String add(Record record){
        if(true){
            switch (record.getType()){
                case 1:
                    return addProfile((Profile)record);

                case 2:
                    return addRequest((Request)record);

                case 3:
                    return addOrder((Order)record);
            }
        }else{throw new IllegalArgumentException("Already have this record");}

        return null;

    }

    public void delete(Record record) {
//        try{
//
//
//            }
//        }catch (Exception e){throw new IllegalArgumentException("There is no such record!");}

        switch (record.getType()) {
            case 1:
                profiles
                        .document(record.getId())
                        .delete();

                break;

            case 2:
                requests
                        .document(record.getId())
                        .delete();

                break;

            case 3:
                orders
                        .document(record.getId())
                        .delete();
                break;


        }
    }

    public void modifyProfile(Profile p){

        final DocumentReference proDocRef = profiles.document(p.getId());

        ArrayList<Integer> rating = new ArrayList<>();

        if(p.getRole()){
            rating.add(p.getRating().getThumbsUp());
            rating.add(p.getRating().getThumbsDown());

        }

        proDocRef
                .update("balance", p.getBalance(),"email",p.getEmail(),"phone",p.getPhone(),"role",p.getRole(),"username",p.getUsername(),"rating",rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

//        db.runTransaction(new Transaction.Function<Void>() {
//            @Override
//            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                DocumentSnapshot snapshot = transaction.get(proDocRef);
//
//                transaction.update(proDocRef, "phone", p.getPhone());
//                transaction.update(proDocRef, "email", p.getEmail());
//                transaction.update(proDocRef, "username", p.getUsername());
//                transaction.update(proDocRef, "balance", p.getBalance());
//                transaction.update(proDocRef, "role", p.getRole());
//
//                if(p.getRole()){
//
//                    ArrayList<Integer> rating = new ArrayList<>();
//                    rating.add(p.getRating().getThumbsUp());
//                    rating.add(p.getRating().getThumbsDown());
//
//                    transaction.update(proDocRef, "rating", rating);
//
//                }
//
//                // Success
//                return null;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Transaction success!");
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Transaction failure.", e);
//                    }
//                });

    }
    public void modifyRequest(Request r){
        try{
            final DocumentReference reqDocRef = requests.document(r.getId());
            ArrayList<GeoPoint> pos = new ArrayList<>();
            GeoPoint start = new GeoPoint(r.getStart().getLatitude(), r.getStart().getLongitude());
            GeoPoint end= new GeoPoint(r.getEnd().getLatitude(), r.getEnd().getLongitude());

            pos.add(start);
            pos.add(end);


            reqDocRef
                    .update("price", r.getPrice(),"riderId",r.getRiderId(),"pos",pos)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

//            db.runTransaction(new Transaction.Function<Void>() {
//                @Override
//                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                    DocumentSnapshot snapshot = transaction.get(reqDocRef);
//                    transaction.update(reqDocRef, "price", r.getPrice());
////                    transaction.update(reqDocRef, "riderId", r.getRiderId());
//
////                    ArrayList<GeoPoint> pos = new ArrayList<>();
////                    GeoPoint start = new GeoPoint(r.getStart().getLatitude(), r.getStart().getLongitude());
////                    GeoPoint end= new GeoPoint(r.getEnd().getLatitude(), r.getEnd().getLongitude());
////
////                    pos.add(start);
////                    pos.add(end);
////
////                    transaction.update(reqDocRef, "pos", pos);
//
//
//                    // Success
//                    return null;
//                }
//            }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.d(TAG, "Transaction success!");
//                }
//            })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Transaction failure.", e);
//                        }
//                    });

        }catch (Exception e){
            throw new IllegalArgumentException("No such record!");
        }



    }
    public void modify(Record record){
        if(record.getType() == 3){
            throw new IllegalArgumentException("Cannot modify order");
        }
        if(record.getType() == 1){
            modifyProfile((Profile)record);
        }
        if(record.getType() == 2){
            modifyRequest((Request)record);
        }

    }






}

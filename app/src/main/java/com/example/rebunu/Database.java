package com.example.rebunu;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Database
 * @author Zihao Huang
 */

public class Database {
//    Boolean exist;
    Record myRecord = null;
    Profile p = (Profile)myRecord;
    //p.setPhone();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference profiles = db.collection("profiles");
    CollectionReference requests = db.collection("requests");
    CollectionReference orders = db.collection("orders");
    CollectionReference auth= db.collection("auth");
    GeoFirestore geoFirestore = new GeoFirestore(requests);

    String TAG = "RebuNu";

    public String register(HashMap<String,Object> map){
        if(map == null){
            return null;
        }
        HashMap<String,Object> profile = new HashMap<>();

        profile.put("email", map.get("email"));
        profile.put("phone", map.get("phone"));
        profile.put("balance", map.get("balance"));
        profile.put("role", map.get("role"));
        profile.put("name", map.get("name"));
        profile.put("rating", map.get("rating"));

        DocumentReference proRef = profiles.document();
        proRef
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

        addAuth(map.get("phone").toString(), map.get("email").toString(), map.get("password").toString(), proRef.getId(),(Boolean)map.get("role"));



        return proRef.getId();

    }

    public void addAuth(String phone, String email, String password, String profileId, Boolean role){
        HashMap<String, Object> au = new HashMap<>();

        au.put("password",password);
        au.put("profileId", profileId);
        au.put("role", role);

        auth
                .document(phone)
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
        auth
                .document(email)
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
        profile.put("name", p.getName());

        if(p.getRole()){
            ArrayList<Integer> rating = new ArrayList<>();
            rating.add(p.getRating().getThumbsUp());
            rating.add(p.getRating().getThumbsDown());

            profile.put("rating",rating);
        }else{
            profile.put("rating",new ArrayList<Integer>());
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
        geoFirestore.setLocation(docRef.getId(), start);
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
        if(record.getType() == 1){
            throw new IllegalArgumentException("Cannot delete profile");
        }
        try{
            switch (record.getType()) {
//                case 1:
//                    profiles
//                            .document(record.getId())
//                            .delete();
//
//                    break;

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


            } catch (Exception e){throw new IllegalArgumentException("There is no such record!");}

    }

    public void modifyProfile(Profile p){

        final DocumentReference proDocRef = profiles.document(p.getId());

        ArrayList<Integer> rating = new ArrayList<>();

        if(p.getRole()){
            rating.add(p.getRating().getThumbsUp());
            rating.add(p.getRating().getThumbsDown());

        }

        proDocRef
                .update("balance", p.getBalance(),"email",p.getEmail(),"phone",p.getPhone(),"role",p.getRole(),"name",p.getName(),"rating",rating)
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

    public Profile queryProfileById(String id) {
        DocumentReference docRef = profiles.document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> dataMap = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + dataMap);
                        Utility.dataMap = dataMap;
                        Utility.dataId = document.getId();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        while(!docRef.get().isComplete()){}
        if(Utility.dataMap != null) {
            String phone = (String) Utility.dataMap.get("phone");
            String email = (String) Utility.dataMap.get("email");
            Integer balance = (Integer) Utility.dataMap.get("balance");
            ArrayList<Integer> rawRating = (ArrayList<Integer>) Utility.dataMap.get("rating");
            String name = (String) Utility.dataMap.get("name");
            Boolean role = (Boolean) Utility.dataMap.get("role");
            Profile profile = null;
            String dataId = Utility.dataId;
            Utility.dataId = null;
            Utility.dataMap = null;

            if(role) {
                try {
                    profile = new Profile(phone, email, name, balance, role, new Rating(rawRating.get(0), rawRating.get(1)));
                    profile.setId(dataId);
                    return profile;
                } catch (Exception ignored) {}
            } else {
                try {
                    profile = new Profile(phone, email, name, balance, role);
                    profile.setId(dataId);
                    return profile;
                } catch (Exception ignored) {}
            }
        }
        return null;
    }

//    public Profile queryProfileById(String id){
//        Profile profile = new Profile();
//        profiles.document(id)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        try{
//                            if(Utility.dataMap != null) {
//
//                                profile.setPhone((String) documentSnapshot.get("phone"));
//                                profile.setEmail((String) documentSnapshot.get("email"));
//                                profile.setBalance((Integer) documentSnapshot.get("balance"));
//                                ArrayList<Integer> rawRating = (ArrayList<Integer>) documentSnapshot.get("rating");
//
//                                profile.setName((String) documentSnapshot.get("name"));
//                                profile.setRole((Boolean) documentSnapshot.get("role"));
//
//                                profile.setId(documentSnapshot.getId());
//                                profile.setType(1);
//
//                                if((Boolean) documentSnapshot.get("role")) {
//                                    try {
//                                        profile.setRating(new Rating(rawRating.get(0), rawRating.get(1)));
//
//                                    } catch (Exception ignored) {}
//                                } else {
//                                    try {
//                                        profile.setRating(new Rating(0, 0));
//
//                                    } catch (Exception ignored) {}
//                                }
//                            }
//                        }catch (Exception e){}
//
//
//
//
//
//                        }
//
//
//
//                });
//        return profile;
//    }




    public Request queryRequestById(String id){
        return null;
    }
    public Order queryOrderById(String id){
        return null;
    }
    public Record queryById(String id, Integer type){
        if(id == null || type == null){
            throw new IllegalArgumentException();
        }
        switch(type){
            case 1:
                return queryProfileById(id);

            case 2:
                return queryRequestById(id);

            case 3:
                return queryOrderById(id);
        }
        return null;
    }


//    public String getRequestIdByRiderId(String riderId){
//        requests
//                .whereEqualTo("riderId", riderId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }






}

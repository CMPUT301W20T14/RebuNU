package com.example.rebunu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class OrderListActivity extends AppCompatActivity {
    private String userId;
    private Boolean role;
    private ArrayList<Order> orders;
    private ListView orderListView;
    private ArrayAdapter<Order> orderListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_list);

        userId = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("userId")).toString();
        role = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getBoolean("userId"));

        orderListView = findViewById(R.id.order_list_listview);

        Database dbOrder = new Database();
        if(role){
            dbOrder.orders
                    .whereEqualTo("riderId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // you need to first transform document to order
                                    // then add it to orders(ArrayList)
                                }
                                orderListAdapter = new OrderListAdapter(OrderListActivity.this, orders);
                                orderListView.setAdapter(orderListAdapter);
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }else{
            dbOrder.orders
                    .whereEqualTo("riderId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                }
                                orderListAdapter = new OrderListAdapter(OrderListActivity.this, orders);
                                orderListView.setAdapter(orderListAdapter);
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }




    }
}

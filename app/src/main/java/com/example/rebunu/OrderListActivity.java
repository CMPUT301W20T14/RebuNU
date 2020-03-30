package com.example.rebunu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
        setContentView(R.layout.activity_order_list);

        userId = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("userId")).toString();
        role = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getBoolean("role"));

        orderListView = findViewById(R.id.order_list_listview);
        orders = new ArrayList<>();

        Database dbOrder = new Database();
        if(role){
            dbOrder.orders
                    .whereEqualTo("driverId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    orders.add(Utility.documentSnapshotToOrder(document));
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
                                    orders.add(Utility.documentSnapshotToOrder(document));
                                }
                                orderListAdapter = new OrderListAdapter(OrderListActivity.this, orders);
                                orderListView.setAdapter(orderListAdapter);
                            } else {
                                Log.d("", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) orderListView.getItemAtPosition(position);
                HashMap<String, String> stringMap = Utility.processOrderToStringHashMap(order,getApplicationContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
                String info = "From: " + stringMap.get("start") + ".\n"
                        + "To: " + stringMap.get("end") + ".\n"
                        + "Driver ID: " + stringMap.get("driverId") + ".\n"
                        + "Rider ID: " + stringMap.get("riderId") + ".\n"
                        + "Price: " + stringMap.get("price") + " QR Bucks" + ".\n"
                        + "Status: " + stringMap.get("status") + ".\n"
                        + "Rating: " + stringMap.get("rating") + '.';
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.setTitle("Information of order: " + stringMap.get("orderId"));
                builder.setMessage(info);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}

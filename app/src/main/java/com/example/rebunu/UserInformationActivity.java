package com.example.rebunu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/**
 * @author Zijian Xi, Zihao Huang
 */
public class UserInformationActivity extends AppCompatActivity {
    String userId;
    private String password = null;
    private Boolean role;
    private String phone;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        userId = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("userId")).toString();
        Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_SHORT).show();

        TextView textview_username_large;
        EditText edittext_username_small;
        EditText edittext_userEmail;
        EditText edittext_userPhone;
        TextView textview_userRole;
        TextView textview_userBalance;
        Button userInformation_button_return;
        Button userInformation_button_apply;

        textview_username_large = findViewById(R.id.userInformation_textview_username_large);
        edittext_username_small = findViewById(R.id.userInformation_edittext_username_small);
        edittext_userEmail = findViewById(R.id.userInformation_edittext_userEmail);
        edittext_userPhone = findViewById(R.id.userInformation_edittext_userPhone);
        textview_userRole = findViewById(R.id.userInformation_textview_userRole);
        textview_userBalance = findViewById(R.id.userInforamtion_textview_userBalance);

        userInformation_button_return = findViewById(R.id.userInformation_button_return);
        userInformation_button_apply = findViewById(R.id.userInformation_button_apply);

        userInformation_button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        edittext_userPhone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                old_phone = s.toString();
//            }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        // retrieve from database ...
        Database db = new Database();

        db.profiles.document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = Objects.requireNonNull(task.getResult());
                if (document.exists()) {
                    textview_username_large.setText((String) document.get("name"));
                    edittext_username_small.setText((String) document.get("name"));
                    edittext_userEmail.setText((String) document.get("email"));
                    email = (String) document.get("email");
                    edittext_userPhone.setText((String) document.get("phone"));
                    phone = (String) document.get("phone");
                    textview_userBalance.setText(((Long) document.get("balance")).toString());
                    if((boolean)document.get("role")){
                        textview_userRole.setText("DRIVER");
                    }else {
                        textview_userRole.setText("RIDER");
                    }

                    db.auth.document(phone).get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            DocumentSnapshot document2 = Objects.requireNonNull(task2.getResult());
                            if (document2.exists()) {
                                password = (String) document2.get("password");
                                role = (Boolean) document2.get("role");
                                Log.d("", " Success");
                            } else {
                                Toast.makeText(getApplicationContext(),"Not found!", Toast.LENGTH_SHORT).show();
                                Log.d("", "No such document");
                                return;
                            }
                        } else {
                            Log.d("", "get failed with ", task2.getException());
                            Toast.makeText(getApplicationContext(), "Oops, little problem occured, please try again...", Toast.LENGTH_SHORT).show();
                            return;
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
        });




        userInformation_button_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    final DocumentReference proDocRef = db.profiles.document(userId);

                    proDocRef
                            .update("email",edittext_userEmail.getText().toString(),"phone",edittext_userPhone.getText().toString(),"name",edittext_username_small.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("", "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error updating document", e);
                                }
                            });
//                    Toast.makeText(getApplicationContext(),phone,Toast.LENGTH_SHORT).show();
                    db.deleteAuth(phone,email);
                    db.addAuth(edittext_userPhone.getText().toString(),edittext_userEmail.getText().toString(), password,userId,role);

                    finish();



            }
        });

        // then update
//        textview_username_large.setText();
//        textview_username_small.setText();
//        textview_userEmail.setText();
//        textview_userPhone.setText();
//        textview_userRole.setText();
//        textview_userBalance.setText();
    }
}

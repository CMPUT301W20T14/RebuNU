package com.example.rebunu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

import java.util.Map;

/**
 * Login screen
 * @author Zijian Xi, Zihao Huang
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Database db = new Database();
        String TAG = "RebuNu";

        Button button_return;
        Button button_login;
        EditText editText_password;
        EditText editText_emailOrPhone;

        button_return = findViewById(R.id.login_button_return);
        button_login = findViewById(R.id.login_button_login);
        editText_password = findViewById(R.id.login_edittext_password);
        editText_emailOrPhone = findViewById(R.id.login_edittext_emailOrPhone);

        button_return.setOnClickListener(v -> finish());

        button_login.setOnClickListener(v -> {

            Boolean flag = true;
            if(editText_password.getText().toString().isEmpty()) {
                editText_password.setError(getResources().getString(R.string.password_empty));
                flag = false;
            }
            if(editText_emailOrPhone.getText().toString().isEmpty()) {
                editText_emailOrPhone.setError(getResources().getString(R.string.email_or_phone_empty));
                flag = false;
            }
            // make sure already input identification
            if(flag) {
                // make sure identification is valid
                db.auth.document(editText_emailOrPhone.getText().toString())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String realPassword =(String) document.getData().get("password");
                                    String profileId = (String) document.getData().get("profileId");
                                    Boolean role = (Boolean) document.getData().get("role");
                                    if(Utility.md5Hashing(editText_password.getText().toString()).equals(realPassword)){
                                        if(role) {
                                            Intent postRequestIntent = new Intent(LoginActivity.this, DriverActivity.class);
                                            postRequestIntent.putExtra("profileId", profileId);
                                            postRequestIntent.putExtra("role", role);
                                            startActivity(postRequestIntent);
                                        } else {
                                            Intent postRequestIntent = new Intent(LoginActivity.this, RiderActivity.class);
                                            postRequestIntent.putExtra("profileId", profileId);
                                            postRequestIntent.putExtra("role", role);
                                            startActivity(postRequestIntent);
                                        }
                                    }else{
                                        editText_emailOrPhone.setError(getResources().getString(R.string.password_or_email_or_phone_wrong));
                                        editText_password.setError(getResources().getString(R.string.password_or_email_or_phone_wrong));
                                        return;
                                    }
                                } else {
                                    editText_emailOrPhone.setError(getResources().getString(R.string.password_or_email_or_phone_wrong));
                                    editText_password.setError(getResources().getString(R.string.password_or_email_or_phone_wrong));
                                    return;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Oops, little problem occurred, please try again...", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
            } else return;
        });
    }
}

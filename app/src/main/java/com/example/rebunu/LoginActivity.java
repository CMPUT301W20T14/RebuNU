package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Login screen
 * @author Zijian Xi
 */
public class LoginActivity extends AppCompatActivity {

    public Boolean auth(String username, String password) {
        // authentication implementation
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_return;
        Button button_login;
        EditText editText_password;
        EditText editText_emailOrPhone;

        button_return = findViewById(R.id.login_button_return);
        button_login = findViewById(R.id.login_button_login);
        editText_password = findViewById(R.id.login_edittext_password);
        editText_emailOrPhone = findViewById(R.id.login_edittext_emailOrPhone);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    // do something for authentication
                    flag = auth(editText_emailOrPhone.getText().toString(),
                            editText_password.getText().toString());
                } else return;
                // make sure identification is valid
                if(flag) {
                    Intent postRequestIntent = new Intent(LoginActivity.this, PostRequestActivity.class);
                    startActivity(postRequestIntent);
                } else return; // actually more code needed for wrong username/password notification
            }
        });
    }
}

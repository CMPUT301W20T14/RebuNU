package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button_return;
        Button button_login;

        button_return = findViewById(R.id.login_button_return);
        button_login = findViewById(R.id.login_button_login);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something for authentication
                Intent postRequestIntent = new Intent(LoginActivity.this, PostRequestActivity.class);
                startActivity(postRequestIntent);
            }
        });
    }
}

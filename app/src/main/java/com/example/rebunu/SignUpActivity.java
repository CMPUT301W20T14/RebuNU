package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button button_return;
        Button button_finish;
        EditText editText_username;

        button_return = findViewById(R.id.signup_button_return);
        button_finish = findViewById(R.id.signup_button_finish);
        editText_username = findViewById(R.id.signup_edittext_name);

        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something for registration
                Boolean check = true;
                if (editText_username.getText().toString().isEmpty()) {
                    editText_username.setError(getResources().getString(R.string.username_empty));
                    check = false;
                }

            }

        });
    }
}
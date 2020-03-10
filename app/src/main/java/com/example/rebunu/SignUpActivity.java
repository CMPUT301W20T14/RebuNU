package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Shanye Xue
 */

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button button_return;
        Button button_finish;
        EditText editText_username;
        EditText editText_email;
        EditText editText_phone;
        EditText editText_password;
        EditText editText_confirmpassword;


        button_return = findViewById(R.id.signup_button_return);
        button_finish = findViewById(R.id.signup_button_finish);
        editText_username = findViewById(R.id.signup_edittext_name);
        editText_email = findViewById(R.id.signup_edittext_email);
        editText_phone = findViewById(R.id.signup_edittext_phone);
        editText_password = findViewById(R.id.signup_edittext_password);
        editText_confirmpassword = findViewById(R.id.signup_edittext_confirmpassword);


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
                //not so sure check of email form is correct
                if(editText_username.getText().toString().matches("[a-z][ ]")) {
                    check = true;
                } else {
                    check = false;
                }


                if (editText_email.getText().toString().isEmpty()) {
                    editText_email.setError(getResources().getString(R.string.email_empty));
                    check = false;
                }

                if(editText_email.getText().toString().matches("^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$")) {
                    check = true;
                } else {
                    check = false;
                }


                if (editText_email.getText().toString().isEmpty()){
                    editText_email.setError(getResources().getString(R.string.phone_empty));
                    check = false;
                }
                if(editText_email.getText().toString().matches(("[0-9]+"))) {
                    if(editText_email.getText().toString().length() == 10){
                        check = true;
                    }
                } else {
                    check = false;
                }

                if (editText_password.getText().toString().isEmpty()){
                    editText_password.setError(getResources().getString(R.string.password_empty));
                    check = false;
                }
                if (editText_confirmpassword.getText().toString().isEmpty()){
                    editText_confirmpassword.setError(getResources().getString(R.string.confirmpassword_empty));
                    check = false;
                }

                if(editText_password.getText().toString() == editText_confirmpassword.getText().toString()){
                    check = true;
                }else{
                    check = false;
                }

                //if check = true, add to Database




            }






        });
    }
}
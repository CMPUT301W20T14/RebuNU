package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Zijian Xi
 */
public class UserInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        TextView textview_username_large;
        TextView textview_username_small;
        TextView textview_userEmail;
        TextView textview_userPhone;
        TextView textview_userRole;
        TextView textview_userBalance;

        textview_username_large = findViewById(R.id.userInformation_textview_username_large);
        textview_username_small = findViewById(R.id.userInformation_textview_username_small);
        textview_userEmail = findViewById(R.id.userInformation_textview_userEmail);
        textview_userPhone = findViewById(R.id.userInformation_textview_userPhone);
        textview_userRole = findViewById(R.id.userInformation_textview_userRole);
        textview_userBalance = findViewById(R.id.userInforamtion_textview_userBalance);

        // retrieve from database ...

        // then update
//        textview_username_large.setText();
//        textview_username_small.setText();
//        textview_userEmail.setText();
//        textview_userPhone.setText();
//        textview_userRole.setText();
//        textview_userBalance.setText();
    }
}

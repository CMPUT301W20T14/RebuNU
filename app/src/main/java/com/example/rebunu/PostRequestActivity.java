package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

public class PostRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);

        ConstraintLayout layout;

        layout = findViewById(R.id.postRequest_layout);

        layout.setVisibility(ConstraintLayout.GONE);
    }
}

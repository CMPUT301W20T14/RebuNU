//package com.example.rebunu;
//import androidx.appcompat.app.AppCompatActivity;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.PatternMatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
//
///**
// * Scan screen
// * @author Bofeng Chen
// */

// Reference: https://www.jianshu.com/p/b85812b6f7c1 2017.05.22 01:10:58 by xiaonan
//public class QRScanActivity extends AppCompatActivity {
//
//    private TextView textView;
//    private Button button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textView = findViewById(R.id.textView);
//        //button = findViewById(R.id.button);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);
//    }
//
//    public void ScanButton(View view){
//        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.initiateScan();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//        if (intentResult != null) {
//            if (intentResult.getContents() == null){
//                textView.setText("Cancelled");
//            }
//            else {
//                textView.setText(intentResult.getContents());
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//}










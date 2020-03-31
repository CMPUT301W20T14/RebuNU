package com.example.rebunu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * Welcome screen
 * @author Zijian Xi
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_login;
        Button button_signUp;

        button_login = findViewById(R.id.start_button_login);
        button_signUp = findViewById(R.id.start_button_signUp);
        // check if we have permission, if not, ask for permission
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////
//                Database db = new Database();
//                db.transaction("3ADPOibNez5dMe6swsYm","UMR5AAMtSVJo04BEvksS",25);
                // forcing location service to update, in case of preventing first app run that no location cache exists causing null pointer exception
                try {
                    // check if we have permission, if not, ask for permission
                    if (!(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, 1);
                    }
                    LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    Objects.requireNonNull(locationManager).requestLocationUpdates(Objects.requireNonNull(locationManager.getBestProvider(new Criteria(), false)), 1000, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Utility.currentLocation = location;
                        }
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                        @Override
                        public void onProviderEnabled(String provider) {}
                        @Override
                        public void onProviderDisabled(String provider) {}
                    });
                } catch (Exception ignored) {}
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto:abc@xyz.com"));
//                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "1234567891"));
//                startActivity(intent);
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
}

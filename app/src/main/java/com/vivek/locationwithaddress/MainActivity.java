package com.vivek.locationwithaddress;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    Button buttonGetLocation;
    TextView textViewLatLong,textViewAddress;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    GPS_Service gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, new String[]{mPermission},REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonGetLocation = findViewById(R.id.buttonGetLocation);
        textViewLatLong = findViewById(R.id.textViewLatLong);
        textViewAddress= findViewById(R.id.textViewAddress);

        buttonGetLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gps = new GPS_Service(MainActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation())
                {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    textViewLatLong.setText("Your :- \nLatitude: "+ latitude + "\nLongitude: " + longitude);
                    address(latitude,longitude);
                }
                else
                    {
                    gps.showSettingsAlert();
                }

            }
        });
    }

    private void address(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();

            textViewAddress.setText("Your Address is :- \n" +address);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
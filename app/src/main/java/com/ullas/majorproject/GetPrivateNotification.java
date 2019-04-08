package com.ullas.majorproject;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GetPrivateNotification extends AppCompatActivity {
    TextView heading,message,address;
    Button getaddress,upload;
    public double latitude, longitude;
    public String Address, setAddress;
    private DatabaseReference mDatabase;

    Geocoder geocoder;
    List<android.location.Address> addresses;
    private FusedLocationProviderClient mFusedLocationClient;
    public String ComplaintID;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_private_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        geocoder = new Geocoder(this, Locale.getDefault());
        heading= findViewById(R.id.heading);
        message= findViewById(R.id.pmessage);
        address= findViewById(R.id.paddress);
        getaddress= findViewById(R.id.getpaddress);
        upload= findViewById(R.id.btnuploadp);

        heading.setText("Trash is Full !!");
        message.setText("Please make a complaint so that our agents arrive to your place and make it clean");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Address = location.toString();
                            //Toast.makeText(ComplaintActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        getaddress.setOnClickListener(new View.OnClickListener() {
            @Override//get address.
            public void onClick(View view) {

                try {
                    address.setText(getLocation());

                } catch (Exception e) {
                    Toast.makeText(GetPrivateNotification.this, "Not possible to get location", Toast.LENGTH_SHORT).show();

                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = getLocation();
                //Toast.makeText(ComplaintActivity.this, ComplaintID, Toast.LENGTH_LONG).show();
                //Toast.makeText(ComplaintActivity.this, p, Toast.LENGTH_LONG).show();

                long time=System.currentTimeMillis();
                String time1=Long.toString(time);
                ComplaintID = UUID.randomUUID().toString();
                Date d=new Date();
                String datestring=d.toString();
                String shortdate=datestring.replace("GMT+05:30", "");
                Complaint a = new Complaint("7259289800", ComplaintID, p,"0"," ","2",time1,"0",shortdate);
                mDatabase.child("Database").child("7259289800").child(ComplaintID).setValue(a);
                Toast.makeText(GetPrivateNotification.this, "Uploaded", Toast.LENGTH_LONG).show();

            }
        });


    }
    public String getLocation() {
        try {


            Toast.makeText(GetPrivateNotification.this, "In get location", Toast.LENGTH_SHORT).show();


            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            setAddress = "ADDRESS\n" + addresses.get(0).getAddressLine(0);


            //TimeUnit.SECONDS.sleep(10);
            return setAddress;
        } catch (Exception e) {
            Toast.makeText(GetPrivateNotification.this, "Caught error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return setAddress;
    }
}

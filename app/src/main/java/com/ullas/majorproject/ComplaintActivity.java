package com.ullas.majorproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ComplaintActivity extends AppCompatActivity {
    Button gallery, camera, address, upload;
    TextView textAddress;
    ImageView img;

    FirebaseStorage storage;
    StorageReference storageReference;

    LocationManager locationManager;

    private Uri filePath;
    // public static final int CAMERA_REQUEST = 10;

    private final int PICK_IMAGE_REQUEST = 71;

    private DatabaseReference mDatabase;

    public String ComplaintID;


    public double latitude, longitude;
    public String Address, setAddress;

    Geocoder geocoder;
    List<Address> addresses;

    private ProgressDialog progressdailog;


    private FusedLocationProviderClient mFusedLocationClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        geocoder = new Geocoder(this, Locale.getDefault());


        progressdailog = new ProgressDialog(this);

        progressdailog.setMessage("Retrieving Address wait...");
        progressdailog.setCancelable(false);


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


        gallery = (Button) findViewById(R.id.btnGallary);
        camera = (Button) findViewById(R.id.btncamera);
        address = (Button) findViewById(R.id.btnAddress);
        upload = (Button) findViewById(R.id.btnUpload);
        textAddress = (TextView) findViewById(R.id.textAddress);
        img = (ImageView) findViewById(R.id.imgcomp);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        ComplaintID = UUID.randomUUID().toString();


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

              /*  File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String pictureName = getPictureName();
                File imageFile = new File(pictureDirectory, pictureName);
                filePath = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
                startActivityForResult(intent,CAMERA_REQUEST);

                */
                startActivityForResult(intent, 0);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override//get address.
            public void onClick(View view) {

                Toast.makeText(ComplaintActivity.this, LoginActivity.num(), Toast.LENGTH_LONG).show();

                try {

                    progressdailog.show();

                    textAddress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textAddress.setText(getLocation());
                            progressdailog.dismiss();

                        }
                    }, 10000);


                } catch (Exception e) {
                    Toast.makeText(ComplaintActivity.this, "Not possible to get location", Toast.LENGTH_SHORT).show();

                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAddress();
                uploadImage();

                Toast.makeText(ComplaintActivity.this, "Address Uploaded", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void uploadAddress() {
        Toast.makeText(ComplaintActivity.this, LoginActivity.num(), Toast.LENGTH_LONG).show();
        String p = ((TextView) findViewById(R.id.textAddress)).getText().toString();
        Toast.makeText(ComplaintActivity.this, ComplaintID, Toast.LENGTH_LONG).show();
        Toast.makeText(ComplaintActivity.this, p, Toast.LENGTH_LONG).show();

        Complaint a = new Complaint(LoginActivity.num(), ComplaintID, p);


        mDatabase.child("Database").child(LoginActivity.num()).child(ComplaintID).setValue(a);

    }


 /*   private String getPictureName()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "com.android." + timestamp + ".jpg";

    }
  */


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, filePath + " ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, filePath + " ", Toast.LENGTH_LONG).show();

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
    }

    public String getLocation() {
        try {


            Toast.makeText(ComplaintActivity.this, "In get location", Toast.LENGTH_SHORT).show();


            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            setAddress = "ADDRESS\n" + addresses.get(0).getAddressLine(0);


            //TimeUnit.SECONDS.sleep(10);
            return setAddress;
        } catch (Exception e) {
            Toast.makeText(ComplaintActivity.this, "Caught error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return setAddress;
    }


    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference ref = storageReference.child(LoginActivity.num() + "/" + ComplaintID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ComplaintActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}

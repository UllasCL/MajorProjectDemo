package com.ullas.majorproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ullas.majorproject.DatabaseClasses.Complaint;
import com.ullas.majorproject.LoginAndRegister.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ComplaintActivity extends AppCompatActivity {
    Button gallery, camera, address, upload;
    TextView textAddress;
    ImageView img;
    Bitmap bitmap;
    int flag = 0;

    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    // public static final int CAMERA_REQUEST = 10;
    private final int PICK_IMAGE_REQUEST = 71;
    private DatabaseReference mDatabase;
    public String ComplaintID;
    public double latitude, longitude;
    public String Address, setAddress;

    Geocoder geocoder;
    List<android.location.Address> addresses;
    private ProgressDialog progressdailog;
    private FusedLocationProviderClient mFusedLocationClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        geocoder = new Geocoder(this, Locale.getDefault());
        progressdailog = new ProgressDialog(this);

        progressdailog.setMessage("Retrieving Address wait...");
        progressdailog.setCancelable(false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Address = location.toString();
                        }
                    }
                });
        gallery = findViewById(R.id.btnGallary);
        camera = findViewById(R.id.btncamera);
        address = findViewById(R.id.btnAddress);
        upload = findViewById(R.id.btnUpload);
        textAddress = findViewById(R.id.textAddress);
        img = findViewById(R.id.imgcomp);

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
                startActivityForResult(intent, 0);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override//get address.
            public void onClick(View view) {
                try {
                    progressdailog.show();
                    textAddress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textAddress.setText(getLocation());
                            progressdailog.dismiss();
                        }
                    }, 5000);
                } catch (Exception e) {
                    Toast.makeText(ComplaintActivity.this, "Not possible to get location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  uploadAddress();
            }
        });
    }

    public void uploadAddress() {
        String p = ((TextView) findViewById(R.id.textAddress)).getText().toString();

        if(p.isEmpty()){

            Toast.makeText(ComplaintActivity.this, "Please retrieve address", Toast.LENGTH_SHORT).show();

        }else {
            long time = System.currentTimeMillis();
            String time1 = Long.toString(time);

            Date d = new Date();
            String dateString = d.toString();
            String shortDate = dateString.replace("GMT+05:30", "");
            Complaint a = new Complaint(LoginActivity.num(), ComplaintID, p, "0", " ", "1", time1, "0", shortDate);
            mDatabase.child("Database").child(LoginActivity.num()).child(ComplaintID).setValue(a);

            if (flag == 1){
                uploadImage();
                Toast.makeText(ComplaintActivity.this, "Address Uploaded", Toast.LENGTH_SHORT).show();
            }
            else if(flag == 2){
                uploadImage1(bitmap);
                Toast.makeText(ComplaintActivity.this, "Address Uploaded", Toast.LENGTH_SHORT).show();
            }
                else{
                Toast.makeText(ComplaintActivity.this, "Please select or click image", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
                flag = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, filePath + " ", Toast.LENGTH_LONG).show();
        } else {
            bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
            flag = 2;
        }
    }

    public String getLocation() {
        try {
            //Toast.makeText(ComplaintActivity.this, "In get location", Toast.LENGTH_SHORT).show();
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            setAddress = "ADDRESS\n" + addresses.get(0).getAddressLine(0);
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
            StorageReference ref = storageReference.child(LoginActivity.num() + "/" + ComplaintID + "/1");
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

    private void uploadImage1(Bitmap bitmap) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final StorageReference ref = storageReference.child(LoginActivity.num() + "/" + ComplaintID + "/1");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(ComplaintActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Log.d("Final URL", "onComplete: Url: " + downUri.toString());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ComplaintActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.ullas.majorproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MyComplaintDetails extends AppCompatActivity
{
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Database");;
    private StorageReference mStorageRef;
    ImageView im,im2;
    String uid,cid;
    String id;
    TextView tid,tcid,tadd,status,resolved;
    Button del;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaint_details);
        tid=(TextView)findViewById(R.id.uid);
        tcid=(TextView)findViewById(R.id.cid);
        tadd=(TextView)findViewById(R.id.address);
        status=(TextView)findViewById(R.id.status);
        //back=(Button)findViewById(R.id.buttonback);
        Intent intent = getIntent();
        id = intent.getStringExtra("compid");
        im=(ImageView)findViewById(R.id.imageView);
        im2=(ImageView)findViewById(R.id.imageView1);
        resolved=(TextView)findViewById(R.id.resolved);

        // Toast.makeText(details.this,id, Toast.LENGTH_SHORT).show();

        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {

                    // Toast.makeText(details.this,postSnapshot.getKey(),Toast.LENGTH_LONG).show();
                    for (DataSnapshot postSnapshot1: postSnapshot.getChildren())
                    {
                        String did=postSnapshot1.getKey();
                        //  Toast.makeText(details.this,postSnapshot1.getKey(),Toast.LENGTH_LONG).show();

                        if(did.equals(id))
                        {
                            Complaint c=postSnapshot1.getValue(Complaint.class);
                            String uid=c.getUserID();
                            String ucid=c.getComplaintID();
                            String uadd=c.complaintAddress;
                            String status1=c.getResolved();
                            if(status1.equals("0")){
                                status1="Not resolved!";
                                im2.setVisibility(View.INVISIBLE);
                                resolved.setText("");

                            }
                            else if(status1.equals("1"))
                            {
                                status1="Resolved!";
                            }
                            tid.setText(uid);
                            tcid.setText(ucid);
                            tadd.setText(uadd);
                            status.setText(status1);
                           // Toast.makeText(MyComplaintDetails.this,uid, Toast.LENGTH_LONG).show();
                           // Toast.makeText(MyComplaintDetails.this,ucid, Toast.LENGTH_LONG).show();




                            StorageReference mStorageRef1 = FirebaseStorage.getInstance().getReference();
                            final long ONE_MEGABYTE = 1024 * 1024;
                            mStorageRef1.child(uid+"/"+ucid+"/1").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    im.getLayoutParams().height = 400;
                                    im.getLayoutParams().width = 600;
                                    im.requestLayout();
                                    im.setImageBitmap(bitmap);
                                }
                            });

                                // Toast.makeText(MyComplaintDetails.this,"second", Toast.LENGTH_LONG).show();

                                StorageReference mStorageRef2 = FirebaseStorage.getInstance().getReference();
                                mStorageRef2.child(uid + "/" + ucid+"/2").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        im2.getLayoutParams().height = 400;
                                        im2.getLayoutParams().width = 600;
                                        im2.requestLayout();
                                        im2.setImageBitmap(bitmap);
                                    }
                                });




                        }
                    }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);





    }
}


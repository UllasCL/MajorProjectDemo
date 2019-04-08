package com.ullas.majorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class Mycomplaints extends AppCompatActivity {
    private List<Complaint> complaintlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private MycomplaintsAdapter mAdapter;

    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Database/"+LoginActivity.num());;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomplaints);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());


        recyclerView = findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



        prepareMovieData();
    }

    private void prepareMovieData() {
        // movieList.clear();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    // Toast.makeText(getActivity(),postSnapshot.getKey(),Toast.LENGTH_LONG).show();
                    for (DataSnapshot postSnapshot1: dataSnapshot.getChildren()) {
                        Complaint c=postSnapshot1.getValue(Complaint.class);
                        // Bitmap b=getimage(c.userID,c.complaintID);
                        //  Complaint movie = new Complaint(c.userID,c.complaintID,c.complaintAddress,b);
                       // if(c.getResolved().equals("0"))
                            complaintlist.add(c);


                        //  Toast.makeText(MainActivity.this,c.getComplaintAddress(), Toast.LENGTH_LONG).show();
                        //arrayList.add("Complaint id:"+c.complaintID+"\n"+"Address:"+c.complaintAddress+"\n"+"Userid:"+c.userID);


                    }

                mAdapter = new MycomplaintsAdapter(complaintlist);
                recyclerView.setAdapter(mAdapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        mDatabase.addListenerForSingleValueEvent(postListener);


       /* Complaint c=new Complaint("12345","1234","Hulkodu");
        Complaint c2=new Complaint("12345","1234","Hulkod");

        Complaint c1=new Complaint("12345","1234","Hulkodu");
        movieList.add(c);
        movieList.add(c1);
        movieList.add(c2);
        */


        //  Toast.makeText(MainActivity.this,movieList.size(), Toast.LENGTH_LONG).show();


    }

   /* Bitmap getimage(String userid,String complaintid)
    {
        Bitmap b;
        mImageRef=FirebaseStorage.getInstance().getReference(userid+"/"+complaintid);
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                b=bm;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }*/
}
/*
 class Complaint1 {
    String userID;
    String complaintID;
    String complaintAddress;
    public Complaint1( ){};
    public String getUserID() {
        return userID;
    }

    public String getComplaintID() {
        return complaintID;
    }

    public String getComplaintAddress() {
        return complaintAddress;
    }


    public Complaint1(String UserID, String ComplaintID, String ComplaintAddress) {
        this.userID = UserID;
        this.complaintID = ComplaintID;
        this.complaintAddress = ComplaintAddress;
    }

}*/












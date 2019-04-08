package com.ullas.majorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PushMessageActivity extends AppCompatActivity {
    Button submit;
    TextView message;
    RatingBar rating;
    String ratingvalue,aid,uid,cid;
    private DatabaseReference mDatabase,mDatabase1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        submit= findViewById(R.id.buttonfeedback);
        message= findViewById(R.id.message);
        rating= findViewById(R.id.ratingBar);

        extras.getString("date");
        extras.getString("message");
        extras.getString("aid");
        extras.getString("uid");
        extras.getString("cid");



       /* StringBuilder keys = new StringBuilder();
        if (extras != null) {
            for (String key : extras.keySet())
                keys.append(key + " = " + extras.getString(key) + "\n ");
        }
        String value=extras.getString("extra");
        Toast.makeText(Second.this, value, Toast.LENGTH_LONG).show();*/
        String value=extras.getString("date");
        String value1=extras.getString("message");
        System.out.println(value1);
        aid=extras.getString("aid");
        uid=extras.getString("uid");
        cid=extras.getString("cid");
        Toast.makeText(PushMessageActivity.this, aid, Toast.LENGTH_LONG).show();

        // Toast.makeText(PushMessageActivity.this, value, Toast.LENGTH_LONG).show();
       // Toast.makeText(PushMessageActivity.this, value1, Toast.LENGTH_LONG).show();
        message.setText("\tYour complaint registered on\n"+value+" has been cleared successfully in\n"+value1+".\nPlease go to my complaints for further details.");


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                 ratingvalue=String.valueOf(rating);
                Toast.makeText(PushMessageActivity.this, ratingvalue, Toast.LENGTH_LONG).show();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Database/"+uid+"/"+cid);
                mDatabase.child("ratingByUser").setValue(ratingvalue);
                mDatabase1 = FirebaseDatabase.getInstance().getReference("Agents/"+aid+"/"+cid);
                mDatabase1.child("ratingByUser").setValue(ratingvalue);
                Toast.makeText(PushMessageActivity.this,"Ratings uploaded", Toast.LENGTH_LONG).show();

            }
        });




    }
}

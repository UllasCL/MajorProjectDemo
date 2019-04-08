package com.ullas.majorproject;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    Button goa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        goa = (Button) findViewById(R.id.Go);

        goa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


                Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());
            }

        });
    }

}


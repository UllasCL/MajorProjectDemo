package com.ullas.majorproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
               /* try {
                    FCMNotification.sendPushNotification("d7StVzAtHsw:APA91bEHnux5fiKhdhwSo52SBIV_02z45HT8Qx7DR40LU_ljA7hbWzwmwh5lr7nld74zYyThW_-pt_ifZk9uIvYbARRl3LBDeE-ATu4EglPzPJrM2Mx909EN5cr9LMAsNbtWriyaDnlV");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });
    }
}

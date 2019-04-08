package com.ullas.majorproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard extends AppCompatActivity {
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Database");
    private List<Leader> leaderlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeaderboardAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        readvalue();
    }
    void readvalue(){
        leaderlist.clear();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot1: dataSnapshot.getChildren()) {
                    long count=postSnapshot1.getChildrenCount();
                  //  Toast.makeText(Leaderboard.this,Long.toString(count),Toast.LENGTH_LONG).show();
                    Leader a=new Leader(postSnapshot1.getKey(),Long.toString(count));
                    leaderlist.add(a);

                }
                Collections.sort(leaderlist, new MyComparator());
                mAdapter = new LeaderboardAdapter(leaderlist);
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

    }
}

class MyComparator implements Comparator<Leader> {
    @Override
    public int compare(Leader leader1, Leader leader2) {
        String score1=leader1.getComplaintscount();
        int w0=Integer.parseInt(score1);
        String score2=leader2.getComplaintscount();
        int w1=Integer.parseInt(score2);


        return (w0 > w1? -1 : (w0 == w1) ? 0 : 1);
    }
}

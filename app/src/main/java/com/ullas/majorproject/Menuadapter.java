package com.ullas.majorproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class Menuadapter extends RecyclerView.Adapter<Menuadapter.MyViewHolder> {

    private Context mContext;
    private List<Menurow> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);

        }
    }


    public Menuadapter(Context mContext, List<Menurow> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Menurow album = albumList.get(position);
        final String option=album.getName();
        holder.title.setText(album.getName());
        // holder.count.setText(album.getNumOfSongs() + " songs");

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option.equals("New Complaint")) {
                    Intent i = new Intent(v.getContext(), ComplaintActivity.class);
                    v.getContext().startActivity(i);
                }
                if(option.equals("My complaints")) {
                    Intent i = new Intent(v.getContext(), Mycomplaints.class);
                    v.getContext().startActivity(i);
                }
                if(option.equals("Users leaderborad")) {
                    Intent i = new Intent(v.getContext(), Leaderboard.class);
                    v.getContext().startActivity(i);
                }

            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option.equals("Show Complaints")) {
                   // Intent i = new Intent(v.getContext(), MainActivity.class);
                  //  v.getContext().startActivity(i);
                }
                if(option.equals("Cleared complaints")) {
                    //Intent i = new Intent(v.getContext(), Clearedcomplaints.class);
                   // v.getContext().startActivity(i);
                }
                if(option.equals("Leaderboard")) {
                   // Intent i = new Intent(v.getContext(), Leaderboard.class);
                  //  v.getContext().startActivity(i);
                }

            }
        });

    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }
}





package com.ullas.majorproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ullas.majorproject.LeaderBoardActivity.Leader;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private List<Leader> leaderlist;
    String compid;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView uid, complaintscount;

        public MyViewHolder(View view) {
            super(view);
            uid = view.findViewById(R.id.uid);
            complaintscount = view.findViewById(R.id.compcount);
        }
    }

    public LeaderboardAdapter(List<Leader> leaderlist) {
        this.leaderlist = leaderlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboardrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Leader leader = leaderlist.get(position);
        holder.complaintscount.setText(leader.getComplaintscount());
        holder.uid.setText(leader.getUid());
    }

    @Override
    public int getItemCount() {
        return leaderlist.size();
    }
}


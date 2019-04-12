package com.ullas.majorproject;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ullas.majorproject.DatabaseClasses.Complaint;

import java.util.List;


public class MycomplaintsAdapter extends RecyclerView.Adapter<MycomplaintsAdapter.MyViewHolder> {

    private List<Complaint> complaintListList;
    String compid;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView compid,uid,tvdate;
        public ImageView im;
        public Button details;

        public MyViewHolder(View view) {
            super(view);
            uid = view.findViewById(R.id.uid);
            compid = view.findViewById(R.id.compid);
            //  im = (ImageView) view.findViewById(R.id.imageView);
            details= view.findViewById(R.id.det);
            tvdate= view.findViewById(R.id.tvdate);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(v.getContext(),compid.getText(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(v.getContext(),MyComplaintDetails.class);
                    intent.putExtra("compid",compid.getText().toString().substring(13));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public MycomplaintsAdapter(List<Complaint> complist) {
        this.complaintListList = complist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycomplaintsrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Complaint complaint = complaintListList.get(position);
        holder.compid.setText("ComplaintID: "+complaint.getComplaintID());
        holder.uid.setText("UserID: "+complaint.getUserID());
        holder.tvdate.setText("Date: "+complaint.getDate());
        //  holder.im.setImageBitmap(movie.getPicture());
    }

    @Override
    public int getItemCount() {
        return complaintListList.size();
    }
}


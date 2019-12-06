package com.app.activitymonitor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodayRecyclerAdapter extends RecyclerView.Adapter<TodayRecyclerAdapter.MyViewHolder> {

    private ArrayList<List<String>> activityList;

    DateTimeFormatter formatter;

    public TodayRecyclerAdapter(ArrayList<List<String>> activityList) {
        this.activityList = activityList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public TextView deleteButton;
        public View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView;

            mTitle = itemView.findViewById(R.id.titleHistory);
            mDate = itemView.findViewById(R.id.dateHistory);
            mTime = itemView.findViewById(R.id.timeHistory);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    //    public void add(int position, String item) {
//        list.add(position, item);
//        notifyItemInserted(position);
//    }
//
    public void remove(int position) {
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mRef.child(activityList.get(position).get(2)).removeValue();
        activityList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final List<String> details = activityList.get(position);
        holder.mTitle.setText(details.get(0));

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(details.get(1),formatter);

        Log.i("datetime", dateTime.toString());
        holder.mDate.setText(String.format("%s, %s %s %s",dateTime.getDayOfWeek(),dateTime.getDayOfMonth(),dateTime.getMonth(),dateTime.getYear()));

        String hourStr = "";
        String minStr = "";

        if (String.valueOf(dateTime.getHour()).length() == 1) {
            hourStr = String.format("0%s",dateTime.getHour());
        } else {
            hourStr = String.format("%s",dateTime.getHour());
        }
        if (String.valueOf(dateTime.getMinute()).length() == 1) {
            minStr = String.format("0%s",dateTime.getMinute());
        } else {
            minStr = String.format("%s",dateTime.getMinute());
        }

        holder.mTime.setText(String.format("%s:%s",hourStr,minStr));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }


}

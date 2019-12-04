package com.app.activitymonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder> {

    private ArrayList<List<String>> activityList;

    public HistoryRecyclerAdapter(ArrayList<List<String>> activityList) {
        this.activityList = activityList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDate;
        public TextView mTime;
        public View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView;

            mTitle = itemView.findViewById(R.id.titleHistory);
            mDate = itemView.findViewById(R.id.dateHistory);
            mTime = itemView.findViewById(R.id.timeHistory);
        }
    }

//    public void add(int position, String item) {
//        list.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final List<String> details = activityList.get(position);
        holder.mTitle.setText(details.get(0));
        holder.mDate.setText(details.get(1));
        holder.mTime.setText(details.get(2));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }


}

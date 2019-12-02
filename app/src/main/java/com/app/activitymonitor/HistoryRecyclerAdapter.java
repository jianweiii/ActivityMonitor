package com.app.activitymonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.MyViewHolder> {

    private List<String> list;

    public HistoryRecyclerAdapter(List<String> list) {
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDate;
        public View layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView;

            mTitle = itemView.findViewById(R.id.titleHistory);
            mDate = itemView.findViewById(R.id.dateHistory);
        }
    }

    public void add(int position, String item) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        list.remove(position);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String name = list.get(position);
        holder.mTitle.setText(name);
        holder.mDate.setText(name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

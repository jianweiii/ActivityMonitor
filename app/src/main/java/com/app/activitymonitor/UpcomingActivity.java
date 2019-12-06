package com.app.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class UpcomingActivity extends AppCompatActivity {

    private RecyclerView upcomingRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter upcomingAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        upcomingRecycler = findViewById(R.id.upcomingRecycler);

        layoutManager = new LinearLayoutManager(this);
        upcomingRecycler.setLayoutManager(layoutManager);

        ArrayList<List<String>> activityList = (ArrayList<List<String>>) getIntent().getSerializableExtra("upcomingActivityList");

        upcomingAdaptor = new UpcomingRecyclerAdapter(activityList);
        upcomingRecycler.setAdapter(upcomingAdaptor);
    }
}

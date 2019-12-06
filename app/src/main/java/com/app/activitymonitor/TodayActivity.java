package com.app.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TodayActivity extends AppCompatActivity {

    private RecyclerView todayRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter todayAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        todayRecycler = findViewById(R.id.todayRecycler);

        layoutManager = new LinearLayoutManager(this);
        todayRecycler.setLayoutManager(layoutManager);

        ArrayList<List<String>> activityList = (ArrayList<List<String>>) getIntent().getSerializableExtra("todayActivityList");

        todayAdaptor = new TodayRecyclerAdapter(activityList);
        todayRecycler.setAdapter(todayAdaptor);
    }
}

package com.app.activitymonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {


    private RecyclerView historyRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter historyAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecycler = findViewById(R.id.historyRecycler);

        layoutManager = new LinearLayoutManager(this);
        historyRecycler.setLayoutManager(layoutManager);

        ArrayList<List<String>> activityList = (ArrayList<List<String>>) getIntent().getSerializableExtra("activityList");
        Log.i("HA", activityList.toString());

//        List<String> input = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            input.add("Test" + i);
//        }// define an adapter
        historyAdaptor = new HistoryRecyclerAdapter(activityList);
        historyRecycler.setAdapter(historyAdaptor);
    }
}

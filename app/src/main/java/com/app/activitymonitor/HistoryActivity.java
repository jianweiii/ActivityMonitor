package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

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

        historyAdaptor = new HistoryRecyclerAdapter(activityList);
        historyRecycler.setAdapter(historyAdaptor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

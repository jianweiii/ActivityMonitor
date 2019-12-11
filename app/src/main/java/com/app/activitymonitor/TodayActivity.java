package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class TodayActivity extends AppCompatActivity {

    private RecyclerView todayRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter todayAdaptor;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        preferences = getSharedPreferences("color", MODE_PRIVATE);

        todayRecycler = findViewById(R.id.todayRecycler);

        layoutManager = new LinearLayoutManager(this);
        todayRecycler.setLayoutManager(layoutManager);

        ArrayList<List<String>> activityList = (ArrayList<List<String>>) getIntent().getSerializableExtra("todayActivityList");

        todayAdaptor = new TodayRecyclerAdapter(activityList);
        todayRecycler.setAdapter(todayAdaptor);
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

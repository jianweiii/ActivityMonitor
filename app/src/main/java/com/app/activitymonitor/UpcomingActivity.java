package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UpcomingActivity extends AppCompatActivity {

    private RecyclerView upcomingRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter upcomingAdaptor;

    SharedPreferences preferences;
    LinearLayout layoutBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        upcomingRecycler = findViewById(R.id.upcomingRecycler);
        layoutBackground = findViewById(R.id.layoutBackground);

        preferences = getSharedPreferences("color", MODE_PRIVATE);
        String bgdColor = preferences.getString("background_color", getString(R.string.black));

        if (bgdColor.equals("#000000")) {
            layoutBackground.setBackgroundResource(R.drawable.gradient);
        } else {
            layoutBackground.setBackgroundColor(Color.parseColor(bgdColor));
        }

        layoutManager = new LinearLayoutManager(this);
        upcomingRecycler.setLayoutManager(layoutManager);

        ArrayList<List<String>> activityList = (ArrayList<List<String>>) getIntent().getSerializableExtra("upcomingActivityList");

        upcomingAdaptor = new UpcomingRecyclerAdapter(activityList);
        upcomingRecycler.setAdapter(upcomingAdaptor);
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

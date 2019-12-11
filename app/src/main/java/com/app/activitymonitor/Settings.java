package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class Settings extends AppCompatActivity {

    private TextView darkMode, lightMode, modeText;
    private LinearLayout layoutBackground;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkMode = findViewById(R.id.darkMode);
        lightMode = findViewById(R.id.lightMode);
        modeText = findViewById((R.id.modeText));

        layoutBackground = findViewById(R.id.layoutBackground);

        preferences = getSharedPreferences("color", MODE_PRIVATE);

        String bgdColor = preferences.getString("background_color", getString(R.string.black));
        String txtColor = preferences.getString("text_color", getString(R.string.white));

        layoutBackground.setBackgroundColor(Color.parseColor(bgdColor));
        modeText.setTextColor(Color.parseColor(txtColor));


        darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutBackground.setBackgroundColor(Color.parseColor(getString(R.string.black)));
                modeText.setTextColor(Color.parseColor(getString(R.string.white)));
                preferences.edit().putString("background_color", getString(R.string.black)).apply();
                preferences.edit().putString("text_color", getString(R.string.white)).apply();
            }
        });

        lightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutBackground.setBackgroundColor(Color.parseColor(getString(R.string.white)));
                modeText.setTextColor(Color.parseColor(getString(R.string.black)));
                preferences.edit().putString("background_color", getString(R.string.white)).apply();
                preferences.edit().putString("text_color", getString(R.string.black)).apply();
            }
        });
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
                finish();
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

package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button logOutButton,historyViewer,addActivityButton;
    private TextView displayUsernameMain;
    private LinearLayout historyActivity;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;

    ArrayList<List<String>> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutButton = findViewById(R.id.mainLogoutButtonId);
        historyViewer = findViewById(R.id.historyViewer);
        displayUsernameMain = findViewById(R.id.displayUsernameMain);
        addActivityButton = findViewById(R.id.addActivityButton);
        historyActivity = findViewById(R.id.historyActivity);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    displayUsernameMain.setText(String.format("Welcome %s", user.getDisplayName()));
                }
            }
        };

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        historyViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("activityList", activityList);
                startActivity(intent);

            }
        });

        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("activityList", activityList);
                startActivity(intent);
            }
        });

        addActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("MAIN", "On start called");


        firebaseAuth.addAuthStateListener(authStateListener);

        final FirebaseUser user =  firebaseAuth.getCurrentUser();
        final String userId = user.getUid();
        final DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activityList.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    // Get the title of each activity
                    String titleActivity = postSnapshot.child("Activity").child("title").getValue().toString();
                    // Get the date of each activity
                    String dateActivity = postSnapshot.child("Activity").child("date").getValue().toString();
                    // Get the time of each activity
                    String timeActivity = postSnapshot.child("Activity").child("time").getValue().toString();

                    // Add activity to a list before adding it to array of list
                    List<String> indvActivity = new ArrayList<>();
                    indvActivity.add(titleActivity);
                    indvActivity.add(dateActivity);
                    indvActivity.add(timeActivity);
                    activityList.add(indvActivity);

                }
                System.out.println(activityList);
//                Toast.makeText(getApplicationContext(), "History Loaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}

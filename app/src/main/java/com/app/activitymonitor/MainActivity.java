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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button logOutButton,historyViewer,addActivityButton;
    private TextView displayUsernameMain;
    private LinearLayout historyActivity,todayActivity,upcomingActivity;
    private TextView todayActivityLeft, upcomingActivityLeft, historyActivityLeft;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;

    ArrayList<List<String>> todayActivityList = new ArrayList<>();
    ArrayList<List<String>> upcomingActivityList = new ArrayList<>();
    ArrayList<List<String>> historyActivityList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutButton = findViewById(R.id.mainLogoutButtonId);
        historyViewer = findViewById(R.id.historyViewer);
        displayUsernameMain = findViewById(R.id.displayUsernameMain);
        addActivityButton = findViewById(R.id.addActivityButton);
        historyActivity = findViewById(R.id.historyActivity);
        todayActivity = findViewById(R.id.todayActivity);
        upcomingActivity = findViewById(R.id.upcomingActivity);

        todayActivityLeft = findViewById(R.id.todayActivityLeft);
        upcomingActivityLeft = findViewById(R.id.upcomingActivityLeft);
        historyActivityLeft = findViewById(R.id.historyActivityLeft);

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

        upcomingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("upcomingActivityList", upcomingActivityList);
                startActivity(intent);

            }
        });

        todayActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("todayActivityList", todayActivityList);
                startActivity(intent);
            }
        });

        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                intent.putExtra("activityList", historyActivityList);
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

        final LocalDate currentDate = LocalDate.now();
        DateTimeFormatter currentDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Log.i("CURRENTDATE", currentDate.toString());

        final String todayDate = currentDate.format(currentDateFormatter);

        System.out.println(currentDate.format(currentDateFormatter));



        firebaseAuth.addAuthStateListener(authStateListener);

        final FirebaseUser user =  firebaseAuth.getCurrentUser();
        final String userId = user.getUid();
        final DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users").child(userId);



        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear list when activity starts to prevent duplicates
                todayActivityList.clear();
                upcomingActivityList.clear();
                historyActivityList.clear();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    // Get key name of each activity
                    String keyActivity = postSnapshot.getKey();
                    // Get the title of each activity
                    String titleActivity = postSnapshot.child("Activity").child("title").getValue().toString();
                    // Get the date of each activity
                    String dateActivity = postSnapshot.child("Activity").child("date").getValue().toString();
                    // Get the time of each activity
                    String timeActivity = postSnapshot.child("Activity").child("time").getValue().toString();

                    // Saving dateTime format as "dd/MM/yyyy HH:mm"
                    String dateTime = String.format("%s %s",dateActivity, timeActivity);

                    // Add activity to a list before adding it to array of list
                    List<String> todayDateActivity = new ArrayList<>();
                    List<String> upcomingDateActivity = new ArrayList<>();
                    List<String> historyDateActivity = new ArrayList<>();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dt = LocalDate.parse(dateActivity,formatter);

                    System.out.println(dt);



                    int comparator = currentDate.compareTo(dt);

                    if (comparator > 0) {
                        historyDateActivity.add(titleActivity);
                        historyDateActivity.add(dateTime);
                        historyDateActivity.add(keyActivity);
                        historyActivityList.add(historyDateActivity);
                    } else if (comparator < 0) {
                        upcomingDateActivity.add(titleActivity);
                        upcomingDateActivity.add(dateTime);
                        upcomingDateActivity.add(keyActivity);
                        upcomingActivityList.add(upcomingDateActivity);
                    } else if (comparator == 0) {
                        todayDateActivity.add(timeActivity);
                        todayDateActivity.add(dateTime);
                        todayDateActivity.add(keyActivity);
                        todayActivityList.add(todayDateActivity);
                    }

                }

                // Sort by datetime string
                Collections.sort(historyActivityList, new Comparator<List<String>>() {
                            @Override
                            public int compare(List<String> o1, List<String> o2) {
                                return (o1.get(1)).compareTo((o2.get(1)));
                            }
                        });

                // Reverse the datetime list so that the most recent event shows first
                Collections.reverse(historyActivityList);


                todayActivityLeft.setText(String.valueOf(todayActivityList.size()));
                historyActivityLeft.setText(String.valueOf(historyActivityList.size()));
                upcomingActivityLeft.setText(String.valueOf(upcomingActivityList.size()));


                Log.i("todayActivity", todayActivityList.toString());
                Log.i("historyActivity", historyActivityList.toString());
                Log.i("upcomingActivity", upcomingActivityList.toString());

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

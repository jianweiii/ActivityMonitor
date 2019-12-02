package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private Button cancelAddActivityButton;
    private Button addAddActivityButton;

    private EditText titleAddActivityEdit;
    private EditText dateAddActivityEdit;

    private DatePickerDialog picker;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        cancelAddActivityButton = findViewById(R.id.cancelAddActivityButton);
        addAddActivityButton = findViewById(R.id.addAddActivityButton);

        titleAddActivityEdit = findViewById(R.id.titleAddActivityEdit);
        dateAddActivityEdit = findViewById(R.id.dateAddActivityEdit);

        final HashMap<String, HashMap<String, String>> hashMap = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();

        dateAddActivityEdit.setInputType(InputType.TYPE_NULL);

        dateAddActivityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateAddActivityEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        cancelAddActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addAddActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Adding current activity details to hashMap
                hashMap.put("Activity", new HashMap<String, String>());
                hashMap.get("Activity").put("title", titleAddActivityEdit.getText().toString());
                hashMap.get("Activity").put("date", dateAddActivityEdit.getText().toString());

                final FirebaseUser user =  firebaseAuth.getCurrentUser();
                final String userId = user.getUid();
                final DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users");
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(userId)) {
                            System.out.println("SCREAMMMMM");
                            mRef.child(userId).push().setValue(hashMap);
//                            mRef.child(userId).child("title").setValue(titleAddActivityEdit.getText().toString());
                        } else {
                            System.out.println("CRYYYYYYY");
                            mRef.child(userId).child("date").setValue(dateAddActivityEdit.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                DatabaseReference mRef =  database.getReference().child("Users").child(userId);
//                System.out.println(mRef);
//                mRef.child("age").setValue(1);
//                mRef.child("gender").setValue(finalGender);
//                mRef.child("username").setValue(username);

                finish();
            }
        });

    }
}

package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private Button cancelAddActivityButton;
    private Button addAddActivityButton;

    private EditText titleAddActivityEdit;
    private EditText dateAddActivityEdit;
    private EditText timeAddActivityEdit;

    private Switch switchNotification;

    private DatePickerDialog picker;
    private TimePickerDialog timePickerDialog;

    FirebaseAuth firebaseAuth;

    private String notificationFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        cancelAddActivityButton = findViewById(R.id.cancelAddActivityButton);
        addAddActivityButton = findViewById(R.id.addAddActivityButton);

        titleAddActivityEdit = findViewById(R.id.titleAddActivityEdit);
        dateAddActivityEdit = findViewById(R.id.dateAddActivityEdit);
        timeAddActivityEdit = findViewById(R.id.timeAddActivityEdit);

        switchNotification = findViewById(R.id.switchNotification);

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
                                if (String.valueOf(dayOfMonth).length() == 1 ) {
                                    String dayOfMonthStr = String.format("0%d",dayOfMonth);
                                    if (String.valueOf(monthOfYear+1).length() == 1) {
                                        String monthOfYearStr = String.format("0%d",(monthOfYear+1));
                                        dateAddActivityEdit.setText(dayOfMonthStr + "/" + monthOfYearStr + "/" + year);
                                    } else {
                                        dateAddActivityEdit.setText(dayOfMonthStr + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                } else {
                                    if (String.valueOf(monthOfYear+1).length() == 1) {
                                        String monthOfYearStr = String.format("0%d",(monthOfYear+1));
                                        dateAddActivityEdit.setText(dayOfMonth + "/" + monthOfYearStr + "/" + year);
                                    } else {
                                        dateAddActivityEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        timeAddActivityEdit.setInputType(InputType.TYPE_NULL);

        timeAddActivityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(AddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (String.valueOf(hourOfDay).length() == 1) {
                                    String hourOfDayStr = String.format("0%d",hourOfDay);
                                    if (String.valueOf(minute).length() == 1) {
                                        String minuteStr = String.format("0%d",minute);
                                        timeAddActivityEdit.setText(hourOfDayStr + ":" + minuteStr);
                                    } else {
                                        timeAddActivityEdit.setText(hourOfDayStr + ":" + minute);
                                    }
                                } else {
                                    if (String.valueOf(minute).length() == 1) {
                                        String minuteStr = String.format("0%d", minute);
                                        timeAddActivityEdit.setText(hourOfDay + ":" + minuteStr);
                                    } else {
                                        timeAddActivityEdit.setText(hourOfDay + ":" + minute);
                                    }
                                }

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        cancelAddActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationFlag = "TRUE";
                    String dateTime = String.format("%s %s",dateAddActivityEdit.getText(),timeAddActivityEdit.getText());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime ldt = LocalDateTime.parse(dateTime,formatter);
                    ZonedDateTime zdt = ldt.atZone(ZoneId.of("Singapore"));
                    long ms = zdt.toInstant().toEpochMilli();
                    System.out.println(ms);

                    System.out.println(System.currentTimeMillis());
                    long delay = (ms - System.currentTimeMillis());
                    System.out.println(delay);
                } else {
                    notificationFlag = "FALSE";
                }
            }
        });

        addAddActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Adding current activity details to hashMap
                hashMap.put("Activity", new HashMap<String, String>());
                hashMap.get("Activity").put("title", titleAddActivityEdit.getText().toString());
                hashMap.get("Activity").put("date", dateAddActivityEdit.getText().toString());
                hashMap.get("Activity").put("time", timeAddActivityEdit.getText().toString());

                final FirebaseUser user =  firebaseAuth.getCurrentUser();
                final String userId = user.getUid();
                final DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users");
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mRef.child(userId).push().setValue(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                String dateTime = String.format("%s %s",dateAddActivityEdit.getText(),timeAddActivityEdit.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime ldt = LocalDateTime.parse(dateTime,formatter);
                ZonedDateTime zdt = ldt.atZone(ZoneId.of("Singapore"));
                long ms = zdt.toInstant().toEpochMilli();
                System.out.println(ms);

                System.out.println(System.currentTimeMillis());
                long delay = (ms - System.currentTimeMillis());

                if (notificationFlag == "TRUE" && delay > 0) {
                    scheduleNotification(getNotification(titleAddActivityEdit.getText().toString()), delay );
                }



                finish();
            }
        });

    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, ReminderReceiver.class);
        notificationIntent.putExtra(ReminderReceiver.NOTIFICATION_ID , 1 );
        notificationIntent.putExtra(ReminderReceiver.NOTIFICATION , notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT );
        long futureInMillis = SystemClock.elapsedRealtime() + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE );
        assert alarmManager != null;
        //TODO: check if i can just use ms to pass in as parameter
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent);
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id );
        builder.setContentTitle("Scheduled Activity Reminder");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
}

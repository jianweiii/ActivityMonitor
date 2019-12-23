package com.app.activitymonitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private Button cancelAddActivityButton;
    private Button addAddActivityButton;

    private EditText titleAddActivityEdit;
    private EditText dateAddActivityEdit;
    private EditText timeAddActivityEdit;
    private EditText numberOfReps;

    private Switch switchNotification;
    private RadioGroup repeatRadioGrp;
    private RadioButton repeatDaily, repeatWeekly, repeatMonthly;
    private String counter_repeat = "nil";
    private int userInputRepeat = 0;

    private DatePickerDialog picker;
    private TimePickerDialog timePickerDialog;

    FirebaseAuth firebaseAuth;

    private String notificationFlag;

    SharedPreferences preferences;

    private TextView newAddActivityTitle;

    private LinearLayout layoutBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        cancelAddActivityButton = findViewById(R.id.cancelAddActivityButton);
        addAddActivityButton = findViewById(R.id.addAddActivityButton);

        titleAddActivityEdit = findViewById(R.id.titleAddActivityEdit);
        dateAddActivityEdit = findViewById(R.id.dateAddActivityEdit);
        timeAddActivityEdit = findViewById(R.id.timeAddActivityEdit);

        newAddActivityTitle = findViewById(R.id.newAddActivityTitle);
        layoutBackground = findViewById(R.id.layoutBackground);

        switchNotification = findViewById(R.id.switchNotification);

        numberOfReps = findViewById(R.id.numberOfReps);
        repeatRadioGrp = findViewById(R.id.repeatRadioGrp);
        repeatDaily = findViewById(R.id.repeatDaily);
        repeatWeekly = findViewById(R.id.repeatWeekly);
        repeatMonthly = findViewById(R.id.repeatMonthly);

        final HashMap<String, HashMap<String, String>> hashMap = new HashMap<>();

        firebaseAuth = FirebaseAuth.getInstance();

        dateAddActivityEdit.setInputType(InputType.TYPE_NULL);

        // Create a date picker
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

        // Create a time picker
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

        // Switch to allow notifications
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // If user has no input, catch exception
                    try {
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
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    notificationFlag = "FALSE";
                }
            }
        });

        // Set repeat to daily
        repeatDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Daily");
                counter_repeat = "d";
            }
        });

        // Set repeat to weekly
        repeatWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Weekly");
                counter_repeat = "w";
            }
        });

        // Set repeat to monthly
        repeatMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Monthly");
                counter_repeat = "m";
            }
        });

        // Add button to push all details to database
        addAddActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String dateTime = String.format("%s %s",dateAddActivityEdit.getText(),timeAddActivityEdit.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime ldt = LocalDateTime.parse(dateTime,formatter);

                String userChoice = numberOfReps.getText().toString();


                for (int i=0;i<=Integer.parseInt(userChoice);i++) {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    Log.i("DATE", dateFormatter.format(ldt));
                    hashMap.clear();


                    // Adding current activity details to hashMap
                    hashMap.put("Activity", new HashMap<String, String>());
                    hashMap.get("Activity").put("title", titleAddActivityEdit.getText().toString());
                    hashMap.get("Activity").put("date", dateFormatter.format(ldt));
                    hashMap.get("Activity").put("time", timeFormatter.format(ldt));

                    final FirebaseUser user =  firebaseAuth.getCurrentUser();
                    final String userId = user.getUid();
                    final DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference().child("Users");
                    mRef.child(userId).push().setValue(hashMap);

                    ZonedDateTime zdt = ldt.atZone(ZoneId.of("Singapore"));
                    long ms = zdt.toInstant().toEpochMilli();
//                    System.out.println(ms);
//
//                    System.out.println(System.currentTimeMillis());
                    long delay = (ms - System.currentTimeMillis());

                    if (notificationFlag == "TRUE" && delay > 0) {
                        scheduleNotification(getNotification(titleAddActivityEdit.getText().toString()), delay );
                    }


                    if (counter_repeat.equals("d")) {
                        ldt = ldt.plusDays(1);
                    } else if (counter_repeat.equals("w")) {
                        ldt = ldt.plusWeeks(1);
                    } else if (counter_repeat.equals("m")) {
                        ldt = ldt.plusMonths(1);
                    }

                }
                // End the activity
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Change background colour
        preferences = getSharedPreferences("color", MODE_PRIVATE);
        String bgdColor = preferences.getString("background_color", getString(R.string.black));
        String txtColor = preferences.getString("text_color", getString(R.string.white));

        layoutBackground.setBackgroundColor(Color.parseColor(bgdColor));
        newAddActivityTitle.setTextColor(Color.parseColor(txtColor));
        cancelAddActivityButton.setTextColor(Color.parseColor(txtColor));
        addAddActivityButton.setTextColor(Color.parseColor(txtColor));
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
        builder.setSmallIcon(R.drawable.ic_iconfinder_calendar_285670);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
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
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.smartmedicationreminder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smartmedicationreminder.service.StartPatientAlarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK_INT;


public class setReminder extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    Button time,date,addEvent;
    TextView timeValue,dateValue;
    CheckBox setAlarmCheckBox;
    DBOpenHelper dbOpenHelper;
    AlarmManager alarmManager;
    private int alarmYear=0,alarmMonth=0,alarmDay=0,alarmHour=0,alarmMinuit=0;
    EditText eventname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        eventname=findViewById(R.id.eventname);
        time=findViewById(R.id.setTimeBtn);
        date=findViewById(R.id.setDateBtn);
        timeValue=findViewById(R.id.setTimeText);
        dateValue=findViewById(R.id.setDateText);
        setAlarmCheckBox=findViewById(R.id.setAlarmCheckBox);
        addEvent = findViewById(R.id.addevent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setAlarmCheckBox.isChecked()) {
//                    SaveEvent(eventname.getText().toString(), EventTime.getText().toString(), date, month, year, "on");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit);
                    showDialog(setReminder.this,"ALERT","Do you want to save this patients medicine reminder?",calendar);
                /*} else {
                    SaveEvent(eventname.getText().toString(), EventTime.getText().toString(), date, month, year, "off");
                }*/
                }else{
                    Toast.makeText(setReminder.this, "Please check the alarm first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minuts = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(setReminder.this, R.style.DialogTheme
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        alarmHour=hourOfDay;
                        alarmMinuit=minute;
                        SimpleDateFormat hformate = new SimpleDateFormat("kk:mm a", Locale.ENGLISH);
                        String event_Time = hformate.format(c.getTime());
                        timeValue.setText(event_Time);
                    }
                }, hours, minuts, false);
                timePickerDialog.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(setReminder.this, R.style.DialogTheme,
                        datep, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }
    DatePickerDialog.OnDateSetListener datep = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            alarmYear=year;
            alarmMonth=monthOfYear;
            alarmDay=dayOfMonth;
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf =new  SimpleDateFormat(myFormat, Locale.US);
        dateValue.setText(sdf.format(myCalendar.getTime()));
    }
    //TO SAFE EVENT IN DATABASE
    private void SaveEvent9(String event, String time, String date, String month, String year, String notify) {
        dbOpenHelper = new DBOpenHelper(setReminder.this);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, notify, database);
        dbOpenHelper.close();
        Toast.makeText(setReminder.this, "Event Saved", Toast.LENGTH_SHORT).show();
    }
    private int getRequestCode(String date,String event,String time){
        int code = 0;
        dbOpenHelper = new DBOpenHelper(setReminder.this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date,event,time,database);
        while (cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();
        Log.d(TAG, "getRequestCode: "+code);
        return code;
    }
    private void setAlarm(Calendar targetCal) {
        Intent startIntent = new Intent(getBaseContext(), StartPatientAlarm.class);
        final PendingIntent startPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, startIntent, 0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), startPendingIntent);
    }
    public void showDialog(final Context context, String title, String msg, final Calendar calendar){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {
                        setAlarm(calendar);
                        finish();
                        Toast.makeText(context, "Patients alarm set succesfully, please be patients for the reminder.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
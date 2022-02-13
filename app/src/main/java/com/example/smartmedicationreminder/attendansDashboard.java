package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.smartmedicationreminder.PatientBottomNav;
import com.example.smartmedicationreminder.R;
import com.example.smartmedicationreminder.addAttendant;
import com.google.firebase.auth.FirebaseAuth;

public class attendansDashboard extends AppCompatActivity {
    CardView add_Attendant, setReminder, Report, settings, healthStatus;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendantsdashboard);
        //button handler
        //Logout = findViewById(R.id.btn_LogoutPatient);

        //card_view handler
        //  add_Attendant = (CardView) findViewById(R.id.addAttendant);
        setReminder = (CardView) findViewById(R.id.setReminder);
        Report = (CardView) findViewById(R.id.report);
        settings = (CardView) findViewById(R.id.settings);
        healthStatus = (CardView) findViewById(R.id.healthStatus);


        //2) set_Reminder OnClickListener
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PatientBottomNav.class);
                startActivity(i);
            }
        });

        //4) report OnClickListener
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),addAttendant.class);
                startActivity(i);
            }
        });

        //5) healthStatus OnClickListener
        healthStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),addAttendant.class);
                startActivity(i);
            }
        });
        //6) Settings OnClickListener
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),addAttendant.class);
                startActivity(i);
            }
        });
    }


    //pressing back button for generating the dialog box for closing an app
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder build = new AlertDialog.Builder(attendansDashboard.this);
        build.setTitle("Exit");
        build.setMessage("Are You sure you want to exit ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                attendansDashboard.super.onBackPressed();
            }
        }).setNegativeButton("No", null).setCancelable(false);
        AlertDialog alert = build.create();
        alert.show();
    }
}
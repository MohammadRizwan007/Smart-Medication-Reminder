package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

public class  ProfilePatientActivity extends AppCompatActivity {
    CardView add_Attendant, addDoctor, setReminder, Report, settings, healthStatus;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);
        //button handler
       //Logout = findViewById(R.id.btn_LogoutPatient);

        //card_view handler
        add_Attendant = (CardView) findViewById(R.id.addAttendant);
        addDoctor = (CardView) findViewById(R.id.addDoctor);
        setReminder = (CardView) findViewById(R.id.setReminder);
        Report = (CardView) findViewById(R.id.report);
        settings = (CardView) findViewById(R.id.settings);
        healthStatus = (CardView) findViewById(R.id.healthStatus);

       //1) addAttendant OnClickListener
        add_Attendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Choice_Attendant.class);
                startActivity(i);
            }
        });


        //2) set_Reminder OnClickListener
        setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PatientBottomNav.class);
                startActivity(i);
            }
        });

        //3) addDoctor OnClickListener
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ContactsActivity.class);
                startActivity(i);
            }
        });

        //4) addReport OnClickListener
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),addReport.class);
                startActivity(i);
            }
        });

        //5) healthStatus OnClickListener
        healthStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),heart_beat_anim_pat.class);
                startActivity(i);
            }
        });
        //6) Settings OnClickListener
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PatientSettingModule.class);
                startActivity(i);
                finish();
            }
        });
    }

    //pressing back button for generating the dialog box for closing an app
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder build = new AlertDialog.Builder(ProfilePatientActivity.this);
        build.setTitle("Exit");
        build.setMessage("Are You sure you want to exit ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
//                ProfilePatientActivity.super.onBackPressed();
            }
        }).setNegativeButton("No", null).setCancelable(false);
        AlertDialog alert = build.create();
        alert.show();
    }
}
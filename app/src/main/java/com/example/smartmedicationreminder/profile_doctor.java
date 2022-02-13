package com.example.smartmedicationreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class profile_doctor extends AppCompatActivity {
    private Button Logout;
    private CardView  Report, settings, addPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);
        //-------HANDLER------
        //Logout = findViewById(R.id.btn_LogoutPatient);
        Report = (CardView) findViewById(R.id.docReport);
        settings = (CardView) findViewById(R.id.docSettings);
        addPatient = (CardView) findViewById(R.id.addPatient);

        //3) settings
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Doctor_setting_module.class);
                startActivity(i);
            }
        });

        //5) healthStatus OnClickListener
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ContactsActivity.class);
                startActivity(i);
            }
        });

        //6) ViewReport OnClickListener
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ViewReport_doc.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder build = new AlertDialog.Builder(profile_doctor.this);
        build.setTitle("Exit");
        build.setMessage("Are You sure you want to exit ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
//                profile_doctor.super.onBackPressed();
            }
        }).setNegativeButton("No", null).setCancelable(false);
        AlertDialog alert = build.create();
        alert.show();
    }

}
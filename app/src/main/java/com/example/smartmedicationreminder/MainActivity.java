package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button doctor;
    Button patient;
    private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getRequiredPermissions();
        doctor = findViewById(R.id.btndoctor);
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Doctor has been clicked", Toast.LENGTH_SHORT).show();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), profile_doctor.class));
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, DoctorActivity.class);
                    startActivity(intent);
                }

            }
        });
        patient = findViewById(R.id.btnpatient);
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Patient has been clicked", Toast.LENGTH_SHORT).show();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), ProfilePatientActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, PatientActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    //pressing back button for generating the dialog box for closing an app
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
        build.setTitle("Exit");
        build.setMessage("Are You sure you want to exit ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        }).setNegativeButton("No", null).setCancelable(false);
        AlertDialog alert = build.create();
        alert.show();
    }

    private void getRequiredPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
            requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
//                    Toast.makeText(this, "DND services cannot function without the required permissions.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PatientSettingModule extends AppCompatActivity {

    CardView user_info, user_resetPass, user_VwReport, user_guidance, user_MedHistory, user_signOut;
    private ImageView closeButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    private String userID;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_setting_module);

        closeButton = (ImageView) findViewById(R.id.Img_cls);

        //card_view handler
        user_info = (CardView) findViewById(R.id.viewInfor);
        user_resetPass = (CardView) findViewById(R.id.resetPass);
        user_guidance = (CardView) findViewById(R.id.help);
        user_VwReport = (CardView) findViewById(R.id.Viewreport);
        user_MedHistory = (CardView) findViewById(R.id.medHis);
        user_signOut = (CardView) findViewById(R.id.logout_user);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        currentUser = fAuth.getCurrentUser();

        documentReference = fStore.collection("users").document(userID);

        //1) User Info OnClickListener
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewPatientDetail.class);
                startActivity(i);
            }
        });


        //2) Reset Password OnClickListener
        user_resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),ResetPasswordActivity.class);
//                startActivity(i);
                final EditText resetPass = new EditText(v.getContext());

                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Reset Password ?");
                passResetDialog.setMessage("Enter New Password (E.g: Abc1234)");
                passResetDialog.setView(resetPass);

                passResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String new_Password = resetPass.getText().toString();
                        currentUser.updatePassword(new_Password).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PatientSettingModule.this, "Successfully Reset Password", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PatientSettingModule.this, "Password Reset UnsuccessFul ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passResetDialog.setNegativeButton("No", null).setCancelable(false);
                passResetDialog.create().show();
            }
        });

        //3) View Report OnClickListener
        user_VwReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewReport_pat.class);
                startActivity(i);
                finish();
            }
        });

        //4) guidance or help OnClickListener
        user_guidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder popUPHelp = new AlertDialog.Builder(v.getContext());
                popUPHelp.setTitle("Help !");
                popUPHelp.setMessage("If you have any Query Contact the developers\n");
                popUPHelp.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PatientSettingModule.this, "Thanks For Your Concern", Toast.LENGTH_SHORT).show();
                    }
                });
                popUPHelp.setCancelable(false);
                popUPHelp.create().show();
            }
        });

        //5) upload Profile Pic OnClickListener
        user_MedHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), uploadProfPicPat.class);
                startActivity(i);
            }
        });
        //6) SignOUT OnClickListener
        user_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PatientSettingModule.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        //2) close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfilePatientActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    //pressing back button for generating the dialog box for closing an app
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ProfilePatientActivity.class);
        startActivity(i);
        finish();

    }

}
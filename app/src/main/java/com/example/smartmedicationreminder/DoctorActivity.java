package com.example.smartmedicationreminder;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorActivity extends AppCompatActivity {

    TextView signup_doctor, doc_forgetPass, forgetPassword;
    private EditText demail, dpassword;
    private Button Login_doctor;
    private FirebaseAuth fAuth;
    private String userID;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        //userID = ;

        //Text_vw_signup handler
        signup_doctor = (TextView) findViewById(R.id.txt_singup_doctor);
        doc_forgetPass = (TextView) findViewById(R.id.txtForgetPassword);

        //button Login
        Login_doctor = (Button) findViewById(R.id.btnLoginDoctor);

        //Edit Text email and pass
        demail = (EditText) findViewById(R.id.edt_doc_email);
        dpassword = (EditText) findViewById(R.id.edt_doc_password);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();


        Login_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = demail.getText().toString().trim();
                String password = dpassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    demail.setError("Email is Required.");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    demail.setError("Please Enter a Valid Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    dpassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    dpassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                Toast.makeText(DoctorActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), profile_doctor.class));
                            } else {
                                Toast.makeText(DoctorActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
        });


//        TextView signup_doctor = (TextView) findViewById(R.id.txt_singup_doctor);

        signup_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorActivity.this, "Sign up has been called", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorActivity.this, SignUp_DoctorActivity.class);
                startActivity(intent);

            }
        });

        doc_forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }

        });

    }

}
//        final DocumentReference documentReference = fStore.collection("doctors").document(fAuth.getCurrentUser().getUid());

        // if the user is valid it navigates towards doc dashboard
//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), profile_doctor.class));
//            finish();
//        }

        // Login Doctor --
//        Login_doctor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = demail.getText().toString().trim();
//                String password = dpassword.getText().toString().trim();
//
//                if (TextUtils.isEmpty(email)) {
//                    demail.setError("Email is Required.");
//                    return;
//                }
//                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    demail.setError("Please Enter a Valid Email");
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    dpassword.setError("Password is Required.");
//                    return;
//                }
//
//                if (password.length() < 6) {
//                    dpassword.setError("Password Must be >= 6 Characters");
//                    return;
//                }
//                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser fuser = fAuth.getCurrentUser();
//                                Toast.makeText(DoctorActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), profile_doctor.class));
//                        } else {
//                            Toast.makeText(DoctorActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });

//            }
//        });
//}

        // SIgnup DOctorset_Onclick
//        signup_doctor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(DoctorActivity.this, "Sign Up has been clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(DoctorActivity.this, SignUp_DoctorActivity.class);
//                startActivity(intent);
//            }
//        });



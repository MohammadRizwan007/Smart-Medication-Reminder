package com.example.smartmedicationreminder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientActivity extends AppCompatActivity {
   private EditText Pemail, Ppassword;
   private Button btnLogin;
   private TextView signUp, forgetPassword;
   private FirebaseAuth fAuth;
   private String userID;
   private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        //getSupportActionBar().setTitle("Log In");
        Pemail = findViewById(R.id.eTemail);
        Ppassword = findViewById(R.id.eTpassword);
        btnLogin = findViewById(R.id.btnLoginPatient);
        forgetPassword = findViewById(R.id.tv_forgetpass);
        signUp = findViewById(R.id.txt_signup_patient);
        fAuth = FirebaseAuth.getInstance();


//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), ProfilePatientActivity.class));
//            finish();
//        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Pemail.getText().toString().trim();
                String password = Ppassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Pemail.setError("Email is Required.");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Pemail.setError("Please Enter a Valid Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Ppassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    Ppassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            Toast.makeText(PatientActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ProfilePatientActivity.class));
                        } else {
                            Toast.makeText(PatientActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        TextView singup_patient = (TextView) findViewById(R.id.txt_signup_patient);
        singup_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PatientActivity.this, "Sign up has been called", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientActivity.this, SignUp_PatientActivity.class);
                startActivity(intent);

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }

        });
    }

}

package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    Button btnResetPassword;
    EditText ResetEmailInput;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        //getSupportActionBar().hide();

        ResetEmailInput = findViewById(R.id.eTemailVerify);
        btnResetPassword = findViewById(R.id.btnSignIn);
        fAuth =  FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.sendPasswordResetEmail(ResetEmailInput.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(TextUtils.isEmpty(ResetEmailInput.getText().toString().trim()))
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Please write your valid email address ", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Email send to your account", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ResetPasswordActivity.this,PatientActivity.class);
                            startActivity(intent);
                        }else
                        {
                            Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
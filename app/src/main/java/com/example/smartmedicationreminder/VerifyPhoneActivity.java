package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.chaos.view.PinView;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private String verificationId;
    private FirebaseAuth Auth;
    // private EditText editTextCode;
    private ProgressBar progressBar;
    private ImageView close;
    private  PinView pinCode;;
    private   Button verifyCode;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        if (getIntent().hasExtra("key")){
            key = getIntent().getStringExtra("key");
        }

        //getSupportActionBar().hide();

        //------HANDLER---------
        //progressbar Handler
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //image handler
        close =(ImageView) findViewById(R.id.Img_close);
        //pinView handler
        pinCode = (PinView) findViewById(R.id.eTemailVerify);
        //Button Handler
        verifyCode = (Button)  findViewById(R.id.btn_verifyCode);


        Auth = FirebaseAuth.getInstance();

        String phonenumber = getIntent().getStringExtra("phonenumber");
        Auth=FirebaseAuth.getInstance();
        sendVerificationCode(phonenumber);

        //onClick on VERIFY CODE BUTTON
        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code =   pinCode.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    pinCode.setError("Enter Code");
                    pinCode.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
        //pressing cross move to start the activity
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if (key.equals("doctor")){
                                Intent intent = new Intent(VerifyPhoneActivity.this,profile_doctor.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {

                                Intent intent = new Intent(VerifyPhoneActivity.this,PatientActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        }else {
                            Toast.makeText(VerifyPhoneActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }

    public void sendVerificationCode(String number){
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
                signInWithCredential(credential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };
}

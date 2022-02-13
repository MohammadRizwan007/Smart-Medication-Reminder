package com.example.smartmedicationreminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class SignUp_PatientActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseFirestore fStore;
    private String userID;
    private EditText userName, pPassword, pEmail, pPhone;
    private FirebaseAuth fAuth;
    private ImageView profileImage;
    private StorageReference storageReference;
    private Button register;
    private Uri imageUri;
    private TextView passStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__patient);
        //getSupportActionBar().setTitle("Sign Up");
        pPhone = findViewById(R.id.eTphonePatient);
        register = findViewById(R.id.btnRegisterPatient);
        userName = findViewById(R.id.eTuserNamepatient);
        pPassword = findViewById(R.id.eTpasswordPatient);
        pEmail = findViewById(R.id.eTemailPatient);
        profileImage = findViewById(R.id.IV_PatprofPic);

        //TextView
        passStat = (TextView) findViewById(R.id.tv_passStat);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),ProfilePatientActivity.class));
            finish();
        }

        //ImageUpload
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        // 2) registration
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUp_PatientActivity.this, "Please Wait...", Toast.LENGTH_SHORT).show();
                final String email = pEmail.getText().toString().trim();
                final String password = pPassword.getText().toString().trim();
                final String fullName = userName.getText().toString();
                final String number = pPhone.getText().toString();

          /*      if (profileImage.equals(Uri.EMPTY))
                {

                }
          */
                if(TextUtils.isEmpty(fullName)){
                    userName.setError("User Name is Required");
                }

                if(TextUtils.isEmpty(email)){
                    pEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    pPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    pPassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                if (number.isEmpty() || number.length() < 10) {
                    pPhone.setError("Enter Valid Number");
                    pPhone.requestFocus();
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // send verification link
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(SignUp_PatientActivity.this, "Verification Link send to your Gmail", Toast.LENGTH_SHORT).show();

                                    }else
                                    Toast.makeText(SignUp_PatientActivity.this, "Failed to send verification Try again", Toast.LENGTH_SHORT).show();
                                }
                            });

                           /* fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });*/
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",number);
                           // user.put("AttendantProfileImage", imageUri.toString());
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
//                            String phonenumber = number;
//                            Intent intent = new Intent(SignUp_PatientActivity.this, VerifyPhoneActivity.class);
//                            intent.putExtra("phonenumber", phonenumber);
//                            startActivity(intent);
                            Intent intent = new Intent(SignUp_PatientActivity.this, PatientActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignUp_PatientActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           }

                    }

                });
            }


        });

        //Validate password Pattern
        pPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = pPassword.getText().toString().trim();
                validatePassword(password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
      // Validate password Pattern
    public void validatePassword(String pass) {
        Pattern CapsLetter = Pattern.compile("[A-Z]");
        Pattern SmallLetter = Pattern.compile("[a-z]");
        Pattern numCase = Pattern.compile("[0-9]");
        if (!CapsLetter.matcher(pass).find() || !SmallLetter.matcher(pass).find() || !numCase.matcher(pass).find()) {
            passStat.setText("Password is weak");
            // passStatus.setTextColor(android.R.color.holo_red_dark);
        } else if (pass.length() < 7) {
            passStat.setText("Length is short");
            //passStatus.setTextColor(android.R.color.holo_red_dark);
        }   //{
        //passStatus.setText("Password is Strong");
        //passStatus.setTextColor(android.R.color.holo_green_light);
        //}
        else {
            passStat.setText("password is Strong");
            //passStatus.setTextColor(android.R.color.holo_green_light);
        }

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null ){
            Intent intent = new Intent(this,ProfilePatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }*/

    // ----------------------PROFILE PIC UPLOAD CODE----------------------------------------------------------

    // method for uploading an image
    public void choosePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // set image to our reportAddImage
            profileImage.setImageURI(imageUri);
            // method calling
            saveReport();
        }

    }

    public void saveReport() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Image is uploading");
        progressDialog.show();

        // Image Upload
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("PatientProfileImage").child(randomKey + ".jpg");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //DISMISS THE PROGRESSBAR WHICH SHOWS THAT UPLOAD IS FINISH
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                        Toast.makeText(SignUp_PatientActivity.this, "Image uploading is failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressbarPercentage = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //setting the percentage of uploading the report
                        progressDialog.setMessage("Percentage: " + (int) progressbarPercentage + "%");
                    }
                });
    }

}

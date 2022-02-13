package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartmedicationreminder.ProfilePatientActivity;
import com.example.smartmedicationreminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class addAttendant extends AppCompatActivity {

    private static final int DELAY_TIME_CODE = 3000;
    private EditText name, relationship, phoneNO;
    private Button btn_saveContact;
    private FirebaseAuth fAuth;
    private Uri imageUri;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private DocumentReference attendantRef;
    private FirebaseFirestore fStore;
    private ImageView att_prof_image;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendant);

        //Initialization fireStore
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();

        attendantRef = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid())
            .collection("attendant").document("attendantData");

        // handler for fields
        name = (EditText) findViewById(R.id.EDT_AttendantName);
        phoneNO = (EditText) findViewById(R.id.EDT_AttendantPhomeNO);
        relationship = (EditText) findViewById(R.id.EDT_AttendantRele);
        att_prof_image = (ImageView) findViewById(R.id.attendantImage);

        //saveContact  button handler
        btn_saveContact = (Button) findViewById(R.id.btn_SaveAttendant);

        // Save Contact onCLickListener
        btn_saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() != 0 && phoneNO.length() != 0 && relationship.length() != 0){
                    Map<String, Object> attendantMap = new HashMap<>();
                  //  attendantMap.put("attendant", true);
                    attendantMap.put("attendantName", name.getText().toString());
                    attendantMap.put("attendantPhone", phoneNO.getText().toString());
                    attendantMap.put("attendantRelation", relationship.getText().toString());
                    attendantMap.put("AttendantProfileImage", imageUri.toString());

                     // METHOD CALLING ADD ATTENDANT DETAILS
                    addAttendantDetails(attendantMap);
                    // Image saved !
                   // uploadImage();
                }
            }
        });
            // 2) profile image OnCLick
        att_prof_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        }); //oncreate END HERE
    }
            // METHOD FOR ADD ATTENDANT --
    private void addAttendantDetails(Map<String, Object> attendantMap){
        attendantRef.set(attendantMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Your Attendant Details has been saved", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(addAttendant.this, ProfilePatientActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, DELAY_TIME_CODE);
                }
            }
        });
    }

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
            att_prof_image.setImageURI(imageUri);
            // method calling
            saveReport();
        }

    }

    public void saveReport() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Image is uploading");
        progressDialog.show();

        // final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("AttendantProfileImage").child(userId + ".jpg");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //DISMISS THE PROGRESSBAR WHICH SHOWS THAT UPLOAD IS FINISH
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded Successfully .", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                        Toast.makeText(addAttendant.this, "Image uploading is failed", Toast.LENGTH_SHORT).show();
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
/*
    // uploading image on fireStore
    private void uploadImage()
    {
    *//*    DocumentReference documentReference = fStore.collection("users").document(currentUser.getUid())
                .collection("attendant").document("ProfileImage");*//*
        Map<String, String> userData = new HashMap<>();
        userData.put("AttendantProfileImage", imageUri.toString());
        attendantRef.set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(addAttendant.this, "Profile Pic is saving", Toast.LENGTH_SHORT).show();
                        }
                    },DELAY_TIME_CODE);

                }
            }
        });
    }*/
    //-------------------------------------TILL HERE ----------------------------------------
}

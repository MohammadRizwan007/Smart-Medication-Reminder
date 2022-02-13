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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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
import java.util.UUID;

public class addReport extends AppCompatActivity {

    private static final int DELAY_CODE = 3000;
    private Button saveReport;
    private ImageView reportAddImage, CloseButton;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        // HANDLER
        saveReport = findViewById(R.id.btn_saveReport);
        reportAddImage = findViewById(R.id.IV_addReport);
        CloseButton = (ImageView) findViewById(R.id.Img_clse);

    // initializing the Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Initializing the fireStore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //getting the current user ID
        userID = fAuth.getCurrentUser().getUid();


        //report add
        reportAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //method calling
                choosePicture();
            }
        });

        //save report to database firebase
        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        //2) close
        CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addReport.this, ProfilePatientActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // method for upl oading an image
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
            reportAddImage.setImageURI(imageUri);
            // method calling
            saveReport();
        }

    }

    public void saveReport() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Report is uploading");
        progressDialog.show();

       // final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("PatientReports/").child(userID + ".jpg");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //DISMISS THE PROGRESSBAR WHICH SHOWS THAT UPLOAD IS FINISH
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image is Uploading.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                        Toast.makeText(addReport.this, "Image uploading is failed", Toast.LENGTH_SHORT).show();
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

    // uploading image on fireStore
    private void uploadImage()
    {
        DocumentReference documentReference = fStore.collection("users").document(userID)
                .collection("report").document(userID);
            Map<String, String> userData = new HashMap<>();
                userData.put("userImage", imageUri.toString());
            documentReference.set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(addReport.this, "report is saving", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(addReport.this, ProfilePatientActivity.class);
                                startActivity(i);
                                finish();
                            }
                        },DELAY_CODE);

                    }
                }
            });
    }
}


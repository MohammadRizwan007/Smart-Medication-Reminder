package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ViewPatientDetail extends AppCompatActivity {

    private TextView PatName, Patnumber, PatEmail;;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private ImageView closeButton;
    private String userId;
    private StorageReference mStorageRef;
    private ImageView ProfImagePat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_detail);

        //handlers
        PatName = (TextView) findViewById(R.id.tv_PatName);
        Patnumber = (TextView) findViewById(R.id.tv_PatNumber);
        PatEmail = (TextView) findViewById(R.id.tv_patEmail);
        closeButton = (ImageView) findViewById(R.id.Img_cls);
        ProfImagePat = (ImageView) findViewById(R.id.Img_ViewPatProfpic);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        // retrieving image----
        mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfilePicPatient").child(userId + ".jpg");

        DocumentReference documentReference = fStore.collection("users").document(userId);

        //-----------------coding for retrieving image-----------
        try {
            final File localFile = File.createTempFile("Patient", "Profile");
            // Picture will get from this piece of code
            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ViewPatientDetail.this, "Image Retrieved", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ProfImagePat.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewPatientDetail.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    PatEmail.setText(value.getString("email"));
                    PatName.setText(value.getString("fName"));
                    Patnumber.setText(value.getString("phone"));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ----------------- TILL HERE ---------------------------------------------

        //2) close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PatientSettingModule.class);
                startActivity(i);
                finish();
            }
        });


    }
}
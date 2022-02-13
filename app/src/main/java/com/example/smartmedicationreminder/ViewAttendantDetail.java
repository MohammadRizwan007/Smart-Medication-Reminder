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

public class ViewAttendantDetail extends AppCompatActivity {

    private TextView Name, number, relationship;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private ImageView closeButton;
    private String userId;
    private ImageView ProfImageAtt;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendant_detail);

        //handlers
        Name = (TextView) findViewById(R.id.tv_attName);
        number = (TextView) findViewById(R.id.tv_attNumber);
        relationship = (TextView) findViewById(R.id.tv_attRele);

        closeButton = (ImageView) findViewById(R.id.Img_cls);
        ProfImageAtt = (ImageView) findViewById(R.id.Img_ViewattProfpic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        // retrieving image----
        mStorageRef = FirebaseStorage.getInstance().getReference().child("AttendantProfileImage").child(userId + ".jpg");

        DocumentReference documentReference = fStore.collection("users").document(userId)
                .collection("attendant").document("attendantData");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Name.setText(value.getString("attendantName"));
                number.setText(value.getString("attendantPhone"));
                relationship.setText(value.getString("attendantRelation"));
            }
        });
                //-----------------coding for retrieving image-----------
        try {
            final File localFile = File.createTempFile("Patient", "Reports");
            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ViewAttendantDetail.this, "Report Retrieved", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ProfImageAtt.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ViewAttendantDetail.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
                    Intent i = new Intent(getApplicationContext(),Choice_Attendant.class);
                    startActivity(i);
                    finish();
                }
            });
        
    }
}
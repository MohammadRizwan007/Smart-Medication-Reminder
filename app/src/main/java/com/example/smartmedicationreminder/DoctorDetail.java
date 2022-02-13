package com.example.smartmedicationreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DoctorDetail extends AppCompatActivity {

    private TextView DocName, Docnumber, DocEmail;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private ImageView closeButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        //handlers
        DocName = (TextView) findViewById(R.id.tv_DocName);
        Docnumber = (TextView) findViewById(R.id.tv_DocNumber);
        DocEmail = (TextView) findViewById(R.id.tv_DocEmail);
        closeButton = (ImageView) findViewById(R.id.Img_cls);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("doctors").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                DocEmail.setText(value.getString("email"));
                DocName.setText(value.getString("fName"));
                Docnumber.setText(value.getString("phone"));
            }
        });

        //2) close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),profile_doctor.class);
                startActivity(i);
                finish();
            }
        });


    }
}
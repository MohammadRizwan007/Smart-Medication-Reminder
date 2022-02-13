
package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class Choice_Attendant extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button _addAttendant, removeAttendant, ViewAttendant;
    private DocumentReference choiceRef;
    private FirebaseFirestore fstore;
    private FirebaseUser currentUser;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice__attendant);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // choiceRef = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid());

        ViewAttendant = (Button) findViewById(R.id.btn_ViewtAttendant);
        _addAttendant = (Button) findViewById(R.id.btn_addAttendant);
        removeAttendant = (Button) findViewById(R.id.btn_EditAttendant);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // get the reference of current attendant in choiceREF
        choiceRef = fstore.collection("users").document(currentUser.getUid())
                .collection("attendant")
                .document("attendantData");

        if (getIntent().hasExtra("attendant")) {
            String hasAttendant = getIntent().getStringExtra("attendant");
            if (hasAttendant.equals("No")) {
                removeAttendant.setVisibility(View.GONE);
            } else {
                _addAttendant.setVisibility(View.INVISIBLE);
            }
        }
        //method calling
        //attendantVisibility();

        _addAttendant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            Toast.makeText(getApplicationContext(), "You already add an attendant",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            Intent i = new Intent(Choice_Attendant.this, addAttendant.class);
                            i.putExtra("attendant", "No");
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });

    //2) View Attendant Detail----
        ViewAttendant.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        Intent i = new Intent(Choice_Attendant.this, ViewAttendantDetail.class);
        startActivity(i);
    }
    });

    //3) delete attendant OnClick
        removeAttendant.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        deleteDocument();
    }
    });
}

//    public void attendantVisibility(){
//
//    }

    // DELETE ATTENDANT DATA FROM PATIENT FIREBASE FIRE STORE
    public void deleteDocument() {
        final AlertDialog.Builder build = new AlertDialog.Builder(Choice_Attendant.this);
        build.setTitle("Remove Attendant");
        build.setMessage("Are You sure you want to delete attendant ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choiceRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Choice_Attendant.this, "your attendant is removed", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Choice_Attendant.this, "Failed to removed", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, e.toString());
                            }
                        });
            }
        }).setNegativeButton("No", null).setCancelable(false);
        AlertDialog alert = build.create();
        alert.show();

    }
}




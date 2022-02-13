package com.example.smartmedicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private TextView nameContact;
    private ImageView profileImage;
    private ImageView cancelCallBtn, acceptCallBtn;
    private String receiverUserId = "", receiverUserImage = "", receiverUserName = "";
    private String senderUserId = "", senderUserImage = "", senderUserName = "", checker = "";
    private String callingID = "", ringingID = "";
    private DatabaseReference usersRef;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mediaPlayer = MediaPlayer.create(this, R.raw.ringing);

        nameContact = findViewById(R.id.name_calling);
        profileImage = findViewById(R.id.profile_image_calling);
        cancelCallBtn = findViewById(R.id.cancel_call);
        acceptCallBtn = findViewById(R.id.make_call);
        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                checker = "clicked";
                cancelCallingUser();
            }
        });

        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                final HashMap<String, Object> callingPickUpMap = new HashMap<>();
                callingPickUpMap.put("picked", "picked");
                mediaPlayer.stop();
                usersRef.child(senderUserId).child("Ringing").updateChildren(callingPickUpMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Intent intent = new Intent(CallingActivity.this, VideoChatActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                mediaPlayer.stop();
            }
        });
        getAndSetUserProfileInfo();
    }


    private void getAndSetUserProfileInfo() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(receiverUserId).exists()) {
                    receiverUserImage = snapshot.child(receiverUserId).child("image").getValue().toString();
                    receiverUserName = snapshot.child(receiverUserId).child("name").getValue().toString();
                    nameContact.setText(receiverUserName);
                    Picasso.get().load(receiverUserImage).placeholder(R.drawable.profile_image).into(profileImage);
                }
                if (snapshot.child(senderUserId).exists()) {
                    senderUserImage = snapshot.child(senderUserId).child("image").getValue().toString();
                    senderUserName = snapshot.child(senderUserId).child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start();


        usersRef.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!checker.equals("clicked") && !snapshot.hasChild("Calling") && !snapshot.hasChild("Ringing")) {


                    final HashMap<String, Object> callingInfo = new HashMap<>();
                    callingInfo.put("calling", receiverUserId);

                    usersRef.child(senderUserId).child("Calling").updateChildren(callingInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        final HashMap<String, Object> ringingInfo = new HashMap<>();
//                                        ringingInfo.put("uid", receiverUserId);
//                                        ringingInfo.put("name", receiverUserId);
//                                        ringingInfo.put("image", receiverUserId);
                                        ringingInfo.put("ringing", senderUserId);

                                        usersRef.child(receiverUserId)
                                                .child("Ringing")
                                                .updateChildren(ringingInfo);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mediaPlayer.stop();
            }
        });
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(senderUserId).hasChild("Ringing") && !snapshot.child(senderUserId).hasChild("Calling")) {
                    acceptCallBtn.setVisibility(View.VISIBLE);
                }
                if (snapshot.child(receiverUserId).child("Ringing").hasChild("picked")) {
                    mediaPlayer.stop();
                    Intent intent = new Intent(CallingActivity.this, VideoChatActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mediaPlayer.stop();

            }
        });

    }

    private void cancelCallingUser() {
        //from sender side
        mediaPlayer.stop();
        usersRef.child(senderUserId);
        usersRef.child("Calling");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("calling")) {
                    callingID = snapshot.child("calling").getValue().toString();
                    usersRef.child("callingID")
                            .child("Ringing")
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        usersRef.child(senderUserId)
                                                .child("Calling")
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        startActivity(new Intent(CallingActivity.this, ContactsActivity.class));
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            });
                } else {
                    startActivity(new Intent(CallingActivity.this, ContactsActivity.class));
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mediaPlayer.stop();

            }
        });

        //from receiver side
        mediaPlayer.stop();
        usersRef.child(senderUserId)
                .child("Ringing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChild("ringing")) {
                            ringingID = snapshot.child("ringing").getValue().toString();
                            usersRef.child("ringingID")
                                    .child("Calling")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                usersRef.child(senderUserId)
                                                        .child("Ringing")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                startActivity(new Intent(CallingActivity.this, ContactsActivity.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            startActivity(new Intent(CallingActivity.this, ContactsActivity.class));
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mediaPlayer.stop();

                    }
                });
    }

}
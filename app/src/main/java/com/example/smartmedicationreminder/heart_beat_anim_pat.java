package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class heart_beat_anim_pat extends AppCompatActivity {

    private ImageView closeButton,heartBeating;
    Animation mHeartBeatAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_beat_anim_pat);

        closeButton = (ImageView) findViewById(R.id.Img_cls);


        //function calling
        initVariables();

        //2) close
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ProfilePatientActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initVariables() {
        heartBeating = (ImageView) findViewById(R.id.Img_heart);
        mHeartBeatAnim = AnimationUtils.loadAnimation(this,R.anim.heart_beat);
        heartBeating.startAnimation(mHeartBeatAnim);
    }
}
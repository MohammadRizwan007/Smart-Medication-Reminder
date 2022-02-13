package com.example.smartmedicationreminder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    //variable
    private static int DELAY_TIME = 3000;
    Animation Anim_top, Anim_bottom;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();
        //Animations hook's
        Anim_top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        Anim_bottom = AnimationUtils.loadAnimation(this, R.anim.bot_anim);

        image = (ImageView) findViewById(R.id.imageView5);

        //set animation to image and text view
        image.setAnimation(Anim_bottom);

        //handler Intent is used
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);

    }

}



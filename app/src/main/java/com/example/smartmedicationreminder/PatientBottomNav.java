package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.example.smartmedicationreminder.HomeFragment;
import com.example.smartmedicationreminder.ProgressFragment;
import com.example.smartmedicationreminder.TodayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientBottomNav extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout mainFrame;
    private HomeFragment homeFragment;
    private TodayFragment todayFragment;
    private ProgressFragment progressFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_bottom_nav);

        mainFrame=(FrameLayout)findViewById(R.id.main_frame);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nav);
        //constructor
        homeFragment=new HomeFragment();
        todayFragment =new TodayFragment();
        progressFragment =new ProgressFragment();
        //Default home fragment call
        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Fragment selectedFragment =null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;

                    case R.id.nav_today:
                        setFragment(todayFragment);
                        return true;

//                    case  R.id.progress_nav:
//                        setFragment(progressFragment);
//                        return true;

                    default:
                        return false;
                }


            }


        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}


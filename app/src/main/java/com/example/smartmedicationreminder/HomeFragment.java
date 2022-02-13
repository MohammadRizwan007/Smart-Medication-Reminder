package com.example.smartmedicationreminder;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class HomeFragment extends Fragment {

    Button button1;
    TextView text,dateText;
    ImageView image;
    LinearLayout medicine;
    LinearLayout measurement;
    LinearLayout activity;
    LinearLayout appointment;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        medicine = view.findViewById(R.id.medication_btn);
        measurement = view.findViewById(R.id.measurement_btn);
        activity = view.findViewById(R.id.activity_btn);
        appointment = view.findViewById(R.id.appointment_btn);
        text = (TextView) view.findViewById(R.id.text1);
        dateText = (TextView) view.findViewById(R.id.date);
        image = (ImageView) view.findViewById(R.id.image1);
        medicine.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), setReminder.class);
                startActivity(intent);
            }
        });
        measurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementList.class);
                startActivity(intent);
            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityList.class);
                startActivity(intent);
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  Intent intent = new Intent(getActivity() , setReminder.class);
                startActivity(intent);
            }
        });
        return view;

    }


}

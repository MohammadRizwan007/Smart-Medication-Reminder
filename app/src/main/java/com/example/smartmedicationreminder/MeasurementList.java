package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeasurementList extends AppCompatActivity {
    ListView listView;
    //string type array initialized
    String[] values={"Blood Pressure",
            "Blood Sugar(before the meal)",
            "Blood Sugar(after the meal)",
            "Heart Beat Rate",
            "Weight"};
    ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_list);

        //ListView
        listView=(ListView)findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MeasurementList.this, android.R.layout.simple_dropdown_item_1line, values);
        listView.setAdapter(adapter);


        //Expandable list
        expandableListView=(ExpandableListView)findViewById(R.id.expandable_list);
        List<String> Headings = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        HashMap<String, List<String>> childList= new HashMap<String, List<String>>();
        String heading_item[]= getResources().getStringArray(R.array.header_title);
        String l1[]=getResources().getStringArray(R.array.child_items);
        for (String title : heading_item){
            Headings.add(title);
        }
        for (String title : l1){
            L1.add(title);
        }
        childList.put(Headings.get(0),L1);
        MeasureDataAdopter dataAdopter=new MeasureDataAdopter(this, Headings, childList);
        expandableListView.setAdapter(dataAdopter);

    }

    }

package com.example.smartmedicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityList extends AppCompatActivity {
    ListView listView_Act;
    //string type array initialized
    String[] values={"Fitness Training",
            "Walking",
            "Physiotherapy",
            "Running",
            "Cycling"};

    ExpandableListView expandableListView_Act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //ListView
        listView_Act = (ListView) findViewById(R.id.listView_Act);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityList.this, android.R.layout.simple_dropdown_item_1line, values);
        listView_Act.setAdapter(adapter);

        //Expandable list
        expandableListView_Act = (ExpandableListView) findViewById(R.id.expandable_list_Act);
        //headings
        List<String> Heading = new ArrayList<String>();
        //Under Heading List_items
        List<String> L1 = new ArrayList<String>();
        //key reference
        HashMap<String, List<String>> childLists= new HashMap<String, List<String>>();

        String string_heading_name [] = getResources().getStringArray(R.array.header_title_Act);
        String string_child_items [] = getResources().getStringArray(R.array.child_items_Act);

        // loop to net number of headers
        for (String title : string_heading_name){
            Heading.add(title);
        }
        for (String title : string_child_items){
            L1.add(title);
        }
        childLists.put(Heading.get(0),L1);
        ActivityDataAdopter activityDataAdopter=new ActivityDataAdopter(this, Heading, childLists);
        expandableListView_Act.setAdapter(activityDataAdopter);
    }

}

package com.example.katherine.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;


/**
 * Created by katherine on 29/02/2016.
 */
public class ListActivity extends Activity {

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.list);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        final ArrayList<Person> listOfPeople = i.getParcelableArrayListExtra("listOfPeople");

        //populate zipcode
        String currLocation;
        if(data.getBoolean("isZipCode")) {
            currLocation = data.getString("zipCode");
        } else {
            currLocation = data.getString("cityNameFromGMaps");
            Log.d("currLocation", currLocation);
        }
        final TextView currLocationText = (TextView) findViewById(R.id.zipcode_list);
        currLocationText.setText(currLocation);

        //populate list
        ListAdapter adapter = new ListAdapter(this, R.layout.list_single, listOfPeople);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }

}

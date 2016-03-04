package com.example.katherine.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.example.katherine.mylibrary.Person;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button zipButton;
    private String zipCode;
    private ArrayList<Person> listOfPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipButton = (Button) findViewById(R.id.zip_button);

        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // data
                if(zipButton.getText() != null && zipButton.length() == 5) {
                    zipCode = zipButton.getText().toString();
                }
                createDummyData();

                // update watch view with each representative
                Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                watchIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
                startService(watchIntent);

                // on phone, transition to list view
                Intent listIntent = new Intent(MainActivity.this, ListActivity.class);
                listIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
                listIntent.putExtra("location", zipCode);
                startActivity(listIntent);
            }
        });
    }

    private void createDummyData() {
        // create people objects based on location
        Person rep1 = new Person();
        rep1.setFirstName("Barbara2");
        rep1.setLastName("Lee");
        rep1.setParty(Boolean.FALSE);
        rep1.setEmail("barbara@barbara.com");
        rep1.setWebsite("http://barbara.com");
        rep1.setLatestTweet("Hi, this is my latest tweet!");
        rep1.setTermStart("2012");
        rep1.setTermEnd("2014");

        ArrayList<String> bills = new ArrayList<String>();
        bills.add("Jul 09, 2013: Here's a bill.");
        bills.add("Aug 12, 2010: Another bill.");

        ArrayList<String> committees = new ArrayList<String>();
        committees.add("Labor Committee");

        rep1.setBills(bills);
        rep1.setCommittees(committees);
        // TODO: bills and committees

        Person sen1 = new Person();
        sen1.setFirstName("Loni");
        sen1.setLastName("Hancock");
        sen1.setParty(Boolean.TRUE);
        sen1.setEmail("loni@loni.com");
        sen1.setWebsite("http://loni.com");
        sen1.setLatestTweet("Hi, this is my latest tweet! Vote for me.");
        sen1.setTermStart("2012");
        sen1.setTermEnd("2014");

        listOfPeople = new ArrayList<Person>();
        listOfPeople.add(rep1);
        listOfPeople.add(sen1);
    }

}

package com.example.katherine.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button zipButton;
    private TextView currentLocationLink;
    private String location;
    private ArrayList<Person> listOfPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipButton = (Button) findViewById(R.id.zip_button);
        currentLocationLink = (TextView) findViewById(R.id.curr_location);

        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText zipCodeEnter = (EditText) findViewById(R.id.zip_code_enter);
                location = zipCodeEnter.getText().toString();

                if (location != null && location.length() == 5) {
                    createDummyData(location);
                    launchWatchView(location);
                    launchPhoneListView(location);
                }
            }
        });

        currentLocationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "Berkeley";
                createDummyData(location);
                launchWatchView(location);
                launchPhoneListView(location);
            }
        });


    }

    private void launchWatchView(String location) {
        Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        watchIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
        watchIntent.putExtra("location", location);
        startService(watchIntent);
    }

    private void launchPhoneListView(String location) {
        Intent listIntent = new Intent(MainActivity.this, ListActivity.class);
        listIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
        listIntent.putExtra("location", location);
        startActivity(listIntent);
    }

    private void createDummyData(String location) {
        if(location.equals("Berkeley")) {
            // create people objects based on location
            Person rep1 = new Person();
            rep1.setFirstName("Barbara");
            rep1.setLastName("Lee");
            rep1.setParty(false);
            rep1.setSenOrRep(Boolean.FALSE);
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
            sen1.setParty(false);
            sen1.setSenOrRep(Boolean.TRUE);
            sen1.setEmail("loni@loni.com");
            sen1.setWebsite("http://loni.com");
            sen1.setLatestTweet("Yay.");
            sen1.setTermStart("2012");
            sen1.setTermEnd("2014");

            Person sen2 = new Person();
            sen2.setFirstName("Senator");
            sen2.setLastName("Dos");
            sen2.setParty(Boolean.TRUE);
            sen2.setSenOrRep(Boolean.TRUE);
            sen2.setEmail("loni@loni.com");
            sen2.setWebsite("http://loni.com");
            sen2.setLatestTweet("Yet another tweet!");
            sen2.setTermStart("2012");
            sen2.setTermEnd("2014");


            listOfPeople = new ArrayList<Person>();
            listOfPeople.add(rep1);
            listOfPeople.add(sen1);
            listOfPeople.add(sen2);
        } else {
            // create people objects based on location
            Person rep1 = new Person();
            rep1.setFirstName("Doug");
            rep1.setLastName("Lamalfa");
            rep1.setParty(true);
            rep1.setSenOrRep(Boolean.FALSE);
            rep1.setEmail("barbara@barbara.com");
            rep1.setWebsite("http://barbara.com");
            rep1.setLatestTweet("Go Bears!");
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
            sen1.setFirstName("Dianne");
            sen1.setLastName("Feinstein");
            sen1.setParty(Boolean.TRUE);
            sen1.setSenOrRep(Boolean.TRUE);
            sen1.setEmail("loni@loni.com");
            sen1.setWebsite("http://loni.com");
            sen1.setLatestTweet("Hi, this is my latest tweet! Vote for me.");
            sen1.setTermStart("2012");
            sen1.setTermEnd("2014");

            Person sen2 = new Person();
            sen2.setFirstName("Barbara");
            sen2.setLastName("Boxer");
            sen2.setParty(false);
            sen2.setSenOrRep(Boolean.TRUE);
            sen2.setEmail("loni@loni.com");
            sen2.setWebsite("http://loni.com");
            sen2.setLatestTweet("Hi, this is my latest tweet! Vote for me.");
            sen2.setTermStart("2012");
            sen2.setTermEnd("2014");

            listOfPeople = new ArrayList<Person>();
            listOfPeople.add(rep1);
            listOfPeople.add(sen1);
            listOfPeople.add(sen2);
        }

    }

}

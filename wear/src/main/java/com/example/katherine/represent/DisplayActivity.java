package com.example.katherine.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;

/**
 * Created by katherine on 02/03/2016.
 */
public class DisplayActivity extends Activity {
    private ArrayList<Person> listOfPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_info);

        Intent i = getIntent();
        final ArrayList<Person> listOfPeople = i.getParcelableArrayListExtra("listOfPeople");

        //createDummyData();

        DisplayGridViewPagerAdapter adapter = new DisplayGridViewPagerAdapter(this, getFragmentManager(), listOfPeople);
        GridViewPager gridViewPager = (GridViewPager) findViewById(R.id.pager);
        gridViewPager.setAdapter(adapter);

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

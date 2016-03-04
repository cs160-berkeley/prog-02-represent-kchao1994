package com.example.katherine.represent;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;

/**
 * Created by katherine on 04/03/2016.
 */
public class ShakeSensor implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor mSensor;

    private final MainActivity mainActivity;

    private float sensorAcceleration;

    public ShakeSensor(SensorManager sm, MainActivity ma) {
        mSensorManager = sm;
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mainActivity = ma;
        sensorAcceleration = 0.0f;
    }

    protected void onResume() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float oldAcceleration = sensorAcceleration;
        sensorAcceleration = event.values[0];

        if(!(sensorAcceleration == oldAcceleration) && sensorAcceleration > 50.0) {
            Log.d("value", event.values[0] + " changed!");
            makeShake();
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int num) {
    }

    public void makeShake() {
        String newLocation = "randomLocation";

        Person rep1 = new Person();
        rep1.setFirstName("Random");
        rep1.setLastName("Representative");
        rep1.setParty(true);
        rep1.setSenOrRep(false);
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
        sen1.setFirstName("Suzie");
        sen1.setLastName("Random");
        sen1.setParty(true);
        sen1.setSenOrRep(true);
        sen1.setEmail("loni@loni.com");
        sen1.setWebsite("http://loni.com");
        sen1.setLatestTweet("Hi, this is my latest tweet! Vote for me.");
        sen1.setTermStart("2012");
        sen1.setTermEnd("2014");

        Person sen2 = new Person();
        sen2.setFirstName("Mike");
        sen2.setLastName("Random");
        sen2.setParty(true);
        sen2.setSenOrRep(true);
        sen2.setEmail("loni@loni.com");
        sen2.setWebsite("http://loni.com");
        sen2.setLatestTweet("Hi, this is my latest tweet! Vote for me.");
        sen2.setTermStart("2012");
        sen2.setTermEnd("2014");

        ArrayList<Person> listOfPeopleRandom = new ArrayList<Person>();
        listOfPeopleRandom.add(rep1);
        listOfPeopleRandom.add(sen1);
        listOfPeopleRandom.add(sen2);

        Intent watchIntent = new Intent(mainActivity.getBaseContext(), DisplayActivity.class);
        watchIntent.putParcelableArrayListExtra("listOfPeople", listOfPeopleRandom);
        watchIntent.putExtra("location", newLocation);
        mainActivity.startActivity(watchIntent);

        Intent phoneIntent = new Intent(mainActivity.getBaseContext(), WatchToPhoneService.class);
        phoneIntent.putParcelableArrayListExtra("listOfPeople", listOfPeopleRandom);
        phoneIntent.putExtra("location", newLocation);
        phoneIntent.putExtra("shake", true);
        mainActivity.startService(phoneIntent);
    }

}

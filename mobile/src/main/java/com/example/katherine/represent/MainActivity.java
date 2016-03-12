package com.example.katherine.represent;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.katherine.mylibrary.JSONParser;
import com.example.katherine.mylibrary.Person;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "KYz1JWoY0PDiV837Mmcg0eoUf";
    private static final String TWITTER_SECRET = "SlqdHFo5hjr89lPiImBBQcGlSmvR6IJnVLQXh6PyQLxYC8IF00";


    private Button zipButton;
    private TextView currentLocationLink;
    private ArrayList<Person> listOfPeople;

    private GoogleApiClient mGoogleApiClient;
    private String requestUrl;
    private static final String SUNLIGHT_API_KEY = "a453ee25cbc24eafa7688c060e801306";

    private Boolean isZipCode;
    private Location locationGMaps;
    private String cityNameFromGMaps;
    private String zipCode;
    private String countyName;
    private String percentRomneyVote;
    private String percentObamaVote;
    public Boolean isRandom;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        // create google API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_main);

        if(getIntent().getExtras() != null) {
            if(getIntent().getExtras().getString("shake").equals("shaken")) {
                getRandomLocation();
            }
        }

        zipButton = (Button) findViewById(R.id.zip_button);
        currentLocationLink = (TextView) findViewById(R.id.curr_location);

        zipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText zipCodeEnter = (EditText) findViewById(R.id.zip_code_enter);
                zipCode = zipCodeEnter.getText().toString();

                if (zipCode != null && zipCode.length() == 5) {
                    isZipCode = true;
                    getPeopleData(isZipCode);

                }

            }
        });

        currentLocationLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationGMaps = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                longitude = locationGMaps.getLongitude();
                latitude = locationGMaps.getLatitude();
                isZipCode = false;
                getPeopleData(isZipCode);

                //getRandomLocation();
            }
        });


    }

    private void getPeopleData(Boolean isZipCode) {
        if(isZipCode) {
            requestUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?zip=" + zipCode
                    + "&apikey=" + SUNLIGHT_API_KEY;

            new AsyncTaskParseJson().execute();
        }
        else {
            requestUrl = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude=" + latitude +
                    "&longitude=" + longitude + "%20&apikey=" + SUNLIGHT_API_KEY;

            new AsyncTaskParseJson().execute();
        }
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground (String... arg0) {

            try {
                JSONParser jParser = new JSONParser();
                ArrayList<String> votes = null;
                if(isZipCode) {
                    votes = jParser.getCountyFromLocation(zipCode, null, null, getApplicationContext());
                } else {
                    votes = jParser.getCountyFromLocation(null, latitude, longitude, getApplicationContext());
                }
                percentObamaVote = votes.get(0);
                percentRomneyVote = votes.get(1);
                listOfPeople = jParser.run(requestUrl);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if(!isZipCode) {
                cityNameFromGMaps = getCityFromLongLat(latitude, longitude);
            }

            launchWatchView();
            launchPhoneListView();
        }
    }

    private void launchWatchView() {
        Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        watchIntent.putExtra("isZipCode", isZipCode);
        watchIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
        watchIntent.putExtra("cityNameFromGMaps", cityNameFromGMaps);
        watchIntent.putExtra("locationGMaps", locationGMaps);

        watchIntent.putExtra("zipCode", zipCode);
        watchIntent.putExtra("obamaVote", percentObamaVote);
        watchIntent.putExtra("romneyVote", percentRomneyVote);
        startService(watchIntent);
    }

    private void launchPhoneListView() {
        Intent listIntent = new Intent(MainActivity.this, ListActivity.class);
        listIntent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
        listIntent.putExtra("cityNameFromGMaps", cityNameFromGMaps);
        listIntent.putExtra("locationGMaps", locationGMaps);
        listIntent.putExtra("isZipCode", isZipCode);
        listIntent.putExtra("zipCode", zipCode);
        startActivity(listIntent);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        return;
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onConnectionSuspended(int pause) {
        return;
    }



    public String getCityFromLongLat(Double latitude, Double longitude) {
        try{
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
        } catch(IOException e) {
            return null;
        }

        return null;
    }

    public void getRandomLocation() {
        isZipCode = false;
        new RandomAsyncTaskParseJson().execute();

    }

    public void onNewIntent(Intent intent) {
        Log.d("new intent detected", intent.getBundleExtra("shake") + " ");
    }

    public class RandomAsyncTaskParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground (String... arg0) {

            try {
                JSONParser jParser = new JSONParser();
                String randomLocation = jParser.randomLocation(getApplicationContext());
                ArrayList<Double> latlong = jParser.getLatLongFromRandom(randomLocation);
                latitude = latlong.get(0);
                longitude = latlong.get(1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            getPeopleData(false);
        }
    }

}

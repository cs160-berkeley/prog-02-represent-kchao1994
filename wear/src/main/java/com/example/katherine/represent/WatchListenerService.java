package com.example.katherine.represent;

import android.content.Intent;
import android.util.Log;

import com.example.katherine.mylibrary.Person;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;

/**
 * Created by katherine on 29/02/2016.
 */
public class WatchListenerService extends WearableListenerService {

    private static final String WEARABLE_DATA_PATH = "/people";
    private ArrayList<Person> listOfPeople;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        super.onMessageReceived( messageEvent );
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap dataMap;
        for (DataEvent event : dataEvents) {

            // Check the data type
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // Check the data path
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(WEARABLE_DATA_PATH)) {
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                    Log.v("dataPath", path);
                    Log.v("Data received on watch:", " " + dataMap);

                    transitionToDisplayActivity(dataMap);
/*
                    // run activity
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            transitionToDisplayActivity(dataMap);
                        }
                    };
                    mainHandler.post(myRunnable);
                    */
                }

            }
        }
    }

    public void transitionToDisplayActivity(DataMap dataMap) {
        // get data out from DataMap
        final Boolean isZipCode = dataMap.getBoolean("izZipCode");
        final String cityNameFromGMaps = dataMap.getString("cityNameFromGMaps");
        final String zipCode = dataMap.getString("zipCode");
        final String romneyVote = dataMap.getString("romneyVote");
        final String obamaVote = dataMap.getString("obamaVote");
        Log.d("isZipCode watchlistener", isZipCode + " ");

        ArrayList<DataMap> listOfPeopleData = dataMap.getDataMapArrayList("listOfPeople");
        listOfPeople = new ArrayList<Person>();

        for(int i = 0; i < listOfPeopleData.size(); i++) {
            DataMap personMap = listOfPeopleData.get(i);
            Person person = new Person();
            person.getFromDataMap(personMap);
            listOfPeople.add(person);
        }

        // make intent
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra("listOfPeople", listOfPeople);
        intent.putExtra("isZipCode", isZipCode);
        intent.putExtra("cityNameFromGMaps", cityNameFromGMaps);
        intent.putExtra("zipCode", zipCode);
        intent.putExtra("romneyVote", romneyVote);
        intent.putExtra("obamaVote", obamaVote);
        startActivity(intent);
    }

}
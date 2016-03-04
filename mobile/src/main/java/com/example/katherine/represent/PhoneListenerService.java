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

/**
 * Created by katherine on 29/02/2016.
 */
public class PhoneListenerService extends WearableListenerService {

    private static final String DISPLAY_PERSON_PATH = "/display_person";

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
                if (path.equals(DISPLAY_PERSON_PATH)) {

                    // get data out from DataMap
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    Person person = new Person();
                    person.getFromDataMap(dataMap);
                    Log.v("dataPath", path);
                    Log.v("Data received on phone:", " " + dataMap);
                    transitionToDetailedActivity(person);

                }


            }
        }
    }

    public void transitionToDetailedActivity(Person person) {
        // make intent
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("person", person);
        startActivity(intent);
    }
}

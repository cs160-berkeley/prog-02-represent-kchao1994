package com.example.katherine.represent;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
public class PhoneListenerService extends WearableListenerService {

    private static final String DISPLAY_PERSON_PATH = "/display_person";
    private static final String SHAKE_PATH = "/shake";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived( messageEvent );

        if( messageEvent.getPath().equalsIgnoreCase(SHAKE_PATH) ) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("shake", "shaken");
            Log.d("got here", "got here");
            startActivity(intent);

        }
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

                if (path.equals(SHAKE_PATH)) {
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                    Log.v("dataPath", path);
                    Log.v("Data received on phone:", " " + dataMap);

                    // get data out from DataMap
                    final String location = dataMap.getString("location");
                    ArrayList<DataMap> listOfPeopleData = dataMap.getDataMapArrayList("listOfPeople");
                    final ArrayList<Person> listOfPeople = new ArrayList<Person>();

                    for(int i = 0; i < listOfPeopleData.size(); i++) {
                        DataMap personMap = listOfPeopleData.get(i);
                        Person person = new Person();
                        person.getFromDataMap(personMap);
                        listOfPeople.add(person);
                    }

                    // run activity
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            transitionToListActivity(listOfPeople, location);
                        }
                    };
                    mainHandler.post(myRunnable);
                }


            }
        }
    }

    public void transitionToDetailedActivity(Person person) {
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("person", person);
        startActivity(intent);
    }

    public void transitionToListActivity(ArrayList<Person> listOfPeople, String location) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("listOfPeople", listOfPeople);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}

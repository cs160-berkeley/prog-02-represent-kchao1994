package com.example.katherine.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.katherine.mylibrary.Person;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by katherine on 29/02/2016.
 */
public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    private static final String DISPLAY_PERSON_PATH = "/display_person";
    private static final String SHAKE_PATH = "/shake";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data = intent.getExtras();

        // shake event
        if(data.getBoolean("shake")) {
            ArrayList<Person> listOfPeopleRandom = data.getParcelableArrayList("listOfPeople");
            String location = data.getString("location");

            // assemble data map
            ArrayList<DataMap> listOfPeopleData = new ArrayList<DataMap>();

            for(int i=0; i < listOfPeopleRandom.size(); i++) {
                DataMap dataMap = new DataMap();
                listOfPeopleRandom.get(i).putToDataMap(dataMap);
                listOfPeopleData.add(dataMap);
            }

            DataMap finalPeopleMap = new DataMap();
            finalPeopleMap.putDataMapArrayList("listOfPeople", listOfPeopleData);
            finalPeopleMap.putLong("time", new Date().getTime());
            finalPeopleMap.putString("location", location);

            new SendToDataLayerThread(SHAKE_PATH, finalPeopleMap).start();
        }

        // detail person view
        else {
            // get data from DisplayGridViewPagerAdapter and send to data layer
            final Person person = intent.getParcelableExtra("person");

            DataMap dataMap = new DataMap();
            person.putToDataMap(dataMap);

            dataMap.putLong("time", new Date().getTime());

            new SendToDataLayerThread(DISPLAY_PERSON_PATH, dataMap).start();

            // Send the message with the cat name
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mWatchApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    sendMessage("/" + person.getFirstName() + "-" + person.getLastName(), "");
                }
            }).start();
        }


        return START_STICKY;
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}



    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Log.d("T", path);
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }




    class SendToDataLayerThread extends Thread {
        String path;
        DataMap dataMap;

        // Constructor for sending data objects to the data layer
        SendToDataLayerThread(String p, DataMap data) {
            path = p;
            dataMap = data;
        }

        public void run() {
            // Construct a DataRequest and send over the data layer
            PutDataMapRequest putDMR = PutDataMapRequest.create(path);
            putDMR.getDataMap().putAll(dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            //request.setUrgent();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mWatchApiClient, request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "WatchToPhone DataMap: " + dataMap + " sent successfully to data layer ");
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }
        }
    }

}
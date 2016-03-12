package com.example.katherine.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.katherine.mylibrary.Person;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by katherine on 29/02/2016.
 */
public class PhoneToWatchService extends Service {

    private GoogleApiClient mApiClient;
    private static final String WEARABLE_DATA_PATH = "/people";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while(intent != null) {
            // Which rep are we showing?
            final ArrayList<Person> listOfPeople = intent.getParcelableArrayListExtra("listOfPeople");
            Bundle data = intent.getExtras();
            final Boolean isZipCode = data.getBoolean("isZipCode");
            Log.d("isZipCode phonetowatch", isZipCode + " ");
            final String cityNameFromGMaps = data.getString("cityNameFromGMaps");
            final String zipCode = data.getString("zipCode");
            final String obamaVote = data.getString("obamaVote");
            final String romneyVote = data.getString("romneyVote");

            // assemble data map
            ArrayList<DataMap> listOfPeopleData = new ArrayList<DataMap>();

            for(int i=0; i < listOfPeople.size(); i++) {
                DataMap dataMap = new DataMap();
                listOfPeople.get(i).putToDataMap(dataMap);
                listOfPeopleData.add(dataMap);
            }

            DataMap finalPeopleMap = new DataMap();
            finalPeopleMap.putBoolean("isZipCode", isZipCode);
            finalPeopleMap.putDataMapArrayList("listOfPeople", listOfPeopleData);
            finalPeopleMap.putLong("time", new Date().getTime());
            finalPeopleMap.putString("cityNameFromGMaps", cityNameFromGMaps);
            finalPeopleMap.putString("zipCode", zipCode);
            finalPeopleMap.putString("obamaVote", obamaVote);
            finalPeopleMap.putString("romneyVote", romneyVote);


            new SendToDataLayerThread(WEARABLE_DATA_PATH, finalPeopleMap).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    //first, connect to the apiclient
                    mApiClient.connect();
                    //now that you're connected, send a massage with the cat name
                    sendMessage("/" + "barbara-lee", "barbara-lee");
                }
            }).start();

            break;
        }


        return START_STICKY;
    }

    @Override //remember, all services need to implement an IBinder
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        //one way to send message: start a new thread and call .await()
        //see watchtophoneservice for another way to send a message
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
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
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mApiClient, request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + dataMap + " sent successfully to data layer ");
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap to data layer");
            }
        }
    }

}

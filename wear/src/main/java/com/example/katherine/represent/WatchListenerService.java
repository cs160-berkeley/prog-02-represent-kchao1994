package com.example.katherine.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by katherine on 29/02/2016.
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if ( messageEvent.getPath() != null ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("REP_NAME", value);
            Log.d("T", "about to start watch's main activity");
            startActivity(intent);

        }

        else {
            super.onMessageReceived( messageEvent );
        }

    }
}
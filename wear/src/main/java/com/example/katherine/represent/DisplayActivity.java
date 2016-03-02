package com.example.katherine.represent;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by katherine on 02/03/2016.
 */
public class DisplayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_card);
    }
}

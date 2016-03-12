package com.example.katherine.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float sensorAcceleration;
    private float oldAcceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        sensorAcceleration = event.values[0];
        if(sensorAcceleration != oldAcceleration && sensorAcceleration > 50.0) {
            Log.d("value", event.values[0] + " changed!");
            makeShake();
        }
        oldAcceleration = sensorAcceleration;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int num) {
    }

    public void makeShake() {

        Intent phoneIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        phoneIntent.putExtra("shake", true);
        startService(phoneIntent);
    }


}

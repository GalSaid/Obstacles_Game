package com.example.assignment1.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.assignment1.Interfaces.MoveCallback;


public class MoveDetector {

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private SensorEventListener sensorEventListener;

    private long timestamp = 0L;

    private final MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }



    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateMove(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 400){
            timestamp = System.currentTimeMillis();
            if(x > 2.0){
                if (moveCallback != null){
                    moveCallback.moveLeft();
                }
            }
            if (x < 2.0) {
                if (moveCallback != null) {
                    moveCallback.moveRight();
                }
            }
            if(y > 2.0){
                if (moveCallback != null){
                    moveCallback.moveUp();
                }
            }
            if (y < 2.0) {
                if (moveCallback != null) {
                    moveCallback.moveDown();
                }
            }
        }
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}

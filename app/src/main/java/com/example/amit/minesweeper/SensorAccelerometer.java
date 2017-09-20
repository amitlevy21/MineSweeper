package com.example.amit.minesweeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class SensorAccelerometer extends Service implements SensorEventListener {

    private static final String TAG = SensorAccelerometer.class.getSimpleName();
    protected SensorAccelerometer.SensorServiceBinder sensorServiceBinder = new SensorAccelerometer.SensorServiceBinder();// An IBinder implementation subclass
    private SensorManager sensorManager;
    boolean isListening = false;
    HandlerThread sensorThread;
    private Handler sensorHandler;

    float newPositionX, newPositionY, newPositionZ,
            firstPositionX, firstPositionY, firstPositionZ,
            calculatedX, calculatedY, calculatedZ;
    boolean changed = false;

    boolean shouldUpdate = false;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorThread = new HandlerThread(SensorAccelerometer.class.getSimpleName());
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    public SensorAccelerometer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorServiceBinder.sensorService = this;

        return sensorServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }

        return super.onUnbind(intent);
    }

    public String getTag() {
        return TAG;
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

        if (!(changed)) {
            firstPositionX = event.values[0];
            firstPositionY = event.values[1];
            firstPositionZ = event.values[2];
            changed = true;
        }
        newPositionX = event.values[0];
        newPositionY = event.values[1];
        newPositionZ = event.values[2];

        calculatedX = (Math.abs(firstPositionX - newPositionX));
        calculatedY = (Math.abs(firstPositionY - newPositionY));
        calculatedZ = (Math.abs(firstPositionZ - newPositionZ));
        if (calculatedX > 3 || calculatedY > 3 || calculatedZ > 3)
            shouldUpdate = true;
        else
            shouldUpdate = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    class SensorServiceBinder extends Binder {
        static final String START_LISTENING = "Start";
        private SensorAccelerometer sensorService;


        //////notify PlayActivity if the board should be updated
        boolean update() {
            if (shouldUpdate)
                return true;
            return false;
        }

        SensorAccelerometer getService() {
            return sensorService;
        }

        void notifyService(String msg) {

            Log.v(getTag(), SensorAccelerometer.class.getSimpleName() + " has got a message from its binding activity. Message: " + msg);

            if (msg == SensorAccelerometer.SensorServiceBinder.START_LISTENING && !isListening) {
                List<Sensor> sensorList= sensorManager.getSensorList(Sensor.TYPE_ALL);
                Log.v(getTag(), "Available sensors: " + sensorList);
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                if (sensor == null && sensorList.size() > 0) {
                    sensor = sensorList.get(0);
                }
                if (sensor == null) return;
                sensorManager.registerListener(getService(), sensor, SensorManager.SENSOR_DELAY_UI, sensorHandler);
                isListening = true;
            }
        }
    }
}


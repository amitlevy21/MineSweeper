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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class SensorAccelerometer extends Service implements SensorEventListener {
    public static final String SENSOR_SERVICE_BROADCAST_ACTION = "SENSOR_SERVICE_BROADCAST_ACTION";
    public static final String SENSOR_SERVICE_VALUES_KEY = "SENSOR_SERVICE_VALUES_KEY";
    public static final String PARCEL_RECORD_KEY = "record";

    private static final String TAG = SensorAccelerometer.class.getSimpleName();
    protected SensorServiceBinder sensorServiceBinder = new SensorServiceBinder();// An IBinder implementation subclass
    protected float values;
    private SensorManager sensorManager;
    boolean isListening = false;
    HandlerThread sensorThread;
    private Handler sensorHandler;

    private float mAccel;

    PlayActivity pl;


    @Override
    public void onCreate() {
        super.onCreate();

        sensorThread = new HandlerThread(SensorAccelerometer.class.getSimpleName());
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorServiceBinder.sensorService = this;

        // A.D: "You must always implement this method, but if you don't want to allow binding, then you should return null."
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

    protected void notifyEvaluation(float[] values) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SENSOR_SERVICE_BROADCAST_ACTION);

        //SomePojo somePojo = new SomePojo();
        //somePojo.setName("parcelable POJO");
        //somePojo.setLatitude(32.1151989);
        //somePojo.setLongitude(34.8196429);

        //broadcastIntent.putExtra(PARCEL_RECORD_KEY, somePojo);
        //broadcastIntent.putExtra(SENSOR_SERVICE_VALUES_KEY, values);
        //Log.v(getTag(), "Notifying new values: " + Arrays.toString(broadcastIntent.getFloatArrayExtra(SENSOR_SERVICE_VALUES_KEY)));
        //sendBroadcast(broadcastIntent);

        pl.updateBoard();
    }

    public float getValues() {
        return values;
    }

    /**
     * Used to specify the tag of the actual running class (sometimes I chose to work with a mock)
     * @return A String of the acting class's tag
     */
    public String getTag() {
        return TAG;
    }

    public SensorAccelerometer getSelf() {
        return this;
    }



    @Override
    public void onSensorChanged(final SensorEvent event) {
        final float[] values = new float[event.values.length];
        for (int i = 0; i < event.values.length; i++) {
            values[i] = event.values[i];// * 1000000.0f;
        }

        notifyEvaluation(values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class LocalBinder extends Binder {
        SensorAccelerometer getService() {
            // Return this instance of LocalService so clients can call public methods
            return SensorAccelerometer.this;
        }
    }

    class SensorServiceBinder extends Binder {
        static final String START_LISTENING = "Start";
        private SensorAccelerometer sensorService;

        SensorAccelerometer getService() {
            return sensorService;
        }

        void notifyService(String msg) {
            // A.D: "you must provide an interface that clients use to communicate with the service, by returning an IBinder."
            Log.v(getTag(), SensorAccelerometer.class.getSimpleName() + " has got a message from its binding activity. Message: " + msg);

            if (msg == SensorServiceBinder.START_LISTENING && !isListening) { // Why can we
                List<Sensor> sensorList= sensorManager.getSensorList(Sensor.TYPE_ALL);
                Log.v(getTag(), "Available sensors: " + sensorList);
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // Sensor.TYPE_GYROSCOPE will be null in Genymotion free edition
                if (sensor == null && sensorList.size() > 0) {
                    // Backup
                    sensor = sensorList.get(0); // for Genymotion sensors (Genymotion Accelerometer in my case)
                }

                if (sensor == null) return;

                sensorManager.registerListener(getService(), sensor, SensorManager.SENSOR_DELAY_UI, sensorHandler);
                isListening = true;
            }
        }
    }
}























  /*  private Context context;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView timelabel;
    private Handler mHandler;
    Runnable run;

    private float mAccel;

    public static boolean moved = false;

    private float mLastX, mLastY, mLastZ;
    private final float NOISE = (float) 3.0;


   // private final IBinder mBinder = new SensorAccelerometer();
    // Random number generator
    private final Random mGenerator = new Random();


    public class LocalBinder extends Binder {
        SensorAccelerometer getService() {
            // Return this instance of LocalService so clients can call public methods
            return SensorAccelerometer.this;
        }
    }

    /** method for clients */
   /* public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }


    public SensorAccelerometer() {
        // TODO Auto-generated constructor stub
        initialiseSensor();
    }


    public void initialiseSensor(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ALL);
        sensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensor(){
        sensorManager.unregisterListener(this);
        Toast.makeText(context, "Sensor Stopped..", Toast.LENGTH_SHORT).show();
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccel = SensorManager.GRAVITY_EARTH;
        float mAccelCurrent = (float)Math.sqrt(x*x+y*y+z*z);
        mAccel = mAccel * 0.9f + mAccelCurrent * 0.1f;

        if(mAccel > 1){

        }
    }


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }*/

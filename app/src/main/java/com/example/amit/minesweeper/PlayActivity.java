package com.example.amit.minesweeper;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;


public class PlayActivity extends AppCompatActivity implements Board.BoardListener, TilesFrameLayoutListener, LocationListener, SensorEventListener {

    public static final int BIGGER_FRACTION = 6;
    public static final int SMALLER_FRACTION = 12;
    private static final int DEFAULT_ANIMATION_DURATION = 1600;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    boolean runOnce = false; // prevent clicking to enter the following if


    private int seconds = 0;

    private Board board;



    private TilesFrameLayout mTilesFrameLayout;

    private LocationManager locationManager;
    private boolean didAlreadyRequestLocationPermission;
    private Location currentLocation;

    float newPositionX, newPositionY, newPositionZ,
            firstPositionX, firstPositionY, firstPositionZ,
            calculatedX, calculatedY, calculatedZ;
    boolean changed = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private LeaderBoard leaderBoard;
    private MainActivity.eDifficulty difficulty;
    private Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle bundle = getIntent().getExtras();
        difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        Button quit = (Button) findViewById(R.id.button_quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EndGameActivity.class);
                intent.putExtra(Keys.RESULT, Board.eState.LOSE);
                intent.putExtra(Keys.TIME, seconds);
                int cubes = board.getNumOfPressedBlocks();
                int goodFlags = board.getNumOfGoodFlags();
                intent.putExtra(Keys.GOOD_CUBES,cubes);
                intent.putExtra(Keys.GOOD_FLAGS,goodFlags);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);
                finish();

            }
        });
        board = buildBoard();
        tickEndlessly();

        mTilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tiles_frame_layout);
        mTilesFrameLayout.setOnAnimationFinishedListener(this);

        Button check = (Button) findViewById(R.id.check);


        /*Intent intent = new Intent(this, SensorAccelerometer.class);
        bindService(intent, sensorsBoundServiceConnection, Context.BIND_AUTO_CREATE);*/


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateBoard();

            }
        });
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        getCurrentLocation();

    }


    /*private ServiceConnection sensorsBoundServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            binder = (SensorAccelerometer.SensorServiceBinder) service;
            isServiceBound = true;
            notifyBoundService(SensorAccelerometer.SensorServiceBinder.START_LISTENING);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceBound = false;
        }
    };*/

    /*void notifyBoundService(String massageFromActivity) {
        if (isServiceBound && binder != null) {
            binder.notifyService(massageFromActivity);
        }
    }*/


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (!(changed)) {
            firstPositionX = sensorEvent.values[0];
            firstPositionY = sensorEvent.values[1];
            firstPositionZ = sensorEvent.values[2];
            changed = true;
        }
        if (seconds % 5 == 0) {

            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                newPositionX = sensorEvent.values[0];
                newPositionY = sensorEvent.values[1];
                newPositionZ = sensorEvent.values[2];

                calculatedX = (Math.abs(firstPositionX - newPositionX));
                calculatedY = (Math.abs(firstPositionY - newPositionY));
                calculatedZ = (Math.abs(firstPositionZ - newPositionZ));
                if (calculatedX >= 5 || calculatedY >= 5 || calculatedZ >= 5) {
                    updateBoard();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int a) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTilesFrameLayout.onResume();
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTilesFrameLayout.onPause();
    }

    public Board buildBoard() {

        Bundle bundle = getIntent().getExtras();
        MainActivity.eDifficulty difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);
        int numOfMines = bundle.getInt(Keys.NUM_OF_MINES);
        int boardSize = bundle.getInt(Keys.BOARD_SIZE);

        int buttonWidth = calculateButtonSize(difficulty);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);


        Board board = new Board(this, gridLayout, boardSize, buttonWidth, numOfMines);
        board.setBoardListener(this);
        return board;
    }

    public void updateBoard() {
        board.changeBoard();
    }


    public int calculateButtonSize(MainActivity.eDifficulty difficulty) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int fraction;

        if (difficulty.ordinal() <= MainActivity.eDifficulty.INTERMEDIATE.ordinal())
            fraction = SMALLER_FRACTION;
        else
            fraction = BIGGER_FRACTION;

        int theSmallerAxis = height < width ? height : width;
        return theSmallerAxis / fraction;
    }


    private void tickEndlessly() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (board.getState() == Board.eState.IN_PROGRESS) {
                    tick();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void tick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textTime = (TextView) findViewById(R.id.timer);
                textTime.setText(getString(R.string.play_activity_time) + " " + seconds);
                seconds++;
            }
        });

    }

    @Override
    public void onUpdate(final int numOfPressedBlocks, int numOfFlags, Board.eState state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView flagsOnPlay = (TextView) findViewById(R.id.flags);
                TextView CubesOnPlay = (TextView) findViewById(R.id.score);
                flagsOnPlay.setText(getString(R.string.flags) + " " + board.getNumOfFlags());
                CubesOnPlay.setText(getString(R.string.score) + " " + board.getNumOfPressedBlocks());
            }
        });



        if(!runOnce) { //prevent clicking while showing animation
            quit = (Button) findViewById(R.id.button_quit);
            quit.setClickable(false);

            if (state.equals(Board.eState.WIN) || state.equals(Board.eState.LOSE)) {
                runOnce = true;
                final Intent intent = new Intent(this, EndGameActivity.class);

                intent.putExtra(Keys.RESULT, state);
                intent.putExtra(Keys.TIME, seconds);

                ///////////sensor
               /* unbindService(sensorsBoundServiceConnection);*/


                intent.putExtra(Keys.GOOD_CUBES, numOfPressedBlocks);
                intent.putExtra(Keys.GOOD_FLAGS, numOfFlags);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                if (state.equals(Board.eState.LOSE)) {
                    mTilesFrameLayout.startAnimation();
                    presentEndGameActivity(intent);
                } else { // WIN

                    leaderBoard = LeaderBoard.getInstance();
                    boolean leader = leaderBoard.isLeader(difficulty,numOfPressedBlocks);
                    if (leader) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                        final EditText et = new EditText(this);

                        alertDialogBuilder.setView(et);
                        alertDialogBuilder.setTitle(R.string.new_leader);

                        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                leaderBoard.addPlayer(difficulty,new PlayerScore(et.toString(),numOfPressedBlocks, currentLocation, seconds),true);

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                animateWin();
                                presentEndGameActivity(intent);
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        animateWin();
                        presentEndGameActivity(intent);
                    }
                }
            }
        }
    }

    private void animateWin() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int mScreenHeight = displaymetrics.heightPixels;

        Drawable image = ContextCompat.getDrawable(this, R.drawable.ic_chuck_norris_approved);


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.play_main_layout);
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
        imageView.setVisibility(View.VISIBLE);

        imageView.setBackground(image);
        relativeLayout.addView(imageView);



        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f, 1.0f);
        fadeIn.setDuration(DEFAULT_ANIMATION_DURATION);
        fadeIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                imageView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        fadeIn.start();


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, -mScreenHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                quit.setTranslationY(value);
            }
        });

        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        valueAnimator.start();
    }

    private void presentEndGameActivity(final Intent intent) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, DEFAULT_ANIMATION_DURATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        board.setBoardListener(null); // clear reference to the PlayActivity for garbage collector to clean
        //unbindService(sensorsBoundServiceConnection);
    }

    @Override
    public void onAnimationFinished() {
        finish();
    }

    //Thanks Perry
    private void getCurrentLocation() {
        boolean isAccessGranted;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
            if (getApplicationContext().checkSelfPermission(fineLocationPermission) != PackageManager.PERMISSION_GRANTED ||
                    getApplicationContext().checkSelfPermission(coarseLocationPermission) != PackageManager.PERMISSION_GRANTED) {
                // The user blocked the location services of THIS app / not yet approved
                isAccessGranted = false;
                if (!didAlreadyRequestLocationPermission) {
                    didAlreadyRequestLocationPermission = true;
                    String[] permissionsToAsk = new String[]{fineLocationPermission, coarseLocationPermission};
                    requestPermissions(permissionsToAsk,LOCATION_PERMISSION_REQUEST_CODE);
                }
            } else {

                isAccessGranted = true;
            }

            if (isAccessGranted) {
                float metersToUpdate = 1;
                long intervalMilliseconds = 1000;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalMilliseconds, metersToUpdate, this);
            }

            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
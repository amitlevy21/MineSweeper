package com.example.amit.minesweeper;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaderBoardActivity extends AppCompatActivity {

    private static final String API_KEY = "AIzaSyCVyXqNtRzG5HYTbIJxUn0d_FoD-SGHNnM";

    private GoogleMap googleMap;
    private LeaderBoard leaderBoard;
    private RadioGroup difficultyRadioGroup;
    private TextView[][] leadersTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        difficultyRadioGroup.check(difficultyRadioGroup.getChildAt(0).getId());


        if (isGoogleMapsInstalled()) {
            // Add the Google Maps fragment dynamically
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            MapFragment mapFragment = MapFragment.newInstance();
            transaction.add(R.id.mapsPlaceHolder, mapFragment);
            transaction.commit();

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    setGoogleMap(googleMap);
                }
            });
        } else {

            // Notify the user he should install GoogleMaps (after installing Google Play Services)
            FrameLayout mapsPlaceHolder = (FrameLayout) findViewById(R.id.mapsPlaceHolder);
            TextView errorMessageTextView = new TextView(getApplicationContext());
            errorMessageTextView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            errorMessageTextView.setText(R.string.maps_notification_error);
            errorMessageTextView.setTextColor(Color.RED);
            mapsPlaceHolder.addView(errorMessageTextView);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); // Unmark to see the changes...

        boolean isAllowedToUseLocation = hasPermissionForLocationServices(getApplicationContext());
        if (isAllowedToUseLocation) {
            try {
                // Allow to (try to) set
                googleMap.setMyLocationEnabled(true);
            } catch (SecurityException exception) {
                Toast.makeText(this, "Error getting location" , Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location is blocked in this app", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasPermissionForLocationServices(Context context) {
        // Because the user's permissions started only from Android M and on...
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || !(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }
    
    public void loadLeadersFromFireBase() {
        leaderBoard = LeaderBoard.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

    }

    public void showLeaders() {
        int difficultyIndex = difficultyRadioGroup.getCheckedRadioButtonId();
        ScrollView leaderContianer = (ScrollView) findViewById(R.id.leader_board_scroll_scores);


        for (int i = 0; i < leadersTextView[difficultyIndex].length; i++) {
            leaderContianer.addView(leadersTextView[difficultyIndex][i], i,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}

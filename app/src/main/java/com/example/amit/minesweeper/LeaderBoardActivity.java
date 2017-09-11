package com.example.amit.minesweeper;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class LeaderBoardActivity extends AppCompatActivity implements LocationListener{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = -100;
    private static final String API_KEY = "AIzaSyBgE5dhgTuqZ9HlrdHzORNroU1sL04oY9Y";

    private LocationManager locationManager;
    private boolean didAlreadyRequestLocationPermission;
    private Location currentLocation;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

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
            errorMessageTextView.setText("ERROR");
            errorMessageTextView.setTextColor(Color.RED);
            mapsPlaceHolder.addView(errorMessageTextView);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //googleMap.setMyLocationEnabled(true);
                //getCurrentLocation();
            }
        }

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
                    requestPermissions(permissionsToAsk, LOCATION_PERMISSION_REQUEST_CODE);
                }
            } else {

                isAccessGranted = true;
            }


            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }


            if (isAccessGranted) {
                float metersToUpdate = 1;
                long intervalMilliseconds = 1000;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, intervalMilliseconds, metersToUpdate, this);
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
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Because the user's permissions started only from Android M and on...
            return true;
        }

        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // The user blocked the location services of THIS app
            return false;
        }

        return true;
    }
}

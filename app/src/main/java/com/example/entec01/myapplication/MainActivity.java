package com.example.entec01.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.entec01.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "--MainActivity";
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView textView;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // This method is called when a new location is found by the network location provider or Gps provider.
                lastKnownLocation = location;
                showLocation();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Status changed: " + status);
            }

            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider enabled: " + provider);
            }

            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider disabled: " + provider);
            }
        };

        // Register the listener with Location Manager's network provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ACCESS_COARSE_LOCATION);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        //Or Register the listener with Location Manager's gps provider
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                        //Or  Register the listener with Location Manager's gps provider
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    }

                } else {

                    // permission denied! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
        }
    }

    private void showLocation() {
        String loc = "Location changed: " + lastKnownLocation.toString()
                + "Longitude: " + lastKnownLocation.getLongitude()
                + "Latitude: " + lastKnownLocation.getLatitude()
                + "Accuracy: " + lastKnownLocation.getAccuracy()
                + "Altitude: " + lastKnownLocation.getAltitude()
                + "Speed: " + lastKnownLocation.getSpeed();
        Log.d(TAG, loc);
        textView.setText(loc);
    }
}

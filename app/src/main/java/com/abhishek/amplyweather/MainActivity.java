package com.abhishek.amplyweather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Context context;
    RelativeLayout splashMorning;
    RelativeLayout splashNoon;
    RelativeLayout splashNight;

    //To fetch the Lat-Long
    protected LocationManager locationManager;

    //Splash Screen Timer -> 6 seconds (or 6000 mil.seconds)
    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To make the splash activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //To set the res->layout for MainActivity.java
        setContentView(R.layout.activity_main);

        //To check the permission for Location Manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        //Get the current system time
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        //fetch the time based UI using their id and assign the appropriate names
        splashMorning = (RelativeLayout) findViewById(R.id.splash_morning);
        splashNoon = (RelativeLayout) findViewById(R.id.splash_noon);
        splashNight = (RelativeLayout) findViewById(R.id.splash_night);

        //Check if the time is in between 6.00 hours and 14.00 hours and set morning UI
        if (timeOfDay >= 6 && timeOfDay < 14) {
            if (splashMorning.getVisibility() != View.INVISIBLE) {
                splashMorning.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
            //Check if the time is in between 14.00 hours and 19.00 hours and set noon UI
        } else if (timeOfDay >= 14 && timeOfDay < 19) {
            if (splashNoon.getVisibility() != View.INVISIBLE) {
                splashNoon.setVisibility(View.VISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
            //Check if the time is in between 19.00 hours and 6.00 hours and set night UI
        } else if (timeOfDay >= 19 && timeOfDay < 6) {
            if (splashNight.getVisibility() == View.INVISIBLE) {
                splashNight.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
            }
        }
        //checkInternetAvailibility();
        checkLocationAvailability();
    }

    //Navigate to Home Activity after running the splash screen for 6 seconds -> time is based on the Animation speed for all 3 UIs
    public void navigate() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    //To check active internet connection

    public void checkInternetAvailibility() {
        if (isInternetAvailable()) {
            new IsInternetActive().execute();
            navigate();
        } else {
            Intent intent = new Intent(MainActivity.this, NoInternet.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isInternetAvailable() {
        try {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:", e.toString());
            return false;
        }
    }

    class IsInternetActive extends AsyncTask<Void, Void, String> {

        InputStream is = null;
        String json = "Fail";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL strUrl = new URL("http://icons.iconarchive.com/icons/designbolts/handstitch-social/24/Android-icon.png");
                //Icon for testing the server

                URLConnection connection = strUrl.openConnection();
                connection.setDoOutput(true);
                is = connection.getInputStream();
                json = "Success";

            } catch (Exception e) {
                e.printStackTrace();
                json = "Fail";
            }
            return json;

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.equals("Fail")) {
                    Log.e("Internet Not Active", "Internet Not Active");
                } else {
                    Log.e("Internet Active", "Internet Active" + result);
                }
            } else {
                Log.e("Internet Not Active", "Internet Not Active");
            }
        }

        @Override
        protected void onPreExecute() {
            Log.e("Validating", "Validating Internet");
            super.onPreExecute();
        }

    }
    //To fetch current Lat and Long of the user's location

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        getCompleteAddressString(latitude, longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    //To fetch the ZipCode of the user based on the lat and long that was retrieved
    private String getCompleteAddressString(double latitude, double longitude) {
        String zip = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                String postalCode = addresses.get(0).getPostalCode();
                zip = postalCode;
                Log.e("Zip", "Zip" + zip);

            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return zip;
    }

    private void checkLocationAvailability() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user with a msg to enable GPS
            new AlertDialog.Builder(this)
                    .setMessage("Please Enable GPS")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            //if GPS is enabled, check Internet and start the next activity
            checkInternetAvailibility();
        }
    }
}

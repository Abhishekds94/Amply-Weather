package com.abhishek.amplyweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    RelativeLayout splashMorning;
    RelativeLayout splashNoon;
    RelativeLayout splashNight;

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

        //Get the current system time
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        //fetch the time based UI using their id and assign the appropriate names
        splashMorning = (RelativeLayout) findViewById(R.id.splash_morning);
        splashNoon = (RelativeLayout) findViewById(R.id.splash_noon);
        splashNight = (RelativeLayout) findViewById(R.id.splash_night);

        //Check if the time is in between 6.00 hours and 14.00 hours and set morning UI
        if(timeOfDay >= 6 && timeOfDay < 14){
            if (splashMorning.getVisibility() != View.INVISIBLE) {
                splashMorning.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
        //Check if the time is in between 14.00 hours and 19.00 hours and set noon UI
        }else if(timeOfDay >= 14 && timeOfDay < 19){
            if (splashNoon.getVisibility() != View.INVISIBLE) {
                splashNoon.setVisibility(View.VISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
        //Check if the time is in between 19.00 hours and 6.00 hours and set night UI
        }else if(timeOfDay >= 19 && timeOfDay < 6){
            if (splashNight.getVisibility() == View.INVISIBLE) {
                splashNight.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
            }
        }
        navigate();
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

}

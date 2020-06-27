package com.abhishek.amplyweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        Toast.makeText(this, "Time-"+timeOfDay, Toast.LENGTH_SHORT).show();
        splashMorning = (RelativeLayout) findViewById(R.id.splash_morning);
        splashNoon = (RelativeLayout) findViewById(R.id.splash_noon);
        splashNight = (RelativeLayout) findViewById(R.id.splash_night);

        if(timeOfDay >= 6 && timeOfDay < 14){
            if (splashMorning.getVisibility() != View.INVISIBLE) {
                splashMorning.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
        }else if(timeOfDay >= 14 && timeOfDay < 19){
            if (splashNoon.getVisibility() != View.INVISIBLE) {
                splashNoon.setVisibility(View.VISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
                splashNight.setVisibility(View.INVISIBLE);
            }
        }else if(timeOfDay >= 19 && timeOfDay < 6){
            if (splashNight.getVisibility() == View.INVISIBLE) {
                splashNight.setVisibility(View.VISIBLE);
                splashNoon.setVisibility(View.INVISIBLE);
                splashMorning.setVisibility(View.INVISIBLE);
            }
        }

    }
}

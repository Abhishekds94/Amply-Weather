package com.abhishek.amplyweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.amplyweather.Network.APIManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private ImageView img;
    private TextView contentTV;

    private TextView tv_city;
    private TextView tv_state;
    private TextView tv_highTempVal;
    private TextView tv_lowTempVal;
    private TextView tv_currTemp;
    private TextView tv_currWeather;

    private AdView mAdView;

    RelativeLayout DashboardBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_dashboard);

        //Initialize all the required views to display the data
        img = findViewById(R.id.img_icon);

        contentTV = findViewById(R.id.textview1);
        tv_city = findViewById(R.id.tv_city);
        tv_state = findViewById(R.id.tv_state);
        tv_highTempVal = findViewById(R.id.tv_highTempVal);
        tv_lowTempVal = findViewById(R.id.tv_lowTempVal);
        tv_currTemp = findViewById(R.id.tv_currTemp);
        tv_currWeather = findViewById(R.id.tv_currWeather);


        //Call the method to initialize data
        initData();

        //For Banner Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        View adContainer = findViewById(R.id.adMobView);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        ((FrameLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Get the current system time
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        DashboardBg = (RelativeLayout) findViewById(R.id.activity_dashboard);

        //Check if the time is in between 6.00 hours and 14.00 hours and set morning BG
        if (timeOfDay >= 6 && timeOfDay < 14) {
            DashboardBg.setBackground(ContextCompat.getDrawable(this, R.drawable.morn));
            //Check if the time is in between 14.00 hours and 19.00 hours and set noon BG
        } else if (timeOfDay >= 14 && timeOfDay < 19) {
            DashboardBg.setBackground(ContextCompat.getDrawable(this, R.drawable.eve));
            //Check if the time is in between 19.00 hours and 6.00 hours and set night BG
        } else if (timeOfDay >= 19 && timeOfDay < 6) {
            DashboardBg.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
        }

    }

    private void initData() {

        //Loader
        final ProgressDialog dialog = ProgressDialog.show(this
                , ""
                , "Sever Connection ..."
        );
        dialog.show();


        //Call to fetch the requested details from the API response
        APIManager.onAPIConnectionResponse(APIManager.AMPLY_WEATHER_URL, null, null, APIManager.APIMethod.POST, new APIManager.APIManagerCallback() {
            @Override
            public void onEventCallBack(JSONObject obj) {
                try {
                    dialog.dismiss();
                    //skyDescription
                    JSONObject today = obj.getJSONObject("today");
                    String city = today.getString("city");
                    String state = today.getString("state");
                    String currentTemper = String.valueOf(today.getInt("temperature"));
                    String currweather = today.getString("temperatureDesc");
                    String highTemper = String.valueOf(today.getInt("highTemperature"));
                    String lowTemper = String.valueOf(today.getInt("lowTemperature"));
                    String iconUrl = today.getString("iconLink");
                    JSONArray dailyArr = obj.getJSONArray("daily");
                    JSONObject next1day = dailyArr.getJSONObject(1);
                    String next1Temper = String.valueOf(today.getInt("comfort"));
                    String next1highTemper = next1day.getString("weekday");
                    JSONObject next2day = dailyArr.getJSONObject(2);
                    String next2Temper = String.valueOf(today.getInt("comfort"));
                    String next2highTemper = next2day.getString("weekday");
                    JSONObject next3day = dailyArr.getJSONObject(3);
                    String next3Temper = String.valueOf(today.getInt("comfort"));
                    String next3highTemper = next3day.getString("weekday");

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(img, null);

                    String content = "city: " + city + "\nstate: " + state + "\ncurrentTemper: " + currentTemper + "\ncurrweather: " + currweather + "\n highTemper: " + highTemper +
                            "\n lowTemper:" + lowTemper + "\n tomorrow: " + next1Temper + ", " + next1highTemper + "\n nextTomorrow:" +
                            next2Temper + ", " + next2highTemper + "\n nextThe day After tomorrow: " + next3Temper + ", " +
                            next3highTemper;

                    String UserCity = city;
                    String UserState = state;
                    String HighTemp = highTemper;
                    String LowTemp = lowTemper;
                    String CurrTemp = currentTemper;
                    String CurrWeather = currweather;

                    tv_city.setText(UserCity+", ");
                    tv_state.setText(UserState);
                    tv_highTempVal.setText(HighTemp);
                    tv_lowTempVal.setText(LowTemp);
                    tv_currTemp.setText(CurrTemp);
                    tv_currWeather.setText(CurrWeather);


                    contentTV.setText(content);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEventInternetError(Exception e) {
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEventServerError(Exception e) {
                dialog.dismiss();
                Toast.makeText(DashboardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Button click to navigate to LocationUpdateActivity to enter the new ZipCode
    public void onClickUpdate(View view) {
        startActivityForResult(new Intent(DashboardActivity.this, LocationUpdateActivity.class), 1002);
    }

    //Checking the repsonse from the API call
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1002) {
                Log.e("Success","Success");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //To display the details after updating the zipcode in the LocationUpdateActivity
    private void update_data(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONObject today = obj.getJSONObject("today");
            String city = today.getString("city");
            String state = today.getString("state");
            String currentTemper = today.getString("temperature");
            String currweather = today.getString("temperatureDesc");
            String highTemper = today.getString("highTemperature");
            String lowTemper = today.getString("lowTemperature");
            String iconUrl = today.getString("iconLink");
            JSONArray dailyArr = obj.getJSONArray("daily");
            JSONObject next1day = dailyArr.getJSONObject(1);
            String next1Temper = next1day.getString("comfort");
            String next1highTemper = next1day.getString("weekday");
            JSONObject next2day = dailyArr.getJSONObject(2);
            String next2Temper = next2day.getString("comfort");
            String next2highTemper = next2day.getString("weekday");
            JSONObject next3day = dailyArr.getJSONObject(3);
            String next3Temper = next3day.getString("comfort");
            String next3highTemper = next3day.getString("weekday");

            Picasso.with(DashboardActivity.this).load(iconUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_heart)
                    .error(R.drawable.ic_heart)
                    .into(img, null);

            String content = "city: " + city + "\nstate: " + state + "\ncurrentTemper: " + currentTemper + "\ncurrweather: " + currweather + "\n highTemper: " + highTemper +
                    "\n lowTemper:" + lowTemper + "\n tomorrow: " + next1Temper + ", " + next1highTemper + "\n nextTomorrow:" +
                    next2Temper + ", " + next2highTemper + "\n nextThe day After tomorrow: " + next3Temper + ", " +
                    next3highTemper;

            String UserCity = city;
            String UserState = state;
            String HighTemp = highTemper;
            String LowTemp = lowTemper;
            String CurrTemp = currentTemper;
            String CurrWeather = currweather;

            tv_city.setText(UserCity);
            tv_state.setText(UserState);
            tv_highTempVal.setText(HighTemp);
            tv_lowTempVal.setText(LowTemp);
            tv_currTemp.setText(CurrTemp);
            tv_currWeather.setText(CurrWeather);


            contentTV.setText(content);

        } catch (Exception ex) {
            Log.e("update_data", "update_data");
        }
    }
}
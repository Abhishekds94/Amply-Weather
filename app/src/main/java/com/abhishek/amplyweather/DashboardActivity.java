package com.abhishek.amplyweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private ImageView img;

    private TextView tv_city;
    private TextView tv_state;
    private TextView tv_highTempVal;
    private TextView tv_lowTempVal;
    private TextView tv_currTemp;
    private TextView tv_currWeather;

    private TextView tv_day1;
    private TextView tv_day1Temp;
    private ImageView iv_day1Icon;
    private TextView tv_day1Weather;
    private TextView tv_day1High;
    private TextView tv_day1Low;

    private TextView tv_day2;
    private TextView tv_day2Temp;
    private ImageView iv_day2Icon;
    private TextView tv_day2Weather;
    private TextView tv_day2High;
    private TextView tv_day2Low;

    private TextView tv_day3;
    private TextView tv_day3Temp;
    private ImageView iv_day3Icon;
    private TextView tv_day3Weather;
    private TextView tv_day3High;
    private TextView tv_day3Low;

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

        checkInternetAvailibility();

        //Initialize all the required views to display the data
        img = findViewById(R.id.img_icon);

        tv_city = findViewById(R.id.tv_city);
        tv_state = findViewById(R.id.tv_state);
        tv_highTempVal = findViewById(R.id.tv_highTempVal);
        tv_lowTempVal = findViewById(R.id.tv_lowTempVal);
        tv_currTemp = findViewById(R.id.tv_currTemp);
        tv_currWeather = findViewById(R.id.tv_currWeather);

        tv_day1 = findViewById(R.id.tv_day1);
        tv_day1Temp = findViewById(R.id.tv_day1Temp);
        iv_day1Icon = findViewById(R.id.iv_day1Icon);
        tv_day1Weather = findViewById(R.id.tv_day1Weather);
        tv_day1High = findViewById(R.id.tv_day1High);
        tv_day1Low = findViewById(R.id.tv_day1Low);

        tv_day2 = findViewById(R.id.tv_day2);
        tv_day2Temp = findViewById(R.id.tv_day2Temp);
        iv_day2Icon = findViewById(R.id.iv_day2Icon);
        tv_day2Weather = findViewById(R.id.tv_day2Weather);
        tv_day2High = findViewById(R.id.tv_day2High);
        tv_day2Low = findViewById(R.id.tv_day2Low);

        tv_day3 = findViewById(R.id.tv_day3);
        tv_day3Temp = findViewById(R.id.tv_day3Temp);
        iv_day3Icon = findViewById(R.id.iv_day3Icon);
        tv_day3Weather = findViewById(R.id.tv_day3Weather);
        tv_day3High = findViewById(R.id.tv_day3High);
        tv_day3Low = findViewById(R.id.tv_day3Low);

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
                    String next1 = next1day.getString("weekday");
                    String next1Temper = String.valueOf(next1day.getInt("comfort"));
                    String next1TemperDesc = next1day.getString("temperatureDesc");
                    String next1highTemper = String.valueOf(next1day.getInt("highTemperature"));
                    String next1lowTemper = String.valueOf(next1day.getInt("lowTemperature"));
                    String next1iconUrl = next1day.getString("iconLink");

                    JSONObject next2day = dailyArr.getJSONObject(2);
                    String next2 = next2day.getString("weekday");
                    String next2Temper = String.valueOf(next2day.getInt("comfort"));
                    String next2TemperDesc = next2day.getString("temperatureDesc");
                    String next2highTemper = String.valueOf(next2day.getInt("highTemperature"));
                    String next2lowTemper = String.valueOf(next2day.getInt("lowTemperature"));
                    String next2iconUrl = next2day.getString("iconLink");

                    JSONObject next3day = dailyArr.getJSONObject(3);
                    String next3 = next3day.getString("weekday");
                    String next3Temper = String.valueOf(next3day.getInt("comfort"));
                    String next3TemperDesc = next3day.getString("temperatureDesc");
                    String next3highTemper = String.valueOf(next3day.getInt("highTemperature"));
                    String next3lowTemper = String.valueOf(next3day.getInt("lowTemperature"));
                    String next3iconUrl = next3day.getString("iconLink");

                    //Log.e("Next Days","Next days"+next1TemperVal);

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(img, null);

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(next1iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(iv_day1Icon, null);

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(next2iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(iv_day2Icon, null);

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(next3iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(iv_day3Icon, null);

                    tv_city.setText(city+", ");
                    tv_state.setText(state);
                    tv_highTempVal.setText(highTemper+ " \u00B0");
                    tv_lowTempVal.setText(lowTemper+ " \u00B0");
                    tv_currTemp.setText(currentTemper);
                    tv_currWeather.setText(currweather);

                    tv_day1.setText(next1);
                    tv_day1Temp.setText(next1Temper+ " \u00B0");
                    tv_day1Weather.setText(next1TemperDesc);
                    tv_day1High.setText(next1highTemper+" \u00B0");
                    tv_day1Low.setText(next1lowTemper+" \u00B0");

                    tv_day2.setText(next2);
                    tv_day2Temp.setText(next2Temper+ " \u00B0");
                    tv_day2Weather.setText(next2TemperDesc);
                    tv_day2High.setText(next2highTemper+" \u00B0");
                    tv_day2Low.setText(next2lowTemper+" \u00B0");

                    tv_day3.setText(next3);
                    tv_day3Temp.setText(next3Temper+ " \u00B0");
                    tv_day3Weather.setText(next3TemperDesc);
                    tv_day3High.setText(next3highTemper+" \u00B0");
                    tv_day3Low.setText(next3lowTemper+" \u00B0");

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
                update_data(data.getStringExtra("json"));
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

            String currentTemper = String.valueOf(today.getInt("temperature"));
            String currweather = today.getString("temperatureDesc");

            String highTemper = String.valueOf(today.getInt("highTemperature"));
            String lowTemper = String.valueOf(today.getInt("lowTemperature"));

            String iconUrl = today.getString("iconLink");

            JSONArray dailyArr = obj.getJSONArray("daily");

            JSONObject next1day = dailyArr.getJSONObject(1);
            String next1 = next1day.getString("weekday");
            String next1Temper = String.valueOf(next1day.getInt("comfort"));
            String next1TemperDesc = next1day.getString("temperatureDesc");
            String next1highTemper = String.valueOf(next1day.getInt("highTemperature"));
            String next1lowTemper = String.valueOf(next1day.getInt("lowTemperature"));
            String next1iconUrl = next1day.getString("iconLink");

            JSONObject next2day = dailyArr.getJSONObject(2);
            String next2 = next2day.getString("weekday");
            String next2Temper = String.valueOf(next2day.getInt("comfort"));
            String next2TemperDesc = next2day.getString("temperatureDesc");
            String next2highTemper = String.valueOf(next2day.getInt("highTemperature"));
            String next2lowTemper = String.valueOf(next2day.getInt("lowTemperature"));
            String next2iconUrl = next2day.getString("iconLink");

            JSONObject next3day = dailyArr.getJSONObject(3);
            String next3 = next3day.getString("weekday");
            String next3Temper = String.valueOf(next3day.getInt("comfort"));
            String next3TemperDesc = next3day.getString("temperatureDesc");
            String next3highTemper = String.valueOf(next3day.getInt("highTemperature"));
            String next3lowTemper = String.valueOf(next3day.getInt("lowTemperature"));
            String next3iconUrl = next3day.getString("iconLink");


            //Picasso to render the weather icons and replace the default image
            Picasso.with(DashboardActivity.this).load(iconUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_heart)
                    .error(R.drawable.ic_heart)
                    .into(img, null);

            //Picasso to render the weather icons and replace the default image
            Picasso.with(DashboardActivity.this).load(next1iconUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_heart)
                    .error(R.drawable.ic_heart)
                    .into(iv_day1Icon, null);

            //Picasso to render the weather icons and replace the default image
            Picasso.with(DashboardActivity.this).load(next2iconUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_heart)
                    .error(R.drawable.ic_heart)
                    .into(iv_day2Icon, null);

            //Picasso to render the weather icons and replace the default image
            Picasso.with(DashboardActivity.this).load(next3iconUrl).fit().centerCrop()
                    .placeholder(R.drawable.ic_heart)
                    .error(R.drawable.ic_heart)
                    .into(iv_day3Icon, null);

            tv_city.setText(city+", ");
            tv_state.setText(state);
            tv_highTempVal.setText(highTemper+ " \u00B0");
            tv_lowTempVal.setText(lowTemper+ " \u00B0");
            tv_currTemp.setText(currentTemper);
            tv_currWeather.setText(currweather);

            tv_day1.setText(next1);
            tv_day1Temp.setText(next1Temper+ " \u00B0");
            tv_day1Weather.setText(next1TemperDesc);
            tv_day1High.setText(next1highTemper+" \u00B0");
            tv_day1Low.setText(next1lowTemper+" \u00B0");

            tv_day2.setText(next2);
            tv_day2Temp.setText(next2Temper+ " \u00B0");
            tv_day2Weather.setText(next2TemperDesc);
            tv_day2High.setText(next2highTemper+" \u00B0");
            tv_day2Low.setText(next2lowTemper+" \u00B0");

            tv_day3.setText(next3);
            tv_day3Temp.setText(next3Temper+ " \u00B0");
            tv_day3Weather.setText(next3TemperDesc);
            tv_day3High.setText(next3highTemper+" \u00B0");
            tv_day3Low.setText(next3lowTemper+" \u00B0");


        } catch (Exception ex) {
            Log.e("update_data", "update_data");
        }
    }

    //To check active internet connection
    public void checkInternetAvailibility() {
        if (isInternetAvailable()) {
            Log.e("Yes","Yes");
        } else {
            Intent intent = new Intent(DashboardActivity.this, NoInternet.class);
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

}
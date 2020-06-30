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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private ImageView img;
    private TextView contentTV;

    private AdView mAdView;

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

        //Call the method to initialize data
        initData();

        //For Banner Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
/*        mAdView = findViewById(R.id.adView);

        //Request for an ad to build
        AdRequest adRequest = new AdRequest.Builder().build();
        //Set AdSize and UnitID
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.loadAd(adRequest);*/

        View adContainer = findViewById(R.id.adMobView);

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        ((FrameLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

                    //Picasso to render the weather icons and replace the default image
                    Picasso.with(DashboardActivity.this).load(iconUrl).fit().centerCrop()
                            .placeholder(R.drawable.ic_heart)
                            .error(R.drawable.ic_heart)
                            .into(img, null);

                    String content = "city: " + city + "\nstate: " + state + "\ncurrentTemper: " + currentTemper + "\ncurrweather: " + currweather + "\n highTemper: " + highTemper +
                            "\n lowTemper:" + lowTemper + "\n tomorrow: " + next1Temper + ", " + next1highTemper + "\n nextTomorrow:" +
                            next2Temper + ", " + next2highTemper + "\n nextThe day After tomorrow: " + next3Temper + ", " +
                            next3highTemper;

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

            contentTV.setText(content);
        } catch (Exception ex) {
            Log.e("update_data", "update_data");
        }
    }
}
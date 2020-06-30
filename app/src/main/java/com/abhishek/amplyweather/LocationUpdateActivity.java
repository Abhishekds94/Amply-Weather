package com.abhishek.amplyweather;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhishek.amplyweather.Network.APIManager;
import com.abhishek.amplyweather.Network.App;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class LocationUpdateActivity extends AppCompatActivity {

    private EditText et_zip;
    private Button btnSubmit;

    private AdView mAdView;

    private RelativeLayout rl_locUpdate;
    private TextView tv_loc_card_text;
    private Button bt_updateLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_location_update);
        et_zip = findViewById(R.id.et_zip);

        //To make the activity full screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Toolbar :: Transparent
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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

        rl_locUpdate = (RelativeLayout) findViewById(R.id.rl_locUpdate);
        tv_loc_card_text = (TextView) findViewById(R.id.tv_loc_card_text);
        bt_updateLoc = (Button) findViewById(R.id.bt_updateLoc);


        //Check if the time is in between 6.00 hours and 14.00 hours and set morning BG
        if (timeOfDay >= 6 && timeOfDay < 14) {
            rl_locUpdate.setBackground(ContextCompat.getDrawable(this, R.drawable.loc_bg_morning));
            tv_loc_card_text.setBackgroundResource(R.color.morning_loc);
            bt_updateLoc.setBackgroundResource(R.drawable.btnbg_morning);
            //Check if the time is in between 14.00 hours and 19.00 hours and set noon BG
        } else if (timeOfDay >= 14 && timeOfDay < 19) {
            rl_locUpdate.setBackground(ContextCompat.getDrawable(this, R.drawable.loc_bg_noon));
            tv_loc_card_text.setBackgroundResource(R.color.noon_loc);
            bt_updateLoc.setBackgroundResource(R.drawable.btnbg_noon);
            //Check if the time is in between 19.00 hours and 6.00 hours and set night BG
        } else if (timeOfDay >= 19 && timeOfDay < 6) {
            rl_locUpdate.setBackground(ContextCompat.getDrawable(this, R.drawable.loc_bg_night));
            tv_loc_card_text.setBackgroundResource(R.color.night_loc);
            bt_updateLoc.setBackgroundResource(R.drawable.btnbg_night);
        }

    }

    public void onClickUpdate(View view) {
        String json = "{\"zipcode\":\"20201\"}";
        String login_json_obj_req = "login_json_obj_req";

        String url = APIManager.AMPLY_WEATHER_URL;
        if (TextUtils.isEmpty(et_zip.getText().toString())) {
            et_zip.setError("Please enter zip code");
            return;
        }

//            show_dialog("Please Wait...");
        try {

            JsonObjectRequest postRequest = new JsonObjectRequest(url,
                    new JSONObject().put("zipcode", et_zip.getText().toString())
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Response", response.toString());
                    try {
                        Intent intent = new Intent();
                        intent.putExtra("json", response.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (Exception e) {
                        Log.e("volley response", "success");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("e", "volley error");
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            App.getInstance().addToRequestQueue(postRequest, login_json_obj_req);
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}

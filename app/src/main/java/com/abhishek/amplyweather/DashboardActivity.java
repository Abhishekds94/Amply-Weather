package com.abhishek.amplyweather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.abhishek.amplyweather.Network.API;
import com.abhishek.amplyweather.Network.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To Hide the title bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        setContentView(R.layout.activity_dashboard);

        URL weatherURL = API.buildURLForWeather();
        new FetchWeatherDetails().execute(weatherURL);
        Log.e(TAG,"WeatherURL:"+weatherURL);

    }

    private class FetchWeatherDetails extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherURL = urls[0];
            String weatherSearchResults = null;

            try {
                weatherSearchResults = API.getResponseFromHttpUrl(weatherURL);
            } catch (IOException e){
                e.printStackTrace();
            }
            Log.e("test","SearchResults"+weatherSearchResults);
            return weatherSearchResults;
        }

        @Override
        protected void onPostExecute(String weatherSearchResults) {
            
            if (weatherSearchResults != null && !weatherSearchResults.equals("")){
                weatherArrayList = parseJSON(weatherSearchResults);
            }
            
            super.onPostExecute(weatherSearchResults);
        }

        private ArrayList<Weather> parseJSON(String weatherSearchResults) {
            Log.e("grere","here1");
            if (weatherArrayList != null){
                Log.e("grere","here2");
                weatherArrayList.clear();
            }

            if (weatherSearchResults != null){
                try {
                    JSONObject rootObject = new JSONObject(weatherSearchResults);
                    JSONArray results = rootObject.getJSONArray("today");
                    Log.e("grere","here3");

                    for (int i=0; i<results.length();i++){
                        Weather weather = new Weather();
                        JSONObject resultsObj = results.getJSONObject(i);
                        String currTemp = resultsObj.getString("comfort");
                        weather.setComfort(currTemp);

                        Log.e("Curr temp","Curr Tempr-"+currTemp);

                    }

                    Log.e("grere","list"+weatherArrayList);
                    return weatherArrayList;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
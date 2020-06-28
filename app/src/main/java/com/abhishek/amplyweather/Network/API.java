package com.abhishek.amplyweather.Network;

import android.net.Uri;
import android.util.Log;

import com.abhishek.amplyweather.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class API {

    private static final String TAG = "API";

    private final static String AMPLY_WEATHER_URL = "https://j9l4zglte4.execute-api.us-east-1.amazonaws.com/api/ctl/weather";

    public static URL buildURLForWeather(){
        Uri buildUri = Uri.parse(AMPLY_WEATHER_URL).buildUpon()
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.e(TAG, "BuildURL: URL -"+url);
        return url;
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}

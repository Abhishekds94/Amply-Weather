package com.abhishek.amplyweather.Network;

import com.abhishek.amplyweather.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIManager {
    public static String AMPLY_WEATHER_URL = "https://j9l4zglte4.execute-api.us-east-1.amazonaws.com/api/ctl/weather";
    public static final MediaType FORM = MediaType.parse("multipart/form-data");
//    public static final MediaType JSON
//            = MediaType.parse("application/x-www-form-urlencoded");
public static final MediaType JSON
        = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public enum APIMethod {
        GET, POST
    }

    public static void onAPIConnectionResponse (String url
            , Map<String, String> params, Map<String, String> headers
            , APIMethod method
            , final APIManagerCallback apiResponse)
    {
        if (method == APIMethod.POST) {
            OkHttpUtils.post().url(url)
                    .headers(headers)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            apiResponse.onEventInternetError(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                apiResponse.onEventCallBack(obj);
                                String temperatureDesc = obj.getJSONObject("today").getString("temperatureDesc");
                                String comfort = obj.getJSONObject("today").getString("comfort");


                            } catch (JSONException e) {
                                apiResponse.onEventServerError(e);
                                e.printStackTrace();
                            }
                        }
                    });


        } else {
            OkHttpUtils.get().url(url)
                    .headers(headers)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            apiResponse.onEventInternetError(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                apiResponse.onEventCallBack(obj);
                            } catch (JSONException e) {
                                apiResponse.onEventServerError(e);
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public interface APIManagerCallback {
        void onEventCallBack(JSONObject obj);
        void onEventInternetError(Exception e);
        void onEventServerError(Exception e);
    }

}

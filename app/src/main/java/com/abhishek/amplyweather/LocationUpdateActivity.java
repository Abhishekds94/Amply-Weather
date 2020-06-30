package com.abhishek.amplyweather;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.amplyweather.Network.APIManager;
import com.abhishek.amplyweather.Network.App;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class LocationUpdateActivity extends AppCompatActivity {

    private EditText et_zip;
    private Button btnSubmit;
    private ImageView imgIcon;
    private TextView txtContetn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_update);
        txtContetn = findViewById(R.id.textview1);
        et_zip = findViewById(R.id.et_zip);
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
//                        JSONObject today = response.getJSONObject("today");
//                        txtContetn.setText(today.getString("city"));
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
//                    params.put("user_name", user_name);
//                    params.put("password", password);
//                    params.put("zipcode", "20201");
                    return params;
                }
            };
            App.getInstance().addToRequestQueue(postRequest, login_json_obj_req);
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }


//        RequestBody body = RequestBody.create(JSON, json);
//        okhttp3.Request request = new okhttp3.Request.Builder()
//                .addHeader("Content-Type", "application/json")
//                .url(APIManager.AMPLY_WEATHER_URL)
//                .post(body)
//                .build();
//        new OkHttpClient().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Toast.makeText(LocationUpdateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) {
//                String message = response.message();
//                try {
//                    JSONObject jsonMessage = new JSONObject(message);
//                    JSONObject today = jsonMessage.getJSONObject("today");
//                    txtContetn.setText(today.getString("city"));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }
}

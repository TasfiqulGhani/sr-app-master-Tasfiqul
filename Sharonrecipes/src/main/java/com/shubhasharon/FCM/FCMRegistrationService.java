package com.shubhasharon.FCM;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shubhasharon.R;


/**
 * Created by wail babou on 2016-09-10.
 */
public class FCMRegistrationService extends IntentService {
    SharedPreferences preferences;

    public FCMRegistrationService() {
        super("FCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = FirebaseInstanceId.getInstance().getToken();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (intent.getExtras() != null) {
            boolean refreshed = intent.getExtras().getBoolean("refreshed");
            if (refreshed) preferences.edit().putBoolean("token_sent", false).apply();
        }
        if (!preferences.getBoolean("token_sent", false))
            sendTokenToServer(token);

    }

    private void sendTokenToServer(final String token) {

        String link = getString(R.string.linkPushNotif)+"wp-json/apnwp/register?os_type=android"+
                "&device_token="+token;
        final StringRequest request = new StringRequest(Request.Method.GET
                , link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("fcm fcm",response);
                    if(response
                            .equals("{\"isError\":\"false\",\"error\":\"200\",\"SuccessMessage\":\"User successfully added in wpuser table\"}")){

                        preferences.edit().putBoolean("token_sent", true).apply();
                        Log.e("Registration Service", "Response : Send Token Success");

                    } else {
                        preferences.edit().putBoolean("token_sent", false).apply();
                        Log.e("Registration Service", "Response : Send Token Failed");
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                preferences.edit().putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed"+ error);


            }
        });
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}
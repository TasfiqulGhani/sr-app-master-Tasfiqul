package com.shubhasharon.FCM;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by wail babou on 2016-09-10.
 */
public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, FCMRegistrationService.class);
        intent.putExtra("refreshed", true);
        startService(intent);
    }
}
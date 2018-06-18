package com.shubhasharon.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.shubhasharon.R;
import com.shubhasharon.utils.MyData;

import wail.splacher.com.splasher.lib.SplasherActivity;
import wail.splacher.com.splasher.models.SplasherConfig;
import wail.splacher.com.splasher.utils.Const;

/**
 * Created by wail babou on 2016-12-24.
 */

public class Splash extends SplasherActivity {

    @Override
    public void initSplasher(SplasherConfig config) {
        config.setReveal_start(Const.START_TOP_LEFT) // anitmation type ..
                //---------------
                .setAnimationDuration(2000) // Reveal animation duration ..
                //---------------
                .setLogo(R.drawable.splash_logo) // logo src..
                .setLogo_animation(Techniques.BounceIn) // logo animation ..
                .setAnimationLogoDuration(800) // logo animation duration ..
                .setLogoWidth(600) // logo width ..
                //---------------
                .setTitle(getString(R.string.app_name)) // title ..
                .setTitleColor(Color.parseColor("#ffffff")) // title color ..
                .setTitleAnimation(Techniques.Bounce) // title animation ( from Android View Animations ) ..
                .setTitleSize(24) // title text size ..
                //---------------
                .setSubtitle(getString(R.string.subtitle)) // subtitle
                .setSubtitleColor(Color.parseColor("#ffffff")) // subtitle color
                .setSubtitleAnimation(Techniques.FadeIn) // subtitle animation (from Android View Animations) ..
                .setSubtitleSize(16)
                .setTitleTypeFace(Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf"));
    }

    @Override
    public void onSplasherFinished() {
        // This method will be executed once the timer is over
        // Start your app main activity
        MyData.addCategories();
        //sendTokenToServer(FirebaseInstanceId.getInstance().getToken());
        Intent i = new Intent(Splash.this, MainActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

}

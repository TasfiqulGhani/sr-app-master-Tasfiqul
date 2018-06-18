package com.shubhasharon;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Ads {
    InterstitialAd mInterstitialAd;

    public Ads(Context c){
        mInterstitialAd = new InterstitialAd(c);
        mInterstitialAd.setAdUnitId("ca-app-pub-8533004717624284/3062405206");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



    }






}

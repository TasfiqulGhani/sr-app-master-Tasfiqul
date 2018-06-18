package com.shubhasharon;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tasfiqul Ghani on 6/5/2017.
 */

public class Saver {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public Saver(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setRated(boolean isFirstTime) {
        editor.putBoolean("isRated", isFirstTime);
        editor.commit();
    }

    public boolean isRated() {
        return pref.getBoolean("isRated", true);
    }

    public void setForToday(boolean isFirstTime) {
        editor.putBoolean("isForToday", isFirstTime);
        editor.commit();
    }

    public boolean isForToday() {
        return pref.getBoolean("isForToday", true);
    }

}
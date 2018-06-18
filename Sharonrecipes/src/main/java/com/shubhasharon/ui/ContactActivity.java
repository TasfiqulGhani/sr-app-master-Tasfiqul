package com.shubhasharon.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.R;

/**
 * Created by wail babou on 2016-12-24.
 */

public class ContactActivity extends AppCompatActivity{
    ImageView fcb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        fcb= (ImageView) findViewById(R.id.fcb);
        new NoInternetChecker(ContactActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facebookUrl = getString(R.string.Blog_FB_page);
                try {
                    int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}

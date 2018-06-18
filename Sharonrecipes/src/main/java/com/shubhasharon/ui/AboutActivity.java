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

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by wail babou on 2016-12-24.
 */

public class AboutActivity extends AppCompatActivity {
    ImageView fcb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        new NoInternetChecker(AboutActivity.this);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.applogot)
                .setDescription("Shubha sharon is a house wife, cooks amazing food and teaches How to make them easily. Her motto is Learn any recipe under 5 minutes.")
                .addItem(new Element().setTitle("version 1.0"))
                .addGroup("Connect with us")
                .addEmail("shubhasharon777@gmail.com")
                .addWebsite("http://shubhasharon.com/")
                .addFacebook("cookwithshubhasharon")
                .addTwitter("shubhasharon")
                .addYoutube("UCcEXr6ZzppFU1jEbX42QP1A")
                .addPlayStore("com.shubhasharon")
                .addInstagram("shubhasharon")
               // .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
       /* fcb= (ImageView) findViewById(R.id.fcb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facebookUrl = getString(R.string.developer_FB_page);
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
        });*/
    }

   /* private Element createCopyright() {
        Element copyright = new Element();
        String copyrightString = String.format("Copyright %d by Shubha sharon recipes", Calendar.getInstance().get(Calendar.YEAR));
                copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}

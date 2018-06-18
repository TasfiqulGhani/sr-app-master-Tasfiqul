package com.shubhasharon.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shubhasharon.Ads;
import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.Saver;
import com.shubhasharon.ui.Categories.GetCategoriesActivity;
import com.shubhasharon.ui.Home.OpenArticle;
import com.shubhasharon.utils.CustomTypefaceSpan;
import com.shubhasharon.FCM.FCMTokenRefreshListenerService;
import com.shubhasharon.FCM.MyFCMService;
import com.shubhasharon.R;
import com.shubhasharon.ui.Home.HomeFragment;
import com.shubhasharon.utils.MyData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Toolbar toolbar;
    int position;
    private AdView mAdView;
    SharedPreferences preferences;


    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        new NoInternetChecker(MainActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        startService(new Intent(this, MyFCMService.class));
        startService(new Intent(this, FCMTokenRefreshListenerService.class));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //......... start coding ......
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();


        mAdView.loadAd(adRequest);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        /*********view pager & tabLayout ********/
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();

        /*******************/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.position=position;
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        MyData.categories.get(position).getColor()));

                tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                        MyData.categories.get(position).getColor()));
                changeStatuColor(MyData.categories.get(position).getColor());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (!preferences.getBoolean("token_sent", false))
            sendTokenToServer(FirebaseInstanceId.getInstance().getToken());


        showRating();
    }

    private void changeTabsFont() {
        Typeface stc = Typeface.createFromAsset(getAssets(),
                "arabic.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(stc);
                }
            }
        }
    }
    public void changeStatuColor(int color){
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(),color));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0;i<MyData.categories.size();i++){
            adapter.addFrag(new HomeFragment(i),MyData.categories.get(i).getCategory_title());
        }
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
          id = item.getItemId();

            if (id == R.id.nav_home) {

            } else if (id == R.id.nav_about) {
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
            } else if (id == R.id.nav_rate) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            }else if (id == R.id.nav_manage) {
                shareTextUrl();
            }else if(id == R.id.nav_fav){
                startActivity(new Intent(MainActivity.this,FavActivity.class));
            }else if(id == R.id.nav_categories){
                startActivity(new Intent(MainActivity.this,GetCategoriesActivity.class));
            }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void shareTextUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"To Learn any recipe under just 5 mins download Sharon's recipes App: "+getResources().getString(R.string.playStoreLink));
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
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

    public void showRating(){
        Saver sc=new Saver(MainActivity.this);
       if(sc.isForToday()){
           if(sc.isRated()){

               Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
               int d=calendar.get(Calendar.DATE);
               Log.e("TTX",""+d);
               if(d%3==0){
                   Log.e("TTX","x"+d);
                   AlertDialog.Builder builder;
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                   } else {
                       builder = new AlertDialog.Builder(MainActivity.this);
                   }
                   builder.setTitle("Rate us !")
                           .setMessage("Do you want to rate us ?")
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));

                               }
                           })
                           .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int which) {
                                   Saver sc=new Saver(MainActivity.this);
                                   sc.setForToday(false);
                               }
                           }).setNeutralButton("Never", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           Saver sc=new Saver(MainActivity.this);
                           sc.setRated(false);
                       }
                   })
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .show();
               }else {

                   sc.setRated(true);
               }

           }
       }

    }
}

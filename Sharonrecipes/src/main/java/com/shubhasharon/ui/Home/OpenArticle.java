package com.shubhasharon.ui.Home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.shubhasharon.Ads;
import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.R;
import com.shubhasharon.ui.Comments.CommentsActivity;
import com.shubhasharon.ui.MainActivity;
import com.shubhasharon.utils.Css;

import java.util.Random;

/**
 * Created by wail babou on 2016-12-24.
 */

public class OpenArticle  extends AppCompatActivity{
    TextView title;
    WebView web;
    ImageView image;
    FloatingActionButton share,pc,comment;
    Intent data;
    Toolbar toolbar;
    Dialog builder;
    ImageView imageView;
    com.victor.loading.rotate.RotateLoading loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_article);
        loader=(com.victor.loading.rotate.RotateLoading)findViewById(R.id.rotateloading);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title= (TextView) findViewById(R.id.title);
        web= (WebView) findViewById(R.id.web);
        image= findViewById(R.id.imageheader);
        data = getIntent();
        new NoInternetChecker(OpenArticle.this);



        Glide
                .with(this)
                .load(data.getStringExtra("logo"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(image);
        title.setText(data.getStringExtra("title"));
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new Browser());
        web.setWebChromeClient(new MyWebClient());
        web.loadDataWithBaseURL("", Css.css+" "
                +data.getStringExtra("content")+" "+
                Css.footer, "text/html", "UTF-8", "");
        share= (FloatingActionButton) findViewById(R.id.fab1);
        pc= (FloatingActionButton) findViewById(R.id.fab2);
        comment=findViewById(R.id.fab3);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });
        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = data.getStringExtra("ArticleUrl");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(OpenArticle.this, CommentsActivity.class);
                ii.putExtra("id",getIntent().getIntExtra("id",0)+"");
                startActivity(ii);
            }
        });

    }
    private void shareTextUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String txt = "Recipe title :"+title.getText().toString()+" \n\n" +
                "Recipe link: "+data.getStringExtra("ArticleUrl")+" \n\n" +
                "Download Sharon's recipes app, to learn any recipe under just 5 mins \n\n" +
                "https://play.google.com/store/apps/details?id="+getPackageName();
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                txt);
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        super.onPause();
        web.onPause();
    }

    class Browser
            extends WebViewClient
    {
        Browser() {

        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }




    }

    public class MyWebClient
            extends WebChromeClient
    {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;



        public MyWebClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (getApplicationContext() == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.e("LoadingT",""+newProgress);
            if (newProgress == 100) {
                loader.stop();
                loader.setVisibility(View.GONE);

            } else {

                loader.start();

            }
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

}

package com.shubhasharon.ui.Home;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.shubhasharon.modals.Article;
import com.shubhasharon.R;
import com.shubhasharon.SQLITE.DBHelper;
import com.shubhasharon.ui.AboutActivity;
import com.shubhasharon.ui.Categories.GetCategoriesActivity;
import com.shubhasharon.ui.FavActivity;
import com.shubhasharon.ui.MainActivity;
import com.shubhasharon.utils.MyData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wail babou on 2016-12-24.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Article> datasource = new ArrayList<>();
    Context context;
    ViewHolder viewHolder;
    private String[] bgColors;
    int pos;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_OPENCAT = 2;
    int position;
    private RewardedVideoAd mRewardedVideoAd;

    InterstitialAd mInterstitialAd;
    public HomeAdapter(Context context, ArrayList<Article> datasource, int position) {
        this.context = context;
        this.datasource = datasource;
        pos = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.custom, parent, false);
            return new ViewHolder(view);
        }else if(viewType == VIEW_TYPE_OPENCAT){
            View view = inflater.inflate(R.layout.custom2, parent, false);
            return new ViewHolder(view);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            View view = inflater.inflate(R.layout.loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (datasource.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if(datasource.get(position).isCategoryOpened){
            return VIEW_TYPE_OPENCAT;
        }else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Typeface caviar = Typeface.createFromAsset(context.getAssets(),
                "CaviarDreams.ttf");
        Typeface stc = Typeface.createFromAsset(context.getAssets(),
                "stc.otf");

        if (holder instanceof ViewHolder) {
            ViewHolder mholder = (ViewHolder) holder;
            mholder.title.setText(Html.fromHtml(datasource.get(position).getTitle()).toString());
            mholder.title.setTypeface(stc);
            mholder.date.setText(datasource.get(position).getDate().substring(0, 10));
            mholder.date.setTypeface(caviar);
            /*mholder.auther.setText(datasource.get(position).getAuther());
            mholder.auther.setTypeface(stc);*/

            try {



                if(datasource.get(position).getSaved() != null){
                    try {
                        mholder.fab.setImageResource(R.drawable.remove);

                    }catch (Exception e){
                    }
                }else {
                    try {
                        mholder.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,
                                MyData.categories.get(pos).getColor())));
                    }catch (Exception e){
                    }
                }


            }catch (Exception e){}
            Glide
                    .with(context)
                    .load(datasource.get(position).getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(mholder.logo);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        if (datasource == null)
            return 0;
        else
            return datasource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView title, auther, date;
        FloatingActionButton fab;

        public ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            //auther = (TextView) itemView.findViewById(R.id.auther);
            date = (TextView) itemView.findViewById(R.id.date);
            fab = (FloatingActionButton) itemView.findViewById(R.id.fab);
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-8533004717624284/3062405206");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {


                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }


                @Override
                public void onAdClosed() {

                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                        Intent ii = new Intent(context, OpenArticle.class);
                        ii.putExtra("id", datasource.get(position).getId());
                        ii.putExtra("content", datasource.get(position).getContent());
                        ii.putExtra("title", datasource.get(position).getTitle());
                        ii.putExtra("title", datasource.get(position).getTitle());
                        ii.putExtra("logo", datasource.get(position).getImg_url());
                        ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                        context.startActivity(ii);
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());

                }
            });
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                    new AdRequest.Builder().build());
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    //  Toast.makeText(RewardedVideoAdActivity.this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                    // rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    // Toast.makeText(RewardedVideoAdActivity.this, "onRewardedVideoAdLeftApplication",
                    //   Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    Intent ii = new Intent(context, OpenArticle.class);
                    ii.putExtra("id", datasource.get(position).getId());
                    ii.putExtra("content", datasource.get(position).getContent());
                    ii.putExtra("title", datasource.get(position).getTitle());
                    ii.putExtra("title", datasource.get(position).getTitle());
                    ii.putExtra("logo", datasource.get(position).getImg_url());
                    ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                    context.startActivity(ii);
                    mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                            new AdRequest.Builder().build());

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int errorCode) {
                    // Toast.makeText(RewardedVideoAdActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
                    mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                            new AdRequest.Builder().build());
                }

                @Override
                public void onRewardedVideoCompleted() {

                }

                @Override
                public void onRewardedVideoAdLoaded() {
                    // Toast.makeText(RewardedVideoAdActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onRewardedVideoAdOpened() {
                    // Toast.makeText(RewardedVideoAdActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoStarted() {
                    //  Toast.makeText(RewardedVideoAdActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      position = getLayoutPosition();
                    Random r = new Random();
                    int i1 = r.nextInt(1 - 0) + 0;
                    if (i1==0){
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }else {
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }

                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    if(datasource.get(position).getSaved() != null){
                        try {
                            new DBHelper(context).deleteArticle(datasource.get(position));
                            datasource.remove(position);
                            HomeAdapter.this.notifyItemRemoved(position);

                        }catch (Exception e){
                        }
                    }else {
                        try {
                            new DBHelper(context).addArticle(datasource.get(position));
                            fab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
                        }catch (Exception e){
                        }
                    }

                }
            });
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      position = getLayoutPosition();
                    Random r = new Random();
                    int i1 = r.nextInt(1 - 0) + 0;
                    if (i1==0){
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }else {
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }

                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Random r = new Random();
                    int i1 = r.nextInt(1 - 0) + 0;
                    if (i1==0){
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }else {
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }

                }
            });
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      position = getLayoutPosition();

                    Random r = new Random();
                    int i1 = r.nextInt(1 - 0) + 0;
                    if (i1==0){
                        if(mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }else {
                        if(mRewardedVideoAd.isLoaded()){
                            mRewardedVideoAd.show();
                        }else {
                            Intent ii = new Intent(context, OpenArticle.class);
                            ii.putExtra("id", datasource.get(position).getId());
                            ii.putExtra("content", datasource.get(position).getContent());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("title", datasource.get(position).getTitle());
                            ii.putExtra("logo", datasource.get(position).getImg_url());
                            ii.putExtra("ArticleUrl", datasource.get(position).getArticle_url());
                            context.startActivity(ii);
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            mRewardedVideoAd.loadAd("ca-app-pub-8533004717624284/3885843732",
                                    new AdRequest.Builder().build());
                        }
                    }




                }
            });

        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_ATOP);
        }
    }
}

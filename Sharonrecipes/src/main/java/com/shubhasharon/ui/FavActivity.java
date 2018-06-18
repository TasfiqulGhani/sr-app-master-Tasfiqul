package com.shubhasharon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;


import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.modals.Article;
import com.shubhasharon.utils.EndlessRecyclerOnScrollListener;
import com.shubhasharon.R;
import com.shubhasharon.ui.Home.HomeAdapter;
import com.shubhasharon.SQLITE.DBHelper;

import java.util.ArrayList;


/**
 * Created by wail babou on 2017-03-14.
 */

public class FavActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    RecyclerView recyclerView;
    HomeAdapter adapter;
    ArrayList<Article> feed = new ArrayList<>();
    LinearLayoutManager layoutManager;
    DBHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        new NoInternetChecker(FavActivity.this);
        getSupportActionBar().setTitle("My Favorites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        helper=new DBHelper(this);
        feed=helper.getSaved();
        adapter = new HomeAdapter(this,feed,0);
        recyclerView.setAdapter(adapter);
        //.............
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try {
                    ArrayList<Article> arr =helper.getSaved(feed.get(feed.size()-1).getBDID());
                    for(int i=0;i<arr.size();i++){
                        feed.add(arr.get(i));
                        adapter.notifyItemInserted(feed.size()-1);
                    }
                    Log.e("more  ...",feed.get(feed.size()-1).getBDID()+"");
                }catch (Exception e){
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

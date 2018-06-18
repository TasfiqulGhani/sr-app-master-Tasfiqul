package com.shubhasharon.ui.Categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.R;
import com.shubhasharon.modals.Article;
import com.shubhasharon.ui.Home.HomeAdapter;
import com.shubhasharon.utils.EndlessRecyclerOnScrollListener;
import com.shubhasharon.utils.MyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OpenCategoryActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    HomeAdapter adapter;
    ArrayList<Article> feed = new ArrayList<>();
    int position = 0;
    int next=2;
    LinearLayoutManager layoutManager;
    com.victor.loading.rotate.RotateLoading loader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        new NoInternetChecker(OpenCategoryActivity.this);
        loader=(com.victor.loading.rotate.RotateLoading)findViewById(R.id.bookloading);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("title"));
        recyclerView = findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        init();
    }
    public void init(){
        getByCategory(getIntent().getStringExtra("id"),1);

        adapter = new HomeAdapter(this, feed, position);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try {
                    getByCategory(getIntent().getStringExtra("id"),next);
                    next++;
                }catch (Exception e){
                }
            }
        });
    }
    public void getByCategory(String category, int page) {
        loader.start();
        String url = getString(R.string.link)+ "get_category_posts/?id="+category+"&page="+page+"&count=4";
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray jArray = jObject.getJSONArray("posts");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        Article n = new Article();
                        n.setArticle_url(json.getString("url"));
                        if(json.getJSONObject("thumbnail_images")!=null){
                            n.setImg_url(json.getJSONObject("thumbnail_images").getJSONObject("full").getString("url"));
                        }
                        n.setId(json.getInt("id"));
                        n.setTitle(json.getString("title"));
                        n.setContent(json.getString("content"));
                        n.setDate(json.getString("date"));
                        JSONObject auth=json.getJSONObject("author");
                        n.setAuther(auth.getString("name"));
                        n.isCategoryOpened=true;
                        feed.add(n);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                loader.stop();
                loader.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("wail error ", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap map = new HashMap();
                //map.put("do", "live");
                return map;
            }
        };

        postrequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postrequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

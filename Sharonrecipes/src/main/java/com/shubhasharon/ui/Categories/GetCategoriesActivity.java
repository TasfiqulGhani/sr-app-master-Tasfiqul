package com.shubhasharon.ui.Categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.shubhasharon.modals.Category;
import com.shubhasharon.ui.Home.HomeAdapter;
import com.shubhasharon.utils.EndlessRecyclerOnScrollListener;
import com.shubhasharon.utils.MyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetCategoriesActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    CatAdapter adapter;
    ArrayList<Category> feed = new ArrayList<>();
    int next=2;
    LinearLayoutManager layoutManager;
    com.victor.loading.rotate.RotateLoading loader;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        initView();
        setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
    }
    public void initView(){

        loader=(com.victor.loading.rotate.RotateLoading)findViewById(R.id.bookloading);
        recyclerView = findViewById(R.id.recy);
        new NoInternetChecker(GetCategoriesActivity.this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );
        recyclerView.addItemDecoration(mDividerItemDecoration);


    }
    public void initData(){
        getByCategory(1);


        adapter = new CatAdapter(this, feed);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getByCategory(int page) {
        loader.start();
        if(next>1&&feed.size()>0){
            feed.add(null);
            adapter.notifyItemInserted(feed.size()-1);
        }
        String url = getString(R.string.link)+ "get_category_index";
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                if(next>1&&feed.size()>0){
                    feed.remove(feed.size()-1);
                    //adapter.notifyItemRemoved(feed.size());
                }
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray jArray = jObject.getJSONArray("categories");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        Category cat = new Category();
                        cat.setCategory_id(json.getInt("id")+"");
                        cat.setCategory_title(json.getString("title"));
                        cat.setCategory_posts_count(json.getInt("post_count")+"");
                        feed.add(cat);
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
}

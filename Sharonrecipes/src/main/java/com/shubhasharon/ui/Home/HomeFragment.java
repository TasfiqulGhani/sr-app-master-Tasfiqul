package com.shubhasharon.ui.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shubhasharon.NoInternetChecker;
import com.shubhasharon.modals.Article;
import com.shubhasharon.modals.ItemComment;
import com.shubhasharon.utils.EndlessRecyclerOnScrollListener;
import com.shubhasharon.R;
import com.shubhasharon.utils.MyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wail babou on 2016-12-23.
 */

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    HomeAdapter adapter;
    ArrayList<Article> feed = new ArrayList<>();
    int position = 0;
    int next=1;
    com.victor.loading.rotate.RotateLoading loader;
    LinearLayoutManager layoutManager;
    private boolean _areLecturesLoaded = false;


    public HomeFragment() {

    }

    @SuppressLint("ValidFragment")
    public HomeFragment(int position) {
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        loader=(com.victor.loading.rotate.RotateLoading)rootView.findViewById(R.id.bookloading);
        recyclerView = rootView.findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getByCategory(String category, int page) {
        final String url = getString(R.string.link)+ "get_category_posts?id="+category+"&page="+page+"&count=4";
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("category",url);
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
                loader.stop();
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
        Volley.newRequestQueue(getContext()).add(postrequest);
    }

    public void getAll(final int page) {
        String url = getString(R.string.link)+ "get_recent_posts?count=4&page="+page+"&count=4";
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

                try {
                    JSONObject jObject = new JSONObject(response);
                    if(jObject.getInt("count")>0){
                        JSONArray jArray = jObject.getJSONArray("posts");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            Article n = new Article();
                            if(json.getJSONObject("thumbnail_images")!=null){
                                n.setImg_url(json.getJSONObject("thumbnail_images").getJSONObject("full").getString("url"));
                            }
                            n.setArticle_url(json.getString("url"));
                            n.setId(json.getInt("id"));
                            n.setTitle(json.getString("title"));
                            n.setContent(json.getString("content"));
                            n.setDate(json.getString("date"));
                            JSONObject auth=json.getJSONObject("author");
                            n.setAuther(auth.getString("name"));
                            feed.add(n);
                        }
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
        Volley.newRequestQueue(getContext()).add(postrequest);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadForFisrtSeen();
                    loader.start();
                    _areLecturesLoaded = true;
                }
            }, 1000);
        }
    }
    public void loadForFisrtSeen(){
        if (position == 0)
            getAll(next);
        else
            getByCategory(MyData.categories.get(position).getCategory_id(),next);

        next++;

        adapter = new HomeAdapter(getContext(), feed, position);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...
                try {
                    if (position == 0){
                        getAll(next);
                    } else
                        getByCategory(MyData.categories.get(position).getCategory_id(),next);
                    next++;
                }catch (Exception e){
                }
            }
        });
    }
}

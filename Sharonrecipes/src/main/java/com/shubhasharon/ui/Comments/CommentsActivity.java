package com.shubhasharon.ui.Comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shubhasharon.R;
import com.shubhasharon.modals.ItemComment;
import com.shubhasharon.ui.dialogs.AddComment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CommentAdapter adapter;
    ArrayList<ItemComment> feed = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String prod_id;
    boolean comment_statement=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prod_id = getIntent().getStringExtra("id");
        initViews();
        initRecyclerView();
    }

    public void initRecyclerView() {

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(this, feed);
        recyclerView.setAdapter(adapter);
        feed.clear();
        getData();
    }
    public void getData(){
        final String url = getString(R.string.link)+ "get_post?id="+prod_id;
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("link",url);
                //Log.e("response",response);
                int maxLogSize = 1000;
                for(int i = 0; i <= response.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i+1) * maxLogSize;
                    end = end > response.length() ? response.length() : end;
                    Log.e("VV", response.substring(start, end));
                }
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject json = jObject.getJSONObject("post");
                    if(json.getString("comment_status").equals("open")){
                        comment_statement = true;
                    }
                    // get comments
                    JSONArray comments = json.getJSONArray("comments");
                    Log.e("comments",comments.toString());

                    for (int k = 0; k < comments.length(); k++) {
                        JSONObject current = comments.getJSONObject(k);
                        ItemComment cc = new ItemComment();
                        cc.setComment(current.getString("content"));
                        cc.setComment_id(current.getString("id"));
                        cc.setDate(current.getString("date"));
                        cc.setUser(current.getString("name"));
                        feed.add(cc);
                        adapter.notifyItemInserted(feed.size() - 1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                //------popup
                if(comment_statement){
                    Intent ii = new Intent(CommentsActivity.this, AddComment.class);
                    ii.putExtra("commentPost",prod_id);
                    startActivityForResult(ii,96);
                }
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

    public void initViews() {
        recyclerView = findViewById(R.id.recy);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_add_comment:
                if(comment_statement){
                    Intent ii = new Intent(this, AddComment.class);
                    ii.putExtra("commentPost",prod_id);
                    startActivityForResult(ii,96);
                }else {
                    Toast.makeText(this,"Sorry .Adding comments not available",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==96 && resultCode==RESULT_OK){
            ItemComment cc = new ItemComment();
            cc.setComment(data.getStringExtra("result"));
            cc.setUser("ME");
            //cc.setDate();
            feed.add(cc);
            adapter.notifyItemInserted(feed.size()-1);
        }
    }
}

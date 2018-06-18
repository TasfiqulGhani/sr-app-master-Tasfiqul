package com.shubhasharon.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by wail babou on 2017-08-09.
 * Project : IN_Android_Official.
 */

public class AddComment extends AppCompatActivity {
    private String comment_post;
    private EditText caption, email, name;
    private FloatingActionButton submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.dialog_add_comment);
        comment_post = getIntent().getStringExtra("commentPost");
        Log.e("item_id_open", comment_post + "");
        initView();
        initListener();
    }

    private void initView() {
        caption = findViewById(R.id.comment);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        submit = findViewById(R.id.fab);
    }

    private void initListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caption.getText().length() > 0 && name.getText().length() > 0 && email.getText().length() > 0) {
                    changeData();
                } else {
                    Toast.makeText(AddComment.this, "Invalid data.. !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeData() {

        String url = null;
        try {
            url = URLEncoder.encode(caption.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String urlall = getString(R.string.link) + "submit_comment?" +
                "post_id=" + comment_post +
                "&name=" + name.getText().toString() +
                "&email=" + email.getText().toString() +
                "&content=" + url;
        StringRequest postrequest = new StringRequest(Request.Method.GET, urlall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", caption.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

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
                map.put("post_id", comment_post);
                map.put("name", name.getText().toString());
                map.put("email", email.getText().toString());
                map.put("content", caption.getText().toString());

                return map;
            }
        };

        postrequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(postrequest);
    }
}

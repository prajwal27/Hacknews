package com.example.prajw.hacknews;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prajw.hacknews.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.view.View.GONE;
import static com.android.volley.Request.Method.GET;

public class WebActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;
    private RecyclerCommentAdapter recyclerCommentAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.web);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.rv_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerViewSearchAdapter(this));
        toolbar.setTitle("Top");
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(getIntent().getStringExtra("url"));
        progressBar.setVisibility(GONE);
        String comment_list = getIntent().getStringExtra("comment_ids");
        ArrayList<Integer> comment_ids = new ArrayList<Integer>();
        StringTokenizer stringTokenizer =
                new StringTokenizer(comment_list, "[,]");
        while (stringTokenizer.hasMoreTokens()) {
            String id = stringTokenizer.nextToken();
            comment_ids.add(Integer.valueOf(id));
        }


        for(int i=0;i< comment_ids.size();i++){

            String url = getString(R.string.item)+String.valueOf(comment_ids.get(i));
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        String name="hi",comment="yo";
                    try {
                         name = response.getString("by");
                         comment = response.getString("text");
                    } catch (JSONException e) {
                        name="psp";comment="no";
                        e.printStackTrace();
                    }
                    recyclerCommentAdapter.add(name,comment);
                    recyclerView.setAdapter(recyclerCommentAdapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }



        //webView.setVisibility(View.VISIBLE);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.setWebChromeClient(new WebChromeClient());
        /*webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });*/


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

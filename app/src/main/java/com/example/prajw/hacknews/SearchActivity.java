package com.example.prajw.hacknews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.android.volley.Request.Method.GET;

public class SearchActivity extends AppCompatActivity{

    private Button searchButton;
    private TextView about,by,karma;
    private RelativeLayout userDetail;
    private EditText searchText;
    private RecyclerViewSearchAdapter recyclerViewSearchAdapter;
    private RecyclerView recyclerView;
    Listener listener;
    Object object;
    ArrayList<Long> storyID = new ArrayList<Long>();

    public interface Listener {
        /** Called when a response is received. */
        public void onResponse(Object tag, JSONObject response);
        public void onErrorResponse(Object tag, VolleyError error);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        searchButton = findViewById(R.id.search_button);
        searchText = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rv_submitted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerViewSearchAdapter(this));
        about = findViewById(R.id.about);
        by = findViewById(R.id.by);
        karma = findViewById(R.id.karma);
        userDetail = findViewById(R.id.user_detail);
        setupBottomNavigation();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                //MySingleton.getInstance(v.getContext()).getRequestQueue().cancelAll(object);
                Toast.makeText(v.getContext()," yooo ",Toast.LENGTH_LONG).show();
                //storyID.clear();
                recyclerViewSearchAdapter = new RecyclerViewSearchAdapter(getApplication().getApplicationContext());
                recyclerView.setAdapter(recyclerViewSearchAdapter);
                about.setText("");
                by.setText("");
                karma.setText("");
                //recyclerViewSearchAdapter.notifyDataSetChanged();
                String username = searchText.getText().toString();
                Log.d("search", "1");

                if(username.length()>0){
                    Toast.makeText(v.getContext()," if ",Toast.LENGTH_LONG).show();
                    username = username.trim();
                    String url = getString(R.string.user)+username+".json";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                userDetail.setVisibility(View.VISIBLE);
                            try {
                                about.setText(String.valueOf(response.getString("about")));
                                Log.d("search", "2");
                            } catch (JSONException e) {
                                about.setText(" NOTHING ");
                                e.printStackTrace();
                            }
                            try {
                                by.setText(String.valueOf(response.getString("by")));
                                Log.d("search", "3");
                            } catch (JSONException e) {
                                by.setText(searchText.getText().toString());
                                e.printStackTrace();
                            }

                            try {
                                karma.setText(String.valueOf(response.getInt("karma")));
                                Log.d("search", "4");
                            } catch (JSONException e) {
                                karma.setText("0 karma");
                                Log.d("search", "5");
                                e.printStackTrace();
                            }
                            try {
                                Log.d("nldn",String.valueOf(response.get("submitted")));
                                String id_array = String.valueOf(response.get("submitted"));
                                StringTokenizer stringTokenizer =
                                        new StringTokenizer(id_array, "[,]");
                                while (stringTokenizer.hasMoreTokens()) {
                                    String id = stringTokenizer.nextToken();
                                   // storyID.add(Long.valueOf(id));
                                    String item_url = getString(R.string.item) +id+".json";
                                    JsonObjectRequest objectRequest = new JsonObjectRequest(GET, item_url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            //listener.onResponse(object, response);
                                            int desc = 0;
                                            try {
                                                desc = (response.getInt("descendants"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            int score = 0;
                                            try {
                                                score = (response.getInt("score"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Long time = Long.valueOf("10000000");
                                            try {
                                                time = (response.getLong("time"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String title = "NO TITLE";
                                            try {
                                                title = (response.getString("title"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Long id = Long.valueOf("100000001");
                                            try {
                                                id = response.getLong("id");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String website = "https://www.google.co.in/";
                                            try {
                                                website = response.getString("url");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String name = "develpoer";
                                            try {
                                                 name = (response.getString("by"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                             /*String commentList = "1";

                                            try {
                                               commentList = String.valueOf(response.get("kids"));
                                                }
                                             catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/
                                            Log.d("search ", "yo");
                                            if(!title.equals("NO TITLE")/* && commentList.length()>1*/){
                                            Story s = new Story(name,title,website,desc,score,id,time);
                                            recyclerViewSearchAdapter.addStory(s);}
                                            recyclerView.notifyAll();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //listener.onErrorResponse(object,error);
                                            Toast.makeText(getApplicationContext()," error in deep: "+error,Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(objectRequest);
                                }
                                // storyID = (ArrayList<Long>) response.get("submitted");

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),"ffs",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }//recyclerViewSearchAdapter = new RecyclerViewSearchAdapter(getApplicationContext(),storyID);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext()," error: "+error,Toast.LENGTH_LONG).show();
                        }
                    });
                    MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }else{
                    Toast.makeText(v.getContext(),"  enter username da ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(SearchActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

}
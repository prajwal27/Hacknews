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
    private EditText searchText;
    private RecyclerViewSearchAdapter recyclerViewSearchAdapter;
    private RecyclerView recyclerView;
    ArrayList<Long> storyID = new ArrayList<Long>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        searchButton = findViewById(R.id.search_button);
        searchText = findViewById(R.id.search);
        recyclerView = findViewById(R.id.rv_submitted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        setupBottomNavigation();



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                storyID.clear();
                //recyclerViewSearchAdapter.notifyDataSetChanged();
                String username = searchText.getText().toString();

                if(username.length()>0){
                    username = username.trim();
                    String url = getString(R.string.user)+username+".json";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                Log.d("nldn",String.valueOf(response.get("submitted")));
                                String id_array = String.valueOf(response.get("submitted"));
                                StringTokenizer stringTokenizer =
                                        new StringTokenizer(id_array, "[,]");
                                while (stringTokenizer.hasMoreTokens()) {
                                    String id = stringTokenizer.nextToken();
                                    storyID.add(Long.valueOf(id));
                                }
                                // storyID = (ArrayList<Long>) response.get("submitted");


                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),"ffs",Toast.LENGTH_LONG);
                                e.printStackTrace();
                            }//recyclerViewSearchAdapter = new RecyclerViewSearchAdapter(getApplicationContext(),storyID);
                            recyclerViewSearchAdapter = new RecyclerViewSearchAdapter(getApplicationContext(),storyID);
                            recyclerView.setAdapter(recyclerViewSearchAdapter);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getApplicationContext()," error: "+error,Toast.LENGTH_LONG);

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

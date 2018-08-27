package com.example.prajw.hacknews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerViewPageAdapter favouriteAdapter;
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    private ArrayList<Long> fav;
    String uid;
    private Long id;
    JsonObjectRequest jsonObjectRequest;
    RecyclerViewPageAdapter recyclerViewPageAdapter;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewFav);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerViewPageAdapter = new RecyclerViewPageAdapter(getApplicationContext(),1);
        recyclerView.setAdapter(recyclerViewPageAdapter);

        firebaseFirestore.collection("Users").document(uid).collection("fav").document("lists").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        fav = (ArrayList<Long>)task.getResult().get("lists");

                    }else{

                        fav = new ArrayList<Long>();
                        Toast.makeText(FavouriteActivity.this,"Data doesn't exists",Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(FavouriteActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                }
            }
        });

        if(fav.size()>0)
        {
            for(int i=0;i<fav.size(); i++){
                id = fav.get(i);
                String url = getString(R.string.item)+String.valueOf(id)+".json";
                jsonObjectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Story s = new Story();
                        try {
                            s.setTime(response.getLong("time"));
                            s.setId(response.getLong("id"));
                            s.setScore(response.getInt("score"));
                            s.setDescendants(response.getInt("descendants"));
                            s.setUrl(response.getString("url"));
                            s.setTitle(response.getString("title"));
                            s.setBy(response.getString("by"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        recyclerViewPageAdapter.addStory(s);
                       // recyclerViewPageAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest);

            }

        }

        setupBottomNavigation();

    }

    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(FavouriteActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

}

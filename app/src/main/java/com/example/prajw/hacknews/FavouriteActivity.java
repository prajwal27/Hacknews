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
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<Long> favourites = new ArrayList<Long>();
    String uid;
    private Long id;
    JsonObjectRequest jsonObjectRequest;
    RecyclerViewPageAdapter recyclerViewPageAdapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        setupBottomNavigation();

        final RecyclerView recyclerView = findViewById(R.id.recyclerView_fav);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d("addd","deddeed");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPageAdapter = new RecyclerViewPageAdapter(this,1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(recyclerViewPageAdapter);
        Toast.makeText(this,uid,Toast.LENGTH_LONG);
        firebaseFirestore.collection("Users").document(uid).collection("fav").document("lists").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        favourites = (ArrayList<Long>)task.getResult().get("lists");

                        Toast.makeText(FavouriteActivity.this,"Fav data exist  "+String.valueOf(favourites.size()),Toast.LENGTH_LONG).show();


                    }else{

                        favourites = new ArrayList<Long>();
                        Toast.makeText(FavouriteActivity.this,"Data doesn't exists",Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(FavouriteActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                }
            }
        });

        if(favourites.size()>0)
        {
            for(int i=0;i<favourites.size(); i++){
                id = favourites.get(i);
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
                        recyclerView.setAdapter(recyclerViewPageAdapter);
                        recyclerViewPageAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        }


    }

    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(FavouriteActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        Log.d("nininin","inidnedn");
    }

}

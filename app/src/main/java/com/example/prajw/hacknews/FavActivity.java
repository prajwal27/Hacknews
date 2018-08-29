package com.example.prajw.hacknews;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.Request.Method.GET;

public class FavActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFav;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<Long> favourites = new ArrayList<Long>();

    @Override
    protected void onStart() {
        super.onStart();

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("fav").document("lists").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        favourites = (ArrayList<Long>) task.getResult().get("idk");
                        //Toast.makeText(getApplication().getApplicationContext(),"sdads",Toast.LENGTH_LONG).show();
                        Log.d("nininini:",favourites.toString());

                    }else{

                        //favourites = new ArrayList<Long>();
                        Toast.makeText(FavActivity.this,"Data doesn't exists",Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(FavActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        setupBottomNavigation();

         final RecyclerViewPageAdapter recyclerViewPageAdapter = new RecyclerViewPageAdapter(this,1);
        recyclerViewFav = findViewById(R.id.rv_fav);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d("addd","deddeed");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerViewPageAdapter = new RecyclerViewPageAdapter(this,1);
        recyclerViewFav.setLayoutManager(linearLayoutManager);
        recyclerViewFav.setHasFixedSize(true);
        recyclerViewFav.setAdapter(recyclerViewPageAdapter);
        Toast.makeText(this,uid,Toast.LENGTH_LONG);



                            //for(int i=0; i<favourites.size();i++){
                                String url = getString(R.string.item)+String.valueOf(17859963)+".json";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            recyclerViewPageAdapter.addStory(new Story(response.getString("by"),response.getString("title"),response.getString("url"),response.getInt("descendants"),response.getInt("score"),response.getLong("id"),response.getLong("time")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("eacasac: ",response.toString());
                                        }
                                        recyclerViewFav.setAdapter(recyclerViewPageAdapter);
                                        recyclerViewPageAdapter.notifyDataSetChanged();

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
        jsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                           // }
                            //Toast.makeText(FavActivity.this,"Fav data exist  "+String.valueOf(favourites.size()),Toast.LENGTH_LONG).show();



    }

    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(FavActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        Log.d("nininin","inidnedn");
    }
}

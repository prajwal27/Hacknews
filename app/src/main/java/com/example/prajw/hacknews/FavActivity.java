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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class FavActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFav;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    RecyclerViewPageAdapter recyclerViewPageAdapter;
    ArrayList<Story> stories = new ArrayList<Story>();
    ArrayList<String> fav = new ArrayList<String>();

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        setupBottomNavigation();

        recyclerViewPageAdapter = new RecyclerViewPageAdapter(this, 1);
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

        firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("fav").document("lists").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        //favourites = (ArrayList<Long>) task.getResult().get("idk");
                        fav = (ArrayList<String>)((Map) task.getResult().get("vaf")).get("fav");
                        for(int  i = 0;i<fav.size();i++){
                            Story s = GsonHelper.getInstance(getApplication().getApplicationContext()).getGson().fromJson(fav.get(i),Story.class);
                            //recyclerViewPageAdapter.addStory(s);
                            stories.add(s);
                            Log.d("ssss ",s.toString());
                            //recyclerViewFav.setAdapter(recyclerViewPageAdapter);
                            Log.d("log ",stories.toString());
                            Log.d("size", String.valueOf(fav.size()));
                            //Log.d("stories ", String.valueOf(recyclerViewPageAdapter.getStories().get(1).toString()));
                            //Log.d("story ", s.getTitle()+" "+s.getBy()+" "+s.getId()+" "+s.getDescendants()+" "+s.getUrl()+" "+s.getTime()+s.getScore());
                        }
                        recyclerViewPageAdapter.setStories(stories);
                        //recyclerViewPageAdapter.notifyDataSetChanged();
                        //recyclerViewPageAdapter.notify();
                        //Toast.makeText(getApplication().getApplicationContext(),"sdads",Toast.LENGTH_LONG).show();
                        Log.d("nininini:",fav.get(0).toString());

                    }else{

                        //favourites = new ArrayList<Long>();
                        Toast.makeText(FavActivity.this,"Data doesn't exists",Toast.LENGTH_LONG).show();
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(FavActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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

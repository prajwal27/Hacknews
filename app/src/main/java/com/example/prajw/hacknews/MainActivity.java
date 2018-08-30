package com.example.prajw.hacknews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.prajw.hacknews.LoginActivity;
import com.example.prajw.hacknews.BottomNavigationViewHelper;
import com.example.prajw.hacknews.MySingleton;
import com.example.prajw.hacknews.RecyclerViewPageAdapter;
import com.example.prajw.hacknews.Story;
//import com.example.prajw.hacknews.BestFragment;
//import com.example.prajw.hacknews.RecentFragment;
import com.example.prajw.hacknews.SectionsPageAdapter;
//import com.example.prajw.hackernews.Home.TopFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static com.android.volley.Request.Method.GET;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String uid;
    private int i;
    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;
    private static int ctr = 0;
    private ViewPager viewPager;
    public ArrayList<Long> favourites = new ArrayList<Long>();
    public ArrayList<String> fav = new ArrayList<>();
    private RelativeLayout listParent;
    //private ProgressBar progressBar;
    RecyclerViewPageAdapter topAdapter;
    RecyclerViewPageAdapter recentAdapter;
    RecyclerViewPageAdapter bestAdapter;
    private Story s;

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuth.getCurrentUser() != null) {
            Map<String, ArrayList<Long>> userMap = new HashMap<String, ArrayList<Long>>();
            Map<String, ArrayList<String>> favvv = new HashMap<String, ArrayList<String>>();
            userMap.put("idk", this.favourites);
            favvv.put("fav",this.fav);
            Map<String, Map> f = new HashMap<String, Map>();
            f.put("kdi",userMap);
            f.put("vaf",favvv);
            firebaseFirestore.collection("Users").document(uid).collection("fav").document("lists").set(f).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Log.d("bbdksbckb", "nndefncenf");
                    } else {
                        Log.d("k kc ak cddc ", "nckdncdncdscdcndslj");
                    }
                }
            });
        }
        if (firebaseAuthListener != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFirebaseAuth();
        setupBottomNavigation();
        listParent = findViewById(R.id.tab_container);
        TabLayout listTabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container);
        listTabs.setupWithViewPager(viewPager);
        viewPager.setAdapter(new CustomListPagerAdapter());
        viewPager.setCurrentItem(CustomListPagerAdapter.TOP_TAB);

        try {
            if (getIntent().getStringExtra("logout").equals("logout")) {
                MySingleton.getInstance(this).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });
                mAuth.signOut();
            }
        }catch (NullPointerException e){
            ;
        }

        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        //firebaseFirestore = FirebaseFirestore.getInstance();

        //progressBar.setVisibility(View.VISIBLE);
        //favourite = (FavouriteActivity)getApplicationContext();
        bestAdapter = new RecyclerViewPageAdapter(this, 0);
        recentAdapter = new RecyclerViewPageAdapter(this, 0);
        topAdapter = new RecyclerViewPageAdapter(this, 0);

        StringRequest stringRequest = new StringRequest(GET
                , getString(R.string.topstories)
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = new JSONObject();
                try {
                    object.put("response", response);
                    object.put("int", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error on the wall", e.toString());
                }
                // new BackgroundListLoadTask().execute(object);
                StringTokenizer stringTokenizer1 =
                        new StringTokenizer(response.substring(0, 141), "[,]");
                while (stringTokenizer1.hasMoreTokens()) {
                    //Long id1 = Long.valueOf(stringTokenizer1.nextToken());

                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(GET, getString(R.string.item) + stringTokenizer1.nextToken() + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                topAdapter.addStory(new Story(response.getString("by"), response.getString("title"), response.getString("url"), response.getInt("descendants"), response.getInt("score"), response.getLong("id"), response.getLong("time")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("eacasac: ", response.toString());
                            }
                            topAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest1);
                }
                StringRequest stringRequest2 = new StringRequest(GET, getString(R.string.newstories), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        StringTokenizer stringTokenizer2 =
                                new StringTokenizer(response.substring(0, 141), "[,]");
                        while (stringTokenizer2.hasMoreTokens()) {

                            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(GET, getString(R.string.item) + stringTokenizer2.nextToken() + ".json", null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        recentAdapter.addStory(new Story(response.getString("by"), response.getString("title"), response.getString("url"), response.getInt("descendants"), response.getInt("score"), response.getLong("id"), response.getLong("time")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("error: ", "json object 2");
                                    }
                                    recentAdapter.notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest2);
                        }

                        //BEST STORIES
                        StringRequest stringRequest3 = new StringRequest(GET, getString(R.string.beststories), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                StringTokenizer stringTokenizer3 =
                                        new StringTokenizer(response.substring(0, 141), "[,]");
                                while (stringTokenizer3.hasMoreTokens()) {

                                    JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(GET, getString(R.string.item) + stringTokenizer3.nextToken() + ".json", null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                bestAdapter.addStory(new Story(response.getString("by"), response.getString("title"), response.getString("url"), response.getInt("descendants"), response.getInt("score"), response.getLong("id"), response.getLong("time")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            bestAdapter.notifyDataSetChanged();

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(jsonObjectRequest3);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("stringrequest3 :", error.toString());

                            }
                        });
                        MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(stringRequest3);
                        //BEST STORIES
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("stringrequest2 :", error.toString());
                    }
                });
                MySingleton.getInstance(getApplication().getApplicationContext()).addToRequestQueue(stringRequest2);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Selected " + error.getMessage());
                Log.v("stringrequest1 :", error.toString());
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in." + String.valueOf(ctr));
        ctr++;
        if (user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d("dsd", "deed");
                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    storageReference = FirebaseStorage.getInstance().getReference();
                    uid = mAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {

                                    Log.d("cdcac", "DATA EXISTS");
                                } else {
                                    Toast.makeText(MainActivity.this, "Data doesn't exists", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("start", "1"));
                                    finish();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    firebaseFirestore.collection("Users").document(uid).collection("fav").document("lists").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {

                                    Toast.makeText(MainActivity.this, "Fav data exist", Toast.LENGTH_LONG).show();
                                    favourites = (ArrayList<Long>)((Map) task.getResult().get("kdi")).get("idk");
                                    fav = (ArrayList<String>)((Map) task.getResult().get("vaf")).get("fav");

                                } else {

                                    //favourites = new ArrayList<Long>();
                                    Toast.makeText(MainActivity.this, "Fav data doesnt exist", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                //favourites = new ArrayList<Long>();
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                    //favourites = new ArrayList<Long>();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("sfsf", "aaad00");
        mAuth.addAuthStateListener(firebaseAuthListener);
        Log.d("dad", "dsad");
        //viewPager.setCurrentItem(HOME_FRAGMENT);
        //checkCurrentUser(mAuth.getCurrentUser());
    }


    /**
     * this is for setting up bottomnavigation
     */
    public void setupBottomNavigation() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottom_nav);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MainActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    /**
     * this is for setting up 3 tabs
     */
     /* private void setupViewPager(){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TopFragment());
        adapter.addFragment(new RecentFragment());
        adapter.addFragment(new BestFragment());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("TOP");
        tabLayout.getTabAt(1).setText("RECENT");
        tabLayout.getTabAt(2).setText("BEST");
    }*/

    /**
     * Tabview + pager for the custom list.
     * This class serves as a adapter to make the tabview and pager work in sync
     * It makes a VolleyRequest to get the list of students.
     * The students obtained are then updated in the setup method to display the data in recycler view.
     */
    class CustomListPagerAdapter extends PagerAdapter {
        public static final String TAG = "CustomPagerAdapter";

        static final int TOP_TAB = 0;
        static final int RECENT_TAB = 1;
        static final int BEST_TAB = 2;

        // Variables necessary for managing pager and adapter
        private String titles[] = {"Top", "Recent", "Best"};
        private int layouts[] = {R.layout.fragment_top, R.layout.fragment_recent, R.layout.fragment_best};
        private int ids[] = {R.id.recyclerView1, R.id.recyclerView2, R.id.recyclerView3};

        CustomListPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int tabPosition) {
            LayoutInflater inflater = (LayoutInflater)
                    getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup layout = (ViewGroup) inflater.inflate(layouts[tabPosition],
                    container, false);
            container.addView(layout);

            RecyclerView rv = layout.findViewById(ids[tabPosition]);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rv.setNestedScrollingEnabled(false);
            if (tabPosition == TOP_TAB) {
                //topAdapter.notifyDataSetChanged();
                rv.setAdapter(topAdapter);
            } else if (tabPosition == RECENT_TAB) {
                //recentAdapter.notifyDataSetChanged();
                rv.setAdapter(recentAdapter);
            } else if (tabPosition == BEST_TAB) {
                //bestAdapter.notifyDataSetChanged();
                rv.setAdapter(bestAdapter);
            }
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int tabPosition, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int tabPosition) {
            return titles[tabPosition];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

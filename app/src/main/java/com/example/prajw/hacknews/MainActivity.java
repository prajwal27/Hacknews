package com.example.prajw.hacknews;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prajw.hackernews.Authentication.LoginActivity;
import com.example.prajw.hackernews.Common.BottomNavigationViewHelper;
import com.example.prajw.hackernews.Common.MySingleton;
import com.example.prajw.hackernews.Common.RecyclerViewPageAdapter;
import com.example.prajw.hackernews.Common.Story;
import com.example.prajw.hackernews.Home.BestFragment;
import com.example.prajw.hackernews.Home.RecentFragment;
import com.example.prajw.hackernews.Home.SectionsPageAdapter;
import com.example.prajw.hackernews.Home.TopFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String uid;
    private int i;
    private int j[] = {R.string.topstories,R.string.newstories,R.string.beststories};
    private static final String TAG = "MainActivity";
    private static final int HOME_FRAGMENT = 0  ;
    private Context mContext = MainActivity.this;
    private static int  ctr =0;
    private ViewPager viewPager;
    private StringRequest stringRequest;
    private JsonObjectRequest jsonObjectRequest;
    //private String urll;
    public static ArrayList<Story> top,best,recent ;
    // ArrayList<Story> tempo = new ArrayList<Story>();
    private RelativeLayout listParent;
    //private ProgressBar progressBar;
    RecyclerViewPageAdapter topAdapter;
    RecyclerViewPageAdapter recentAdapter;
    RecyclerViewPageAdapter bestAdapter;
    RecyclerViewPageAdapter favouriteAdapter;

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("top"
                , (ArrayList<? extends Parcelable>) topAdapter.getList());
        outState.putParcelableArrayList("recent"
                , (ArrayList<? extends Parcelable>) recentAdapter.getList());
        outState.putParcelableArrayList("best"
                , (ArrayList<? extends Parcelable>) bestAdapter.getList());
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.container);

        listParent = findViewById(R.id.tab_container);
        TabLayout listTabs = findViewById(R.id.tabs);
        listTabs.setupWithViewPager(viewPager);
        //progressBar.setVisibility(View.VISIBLE);

        bestAdapter = new RecyclerViewPageAdapter(this,0);
        recentAdapter = new RecyclerViewPageAdapter(this,0);
        topAdapter = new RecyclerViewPageAdapter(this,0);
        favouriteAdapter = new RecyclerViewPageAdapter(this,1);

        if (savedInstanceState != null) {
            // progressBar.setVisibility(View.GONE);
            // bestAdapter.getList(savedInstanceState.getParcelableArrayList("top"));
            //topAdapter.setList(savedInstanceState.getParcelableArrayList("recent"));
            //recentAdapter.setList(savedInstanceState.getParcelableArrayList("best"));
            ;;
        }else {
            for(i = 0; i<2 ; i++){

                String url = getString(j[i]);
                //String url = getString(R.string.topstories);
                StringRequest stringRequest = new StringRequest(Request.Method.GET
                        , url
                        , new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("response",response);
                            object.put("int",i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error on the wall",e.toString());
                        }
                        new BackgroundListLoadTask().execute(object);
                    }
                } ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Selected " + error.getMessage());
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
            //Loading selected
            //String url = getString(R.string.URL_PROFESSOR_DETAILED_INFO)
            //      + User.getInstance(getApplicationContext()).getUsername()
            //    + "/";
             /*{
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token "
                            + User.getInstance(getApplicationContext()).getToken());
                    return params;
                }

            };*/

        }
        viewPager.setAdapter(new CustomListPagerAdapter());
        viewPager.setCurrentItem(CustomListPagerAdapter.RECENT_TAB);
        //top = jsonrequest(getString(R.string.topstories));
        //best = jsonrequest(getString(R.string.beststories));
        //recent= jsonrequest(getString(R.string.newstories));
        //Log.d("dcscd",recent.toString());

        /** mAuth = FirebaseAuth.getInstance();
         firebaseFirestore = FirebaseFirestore.getInstance();

         firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        FirebaseUser user =firebaseAuth.getCurrentUser();
        if(user == null){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
        }else{

        firebaseFirestore.collection("Users").document(user.getUid().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

        if (task.isSuccessful()) {

        if (task.getResult().exists()) {


        } else {
        Toast.makeText(MainActivity.this, "Data doesn't exists", Toast.LENGTH_LONG).show();
        startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("start","1"));
        finish();
        }
        } else {
        String error = task.getException().getMessage();
        Toast.makeText(MainActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

        }
        }
        });

        }
        }
        };*/
        Log.d("ddxd0","cdc");
        setupFirebaseAuth();
        setupBottomNavigation();
        Log.d("ddsd","ac");
        //setupViewPager();
    }

    class BackgroundListLoadTask extends AsyncTask<JSONObject, Integer, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            try {
                JSONObject obj = jsonObjects[0];
                String response = obj.getString("response");
                int ch = obj.getInt("int");
                if(ch == 0) {
                    top = new ArrayList<>();
                }else if(ch == 1){
                    recent = new ArrayList<>();
                }else{
                    best = new ArrayList<>();
                }
                Log.d(TAG, response);
                StringTokenizer stringTokenizer =
                        new StringTokenizer(response, "[,]");
                while (stringTokenizer.hasMoreTokens()) {
                    String id = stringTokenizer.nextToken();
                    Story story = new Story();
                    story.setId(Long.valueOf(id));
                    //student.setSelected(true);
                    if(ch == 0) {
                        top.add(story);
                    }else if(ch == 1){
                        recent.add(story);
                    }else{
                        best.add(story);
                    }
                }
                /*if (!response.isNull("selectedStudents")
                        && !response.getString("selectedStudents").equals("")) {
                    String selectedStudents = response.getString("selectedStudents");
                    selectedStudents = selectedStudents.replace("\"[", "");
                    selectedStudents = selectedStudents.replace("]\"", "");
                    StringTokenizer stringTokenizer =
                            new StringTokenizer(selectedStudents, ",");
                    Log.d(TAG, selectedStudents);
                    while (stringTokenizer.hasMoreTokens()) {
                        String username = stringTokenizer.nextToken();
                        Student student = new Student();
                        student.setUsername(username);
                        student.setSelected(true);
                        selected.add(student);
                    }
                }*/

             /*   String url = getString(R.string.URL_GET_LIST)
                        + User.getInstance(getApplicationContext()).getUsername()
                        + "/";
                JsonArrayRequest getList = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    List<Student> all = new ArrayList<>();
                                    List<Student> unselected = new ArrayList<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        String username = response
                                                .getJSONObject(i)
                                                .getString("username");
                                        Student student = new Student();
                                        student.setUsername(username);
                                        if (selected.contains(student)) {
                                            selected.contains(student);
                                            student.setSelected(true);
                                        } else {
                                            student.setSelected(false);
                                            unselected.add(student);
                                        }
                                        all.add(student);


                                    }
                                    Log.d(TAG, "Selected Students " + selected.toString());
                                    */
                if(ch==0){
                    new GetDetailsTask("Top", top, topAdapter).execute();
                }else if(ch==2){
                    new GetDetailsTask("Recent", recent, recentAdapter).execute();
                }else{
                    new GetDetailsTask("Best", best, bestAdapter).execute();
                }

                //new GetDetailsTask("Selected", selected, mSelectedAdapter).execute();
                //new GetDetailsTask("Unselected", unselected, mUnselectedAdapter).execute();
                                    /*
                                } catch (Exception e) {
                                    Log.d(TAG, e.getMessage());

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Token "
                                + User.getInstance(getApplicationContext()).getToken());
                        return params;
                    }

                };
                VolleyHelper.getInstance(getApplicationContext()).addToRequestQueue(getList);*/

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

    }

    class GetDetailsTask extends AsyncTask<Void, Void, Void> {
        private RecyclerViewPageAdapter adapter;
        private String listType;
        private List<Story> stories;

        GetDetailsTask(String listType, List<Story> stories, RecyclerViewPageAdapter adapter) {
            this.listType = listType;
            this.adapter = adapter;
            this.stories = stories;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (stories.size() > 0)
                getDetails(stories.get(0));
            return null;
        }

        private void getDetails(final Story story) {
            String url = getString(R.string.item) + story.getId() + ".json";
            JsonObjectRequest getDetails = new JsonObjectRequest(Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (stories.size() > 1) {
                                    stories.remove(0);
                                    getDetails(stories.get(0));
                                }
                                story.setBy(response.getString("by"));
                                story.setDescendants(response.getInt("descendants"));
                                story.setScore(response.getInt("score"));
                                story.setTime(response.getLong("time"));
                                story.setTitle(response.getString("title"));
                                adapter.addStory(story);
                                //Log.d(TAG, "Get details " + student.getUsername() + "");
                                //if (listType.equals("Top"))
                                //progressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Volley Error" + error.getMessage());
                        }
                    })/* {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token "
                            + User.getInstance(getApplicationContext()).getToken());
                    return params;
                }
            }*/;
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(getDetails);
        }
    }


    private void checkCurrentUser(FirebaseUser user){

        Log.d(TAG, "checkCurrentUser: checking if user is logged in."+String.valueOf(ctr));ctr++;

        if(user == null){
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);finish();
        }
    }
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d("dsd","deed");
                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                if (task.getResult().exists()) {

                                    Log.d("cdcac","DATA EXISTS");
                                } else {
                                    Toast.makeText(MainActivity.this, "Data doesn't exists", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("start","1"));
                                    finish();
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(MainActivity.this, "(Firestore Retrieve)error: " + error, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
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
        Log.d("sfsf","aaad00");
        mAuth.addAuthStateListener(firebaseAuthListener);
        Log.d("dad","dsad");
        viewPager.setCurrentItem(HOME_FRAGMENT);
        //checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener!= null){
            mAuth.removeAuthStateListener(firebaseAuthListener);}
    }

    /**
     * this is for setting up bottomnavigation
     */
    public void setupBottomNavigation(){
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
    private void setupViewPager(){
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
    }

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
            LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup layout = (ViewGroup) inflater.inflate(layouts[tabPosition], container, false);
            container.addView(layout);

            RecyclerView rv = layout.findViewById(ids[tabPosition]);
            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rv.setNestedScrollingEnabled(false);
            if (tabPosition == TOP_TAB) {
                rv.setAdapter(topAdapter);
            } else if (tabPosition == RECENT_TAB) {
                rv.setAdapter(recentAdapter);
            } else if (tabPosition == BEST_TAB) {
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

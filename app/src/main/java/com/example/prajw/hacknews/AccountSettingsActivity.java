package com.example.prajw.hacknews;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AccountSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String TAG = "MainActivity";
    private Context context;
    private ListView listView;
    private FirebaseAuth mAuth;
    AlertDialog alert;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Context mContext = AccountSettingsActivity.this;
    //private MainActivity activity =(MainActivity)getApplication().getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        //  MainActivity activity = (MainActivity)getApplication().getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        context=AccountSettingsActivity.this;
        toolbar = findViewById(R.id.profileToolBar);
        toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setupSettings();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        Toast.makeText(view.getContext(),"EDIT PROFILE",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AccountSettingsActivity.this,ProfileActivity.class).putExtra("start",1));
                        finish();
                        break;
                    case  1:
                        Details();
                        Toast.makeText(view.getContext(),"LOG OUT",Toast.LENGTH_SHORT).show();
                        break;
                    default:break;
                }
            }
        });

    }

    private void setupSettings(){
         listView = findViewById(R.id.lvAccountSettings);
        ArrayList<String> options = new ArrayList<>();
        options.add("Edit Profile");
        options.add("Log Out");
        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
    }

    private void Details(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);

        builder.setMessage("Do want to logout?")
                .setCancelable(false)
                .setPositiveButton( getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                        {
                            public void onClick(final DialogInterface dialog, int id) {
                               // mAuth.signOut();
                                //mAuth.signOut();
                                //clearAppData();
                                //activity.mAuth.signOut();
                                startActivity(new Intent(getApplication().getApplicationContext(),MainActivity.class).putExtra("logout","logout"));
                                finish();
                                System.exit(0);
                            }
                        }
                )
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //dialog box details
        alert = builder.create();
        alert.setTitle(" EXIT APP ");
        alert.show();
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear "+packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

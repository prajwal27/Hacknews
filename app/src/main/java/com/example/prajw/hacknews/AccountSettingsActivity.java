package com.example.prajw.hacknews;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AccountSettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

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
    }

    private void setupSettings(){
        ListView listView = findViewById(R.id.lvAccountSettings);
        ArrayList<String> options = new ArrayList<>();
        options.add("Edit Profile");
        options.add("Log Out");
        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,options);
        listView.setAdapter(adapter);
    }
}

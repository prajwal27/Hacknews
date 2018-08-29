package com.example.prajw.hacknews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){

                    case R.id.ic_house:
                        Intent intent1 = new Intent(context,MainActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context,SearchActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_favourite:
                        //if(opt != 2){
                        Intent intent3 = new Intent(context,FavActivity.class);
                        context.startActivity(intent3);//}
                        break;
                    case R.id.ic_profile:
                        Intent intent4 = new Intent(context,ProfileActivity.class);intent4.putExtra("start","0");
                        context.startActivity(intent4);
                        break;
                        default:break;
                }
                return false;
            }
        });
    }
}

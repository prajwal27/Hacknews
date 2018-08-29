package com.example.prajw.hacknews;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Singleton class to create GSON object.
 */
public class GsonHelper
{
    private static final String TAG = "GsonHelper" ;
    private static GsonHelper mInstance;
    private static Gson gson ;
    private static Context mContext ;
    GsonHelper(Context context)
    {
        mContext = context;
        gson=new Gson();
    }
    public static synchronized GsonHelper getInstance(Context mContext)
    {
        // If Instance is null then initialize new Instance
        if(mInstance == null){
            mInstance = new GsonHelper(mContext);
        }
        // Return MySingleton new Instance
        return mInstance;
    }
    public Gson getGson()
    {
        //If gson null create a new one
        if(gson==null)
            return new Gson();
        // Return existing gson
        Log.d(TAG,"get Gson called");
        return gson;
    }
}

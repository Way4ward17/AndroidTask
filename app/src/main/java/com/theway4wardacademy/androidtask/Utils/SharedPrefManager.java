package com.theway4wardacademy.androidtask.Utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Way4wardPC on 10/12/2018.
 */
public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private  Context mCtx;
    private  final String TOKEN = "token";

    public SharedPrefManager(Context context) {

        mCtx = context;

    }






    public String getToken(){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                TOKEN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", null);
    }

    public boolean saveToken(String id){
        SharedPreferences sharedPreferences =  mCtx.getSharedPreferences(
                TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.apply();
        return true;
    }

}


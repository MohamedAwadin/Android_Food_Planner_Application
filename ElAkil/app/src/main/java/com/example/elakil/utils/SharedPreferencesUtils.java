package com.example.elakil.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String PREF_NAME = "FoodPlannerPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_GUEST = "isGuest" ;


    private SharedPreferences sharedPreferences ;

    public SharedPreferencesUtils(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME , Context.MODE_PRIVATE);
    }

    public void setLoggedIn(boolean isLoggedIn){
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setGuestMode(boolean isGuest){
        sharedPreferences.edit().putBoolean(KEY_IS_GUEST, isGuest).apply();
    }

    public boolean isGuestMode(){
        return sharedPreferences.getBoolean(KEY_IS_GUEST, false);
    }

    public void clear(){
        sharedPreferences.edit().clear().apply();
    }


}

package com.vys.todo.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    Context context;
    private String prefName = "Shared Preferences";
    private SharedPreferences prefs;
    public static String isLoggedIn = "logged";
    public static String username = "username";
    public static String password = "password";
    public static String token = "token";

    public SharedPrefs(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
    }

    public boolean getIsLoggedIn() {
        return this.prefs.getBoolean(isLoggedIn,false);
    }

    public void setIsLoggedIn(boolean value) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putBoolean(isLoggedIn,value);
        editor.apply();
    }

    public String getUsername() {
        return this.prefs.getString(username,"");
    }

    public void setUsername(String value) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(username,value);
        editor.apply();
    }

    public String getPassword() {
        return this.prefs.getString(password,"");
    }

    public void setPassword(String value) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(password,value);
        editor.apply();
    }

    public static String getToken() {
        return token;
    }

    public void setToken(String value) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(password,value);
        editor.apply();
    }
}

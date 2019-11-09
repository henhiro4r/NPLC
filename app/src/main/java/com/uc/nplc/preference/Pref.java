package com.uc.nplc.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String PREF = "user";
    private Context context;
    private SharedPreferences sharedPreferences;
    private String idKey = "idKey";
    private String nameKey = "nameKey";
    private String pointKey = "pointKey";

    public Pref(Context context) {
        this.context = context;
    }

    public String getIdKey() {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(idKey, "");
    }

    public void setIdKey(String value) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(idKey, value);
        editor.apply();
    }

    public String getNameKey() {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(nameKey, "");
    }

    public void setNameKey(String value) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nameKey, value);
        editor.apply();
    }

    public String getPointKey() {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(pointKey, "");
    }

    public void setPointKey(String value) {
        sharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pointKey, value);
        editor.apply();
    }

    public void deletePref(){
        setIdKey("");
        setNameKey("");
        setPointKey("");
    }
}

package com.nect.friendlymony.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class Pref {

    public static SharedPreferences preferences;

    public static void openPref(Context context) {
        Pref.preferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setStringValue(Context context, String key, String value) {
        Pref.openPref(context);
        SharedPreferences.Editor editor = Pref.preferences.edit();
        editor.putString(key, value);
        editor.commit();
        Pref.preferences = null;
    }

    public static void setIntValue(Context context, String key, int value) {
        Pref.openPref(context);
        SharedPreferences.Editor editor = Pref.preferences.edit();
        editor.putInt(key, value);
        editor.commit();
        Pref.preferences = null;
    }

    public static String getStringValue(Context context, String key, String defvalue) {
        Pref.openPref(context);
        String result = Pref.preferences.getString(key, defvalue);
        Pref.preferences = null;

        return result;
    }

    public static int getintValue(Context context, String key, int defvalue) {
        Pref.openPref(context);
        int result = Pref.preferences.getInt(key, defvalue);
        Pref.preferences = null;
        return result;
    }

    public static void clearPref(Context context) {
        Pref.openPref(context);
        SharedPreferences.Editor editor = Pref.preferences.edit();
        editor.clear();
        editor.commit();
    }
}

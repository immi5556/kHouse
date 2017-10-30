package com.khouse.immi.chuck24hoursbible;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Immanuel on 6/19/2017.
 */

public class CommonPreference {
    public static final String PREFS_NAME = "MyPrefsFile";
    public static boolean IsFileSaved(String filename, Context ctx){
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        boolean rest = settings.getBoolean(filename, false);
        return  rest;
    }

    public static void SaveFile(String filename, Context ctx){
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(filename, true);
        editor.commit();
    }
}

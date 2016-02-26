package com.zjianhao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zjianhao on 15-9-15.
 */
public class SharePreferenceUtils {




    public static void saveString(Context context,String name,String key,String value){
        SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void saveBoolean(Context context,String name,String key,boolean value){
        SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }
    public static String getStringValue(Context context,String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        if (preferences == null)
            return null;
        return preferences.getString(key,null);
    }

    public static boolean getBooleanValue(Context context,String name,String key){
        SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        if (preferences == null)
            return false;
        return preferences.getBoolean(key,false);
    }
}

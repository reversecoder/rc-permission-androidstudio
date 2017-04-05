package com.reversecoder.permission.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by rashed on 4/5/17.
 */

public class SessionManager {

    public static void setStringSetting(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * get value from shared preference
     *
     * @param context
     * @param key
     * @return string
     */
    public static String getStringSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");

    }

    /**
     * get value from shared preference if not found return given default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return string
     */
    public static String getStringSetting(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);

    }

    /**
     * Set boolean value in shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBooleanSetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * get boolean value from shared preference if not found return given
     * default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return boolean
     */
    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);

    }

    /**
     * Set int value in shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setIntegerSetting(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * get integer value from shared preference if not found return given
     * default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return boolean
     */
    public static int getIntegerSetting(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);

    }

    /**
     * remove item from shared preference
     *
     * @param context
     * @param key
     */
    public static void removeSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public boolean removeAllSetting(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        return true;
    }

    /**
     * get shared preference editor
     *
     * @param context
     * @return
     */
    public static Editor getEditor(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit();
    }

}
package com.bm.hm.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.bm.hm.base.HMApplication;
import com.bm.hm.constant.Constant;

public class SharedPreferencesUtils {

    static SharedPreferencesUtils instance;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesUtils() {
        mSharedPreferences = HMApplication.getInstance().getSharedPreferences(Constant.SP_NAME, Activity.MODE_PRIVATE);
    }

    // 单例模式
    public static synchronized SharedPreferencesUtils getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesUtils();
        }
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

}

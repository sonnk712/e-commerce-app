package com.LTTBDD.ecommerce_app.common.config.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AbhiAndroid
 */

public class StorageUser {
    private static final String SHARED_PREF = "userData";
    private Context context;

    public StorageUser(Context context) {
        this.context = context;
    }

    public void saveUserData(String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSavedUserData(String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        return pref.getString(key, "");
    }

    public void removeFromStorage() {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}

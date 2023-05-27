package com.LTTBDD.ecommerce_app.common.config.storage;

import android.content.Context;

import com.LTTBDD.ecommerce_app.common.utils.DateUtils;
import com.LTTBDD.ecommerce_app.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataLocalManager {
    private static final String SHARED_PREF = "userData";
    private static DataLocalManager instance;
    private StorageUser currUser;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.currUser = new StorageUser(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setCurrUser(User user){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String strCurrUser = gson.toJson(user);
        DataLocalManager.getInstance().currUser.saveUserData(SHARED_PREF, strCurrUser);
    }

    public static User getCurrUser(){
        String strCurrUser = DataLocalManager.getInstance().currUser.getSavedUserData(SHARED_PREF);
        Gson gson = new Gson();
        User currUser = gson.fromJson(strCurrUser, User.class);
        return currUser;
    }
    public static void logout(){
        DataLocalManager.getInstance().currUser.removeFromStorage();
    }

}

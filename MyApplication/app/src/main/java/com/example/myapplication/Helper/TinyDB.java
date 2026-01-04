package com.example.myapplication.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.example.myapplication.domain.ItemsModel;
import com.example.myapplication.domain.OrderModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TinyDB {
    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public void putListObject(String key, ArrayList<ItemsModel> objArray) {
        Gson gson = new Gson();
        String json = gson.toJson(objArray);
        preferences.edit().putString(key, json).apply();
    }

    public ArrayList<ItemsModel> getListObject(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<ArrayList<ItemsModel>>() {}.getType();
            ArrayList<ItemsModel> list = gson.fromJson(json, type);
            return (list != null) ? list : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }

    public void putListOrder(String key, ArrayList<OrderModel> objArray) {
        Gson gson = new Gson();
        String json = gson.toJson(objArray);
        preferences.edit().putString(key, json).apply();
    }

    public ArrayList<OrderModel> getListOrder(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<ArrayList<OrderModel>>() {}.getType();
            ArrayList<OrderModel> list = gson.fromJson(json, type);
            return (list != null) ? list : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }
}

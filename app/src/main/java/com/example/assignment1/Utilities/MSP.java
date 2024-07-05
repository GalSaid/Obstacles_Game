package com.example.assignment1.Utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.assignment1.Model.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MSP {

    private static MSP msp;
    private Gson gson;
    private SharedPreferences prefs;

    private MSP(Context context) {
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE);
        gson = new Gson();

    }

    public static void init(Context context) {
        if (msp == null) {
            msp = new MSP(context);
        }
    }

    public static MSP getInstance() {
        return msp;
    }

    public void savePlayersList(String key, ArrayList<Player> players) {
        Collections.sort(players);
        if(players.size() == 11) //Save only the top 10
            players.remove(players.size()-1);
        String playersAsJson = gson.toJson(players);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, playersAsJson);
        editor.apply();
    }

    public ArrayList<Player> readPlayersList(String key) {
        String playersAsJson=prefs.getString(key, "");
        if(playersAsJson.isEmpty())
            return new ArrayList<Player>();
        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        ArrayList<Player> players = gson.fromJson(playersAsJson,type);
        return players;
    }

}

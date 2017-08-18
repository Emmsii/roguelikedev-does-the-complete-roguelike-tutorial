package com.mac.rltut.game.entity.creature.stats;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.entity.creature.Player;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:34 AM.
 */
public class Stats {
    
    private HashMap<String, Integer> kills;
    private HashMap<String, Integer> values;

    public Stats(){
        this.kills = new HashMap<String, Integer>();
        this.values = new HashMap<String, Integer>();
    }

    public void incrementValue(String key){
        incrementValue(key, 1);
    }
    
    public void incrementValue(String key, int amount){
        if(!values.containsKey(key)) values.put(key, 0);
        values.put(key, values.get(key) + amount);
    }

    public int getValue(String key){
        if(!values.containsKey(key)){
            Log.warn("Stats do not contain the key [" + key + "]");
            return -1;
        }
        return values.get(key);
    }
    
    public void addKill(String name){
        if(!kills.containsKey(name)) kills.put(name, 0);
        kills.put(name, kills.get(name) + 1);
    }

    public HashMap<String, Integer> kills(){
        return kills;
    }
}

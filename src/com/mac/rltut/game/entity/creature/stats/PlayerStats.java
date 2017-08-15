package com.mac.rltut.game.entity.creature.stats;

import com.mac.rltut.game.entity.creature.Player;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:34 AM.
 */
public class PlayerStats {
    
    private Player player;
    
    private HashMap<String, Integer> kills;
    private int tilesTraveled;
    private int damageReceived;
    private int damageTaken;
    
    private int attackAttempts;
    private int hits;
    private int misses;

    protected PlayerStats() {}
    
    public PlayerStats(Player player){
        this.player = player;
        this.kills = new HashMap<String, Integer>();
    }
    
    public void addKill(String name){
        if(!kills.containsKey(name)) kills.put(name, 0);
        kills.put(name, kills.get(name) + 1);
    }
    
    public void addTileTraveled(){
        tilesTraveled++;
    }
    
    public HashMap<String, Integer> kills(){
        return kills;
    }
}

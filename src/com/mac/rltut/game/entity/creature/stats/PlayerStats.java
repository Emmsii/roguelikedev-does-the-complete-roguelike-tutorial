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
    private int damageDealt;
    
    private int attackAttempts;
    private int hits;
    private int misses;
    private int blocks;

    protected PlayerStats() {}
    
    public PlayerStats(Player player){
        this.player = player;
        this.kills = new HashMap<String, Integer>();
    }
    
    public int tilesTraveled(){
        return tilesTraveled;
    }
    
    public int damageReceived(){
        return damageReceived;
    }
    
    public int damageDealt(){
        return damageDealt;
    }
    
    public int attackAttempts(){
        return attackAttempts;
    }
    
    public int hits(){
        return hits;
    }
    
    public int misses(){
        return misses;
    }
    
    public int blocks(){
        return blocks;
    }
    
    public void addKill(String name){
        if(!kills.containsKey(name)) kills.put(name, 0);
        kills.put(name, kills.get(name) + 1);
    }
    
    public void addTileTraveled(){
        tilesTraveled++;
    }
    
    public void addDamageReceived(int amount){
        damageReceived += amount;
    }
    
    public void addDamageDealt(int amount){
        damageDealt += amount;
    }
    
    public void addAttackAttempt(){
        attackAttempts++;
    }
    
    public void addHit(){
        hits++;
    }
    
    public void addMiss(){
        misses++;
    }
    
    public void addBlock(){
        blocks++;
    }
    
    public HashMap<String, Integer> kills(){
        return kills;
    }
}

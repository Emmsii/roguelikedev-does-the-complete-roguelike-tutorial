package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 12/07/2017 at 10:12 AM.
 */
public class CreatureSpawnProperty {
    
    private final Creature creature;
    private final String spawnLevels;
    private final String spawnTypes;
    private final String spawnNear;
    private final int spawnWeight;
    private final String packSize;
        
    public CreatureSpawnProperty(Creature creature, String spawnLevels, String spawnTypes, String spawnNear, int spawnWeight, String packSize){
        this.creature = creature;
        this.spawnLevels = spawnLevels;
        this.spawnTypes = spawnTypes;
        this.spawnNear = spawnNear;
        this.spawnWeight = spawnWeight;
        this.packSize = packSize;
        
        if(spawnWeight < 0) spawnWeight = 1;
        if(spawnWeight > 100) spawnWeight = 100;
    }
    
    public boolean canSpawnOnLevel(int z){
        if(spawnLevels.trim().equalsIgnoreCase("all")) return true;
        String[] split = spawnLevels.trim().split(",");
        List<Integer> levels = new ArrayList<Integer>();
        
        if(split.length == 0) levels.add(Integer.parseInt(spawnLevels.trim()));
        else {
            for(String s : split) {
                String[] splitRange = s.trim().split("-");
                if (splitRange.length == 2) {
                    int left = Integer.parseInt(splitRange[0].trim());
                    int right = Integer.parseInt(splitRange[1].trim());
                    if (left == right) {
                        if (!levels.contains(left)) levels.add(left);
                    } else {
                        int min = Math.min(left, right);
                        int max = Math.max(left, right);
                        for (int level = min; level <= max; level++) if (!levels.contains(level)) levels.add(level);
                    }
                } else {
                    int newLevel = Integer.parseInt(s.trim());
                    if (!levels.contains(newLevel)) levels.add(newLevel);
                }
            }
        }
        return levels.contains(z + 1);
    }
    
    public boolean canSpawnOnType(String type){
        if(spawnTypes.trim().equalsIgnoreCase("all")) return true;
        String[] split = spawnTypes.trim().split(",");
        List<String> types = new ArrayList<String>();
        
        if(split.length == 0) types.add(spawnTypes.trim());
        else for(String s : split) if(!types.contains(s.trim())) types.add(s.trim());
        return types.contains(type.toLowerCase());
    }
    
    public int packSize(Random random){
        String[] split = packSize.trim().split("-");
        if(split.length < 2) return Integer.parseInt(packSize.trim());
        int left = Integer.parseInt(split[0].trim());
        int right = Integer.parseInt(split[1].trim());
        
        if(left == right) return left;
        else{
            int min = Math.min(left, right);
            int max = Math.max(left, right);
            return MathUtil.range(min, max, random);
        }
    }
    
    public List<String> spawnNear(){
        List<String> result = new ArrayList<String>();
        if(spawnNear.equalsIgnoreCase("all")) return result;
        String[] split = spawnNear.split(",");
        for(String s : split) result.add(s.trim().toLowerCase());
        return result;
    }
    
    public int spawnWeight(){
        return spawnWeight;
    }
    
    public Creature creature(){
        return creature;
    }
    
}

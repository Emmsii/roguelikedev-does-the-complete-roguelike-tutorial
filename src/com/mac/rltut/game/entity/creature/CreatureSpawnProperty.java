package com.mac.rltut.game.entity.creature;

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
    private final String spawnNear;
    private final int spawnWeight;
    private final String packSize;
    
    private final List<Integer> canSpawnAtDepth;
    private final List<Integer> cannotSpawnAtDepth;
    private final List<String> canSpawnAtTypes;
    private final List<String> cannotSpawnAtTypes;
        
    private final char NOT_TOKEN = 33;
    
    public CreatureSpawnProperty(Creature creature, String spawnLevels, String spawnTypes, String spawnNear, int spawnWeight, String packSize){
        this.creature = creature;
        this.spawnNear = spawnNear;
        this.packSize = packSize;
        
        if(spawnWeight < 0) spawnWeight = 1;
        if(spawnWeight > 100) spawnWeight = 100;
        this.spawnWeight = spawnWeight;
        
        this.canSpawnAtDepth = setSpawnDepths(spawnLevels.trim(), false);
        this.cannotSpawnAtDepth = setSpawnDepths(spawnLevels.trim(), true);
        this.canSpawnAtTypes = setSpawnTypes(spawnTypes.trim(), false);
        this.cannotSpawnAtTypes = setSpawnTypes(spawnTypes.trim(), true);
    }
    
    private List<Integer> setSpawnDepths(String spawnLevels, boolean useNotToken){
        List<Integer> levels = new ArrayList<Integer>();
        if(spawnLevels.equalsIgnoreCase("all") && !useNotToken){
            levels.add(-1);
            return levels;
        }
        
        List<String> values = new ArrayList<String>();
        String[] split = spawnLevels.split(",");
        for(String s : split){
            s = s.trim();
            if(useNotToken && s.charAt(0) == NOT_TOKEN) values.add(s.substring(1));
            else if(!useNotToken && s.charAt(0) != NOT_TOKEN) values.add(s);
        }
        
        for(String value : values){
            String[] splitRange = value.split("-");
            if(splitRange.length == 2){
                int left = Integer.parseInt(splitRange[0].trim());
                int right = Integer.parseInt(splitRange[1].trim());
                if(left == right) {
                    if (!levels.contains(left)) levels.add(left);
                }else{
                    int min = Math.min(left, right);
                    int max = Math.max(left, right);
                    for(int level = min; level <= max; level++) if (!levels.contains(level)) levels.add(level);
                }
            }else{
                int newLevel = Integer.parseInt(value);
                if(!levels.contains(newLevel)) levels.add(newLevel);
            }
        }
        return levels;
    } 
    
    private List<String> setSpawnTypes(String input, boolean useNotToken){
        List<String> types = new ArrayList<String>();
        if(input.equalsIgnoreCase("all") && !useNotToken){
            types.add("all");
            return types;
        }
        
        String[] split = input.split(",");
        for(String s : split){
            s = s.trim();
            if(useNotToken && s.charAt(0) == NOT_TOKEN) types.add(s.substring(1));
            else if(!useNotToken && s.charAt(0) != NOT_TOKEN) types.add(s);
        }
        
        return types;
    }
    
    public boolean canSpawnAtDepth(int z){
        if(canSpawnAtDepth.contains(-1)) return true;
        if(canSpawnAtDepth.contains(z) && cannotSpawnAtDepth.contains(z)){
            Log.warn("Creature spawn depth collision!");
            Log.warn("Creature [" + creature.name() + "] will not spawn on level [" + z + "]");
            return false;
        }
        if((canSpawnAtDepth.contains(z) || canSpawnAtDepth.isEmpty()) && !cannotSpawnAtDepth.contains(z)) return true;
        return false;
    }

    public boolean canSpawnOnType(String type){
        type = type.toLowerCase();
        if(canSpawnAtTypes.contains("all")) return true;
        if(canSpawnAtTypes.contains(type) && cannotSpawnAtTypes.contains(type)){
            Log.warn("Creature spawn type collision!");
            Log.warn("Creature [" + creature.name() + "] will not spawn on level types [" + type + "]");
            return false;
        }
        if((canSpawnAtTypes.contains(type) || canSpawnAtTypes.isEmpty()) && !cannotSpawnAtTypes.contains(type)) return true;
        return false;
    }

    public List<String> spawnNear(){
        List<String> result = new ArrayList<String>();
        if(spawnNear.equalsIgnoreCase("all")) return result;
        String[] split = spawnNear.split(",");
        for(String s : split) result.add(s.trim().toLowerCase());
        return result;
    }

    public int packSize(Random random){
        return MathUtil.randomIntFromString(packSize, random);
    }
    
    public int spawnWeight(){
        return spawnWeight;
    }
    
    public Creature creature(){
        return creature;
    }
    
}

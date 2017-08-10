package com.mac.rltut.game.entity.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/07/2017 at 05:00 PM.
 */
public class EntitySpawnProperty {
    
    protected final char NOT_TOKEN = 33;
    
    protected Entity entity;
    protected int chance;
    protected float depthMultiplier;
    
    protected final List<Integer> canSpawnAtDepth;
    protected final List<Integer> cannotSpawnAtDepth;
    protected final List<String> canSpawnAtTypes;
    protected final List<String> cannotSpawnAtTypes;
    
    public EntitySpawnProperty(Entity entity, String spawnLevels, String spawnTypes, int chance, float depthMultiplier){
        this.entity = entity;
        this.chance = chance;
        this.depthMultiplier = depthMultiplier;

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
            Log.warn("Entity spawn depth collision!");
            Log.warn("Entity [" + entity.name() + "] will not spawn on level [" + z + "]");
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
            Log.warn("Creature [" + entity.name() + "] will not spawn on level types [" + type + "]");
            return false;
        }
        if((canSpawnAtTypes.contains(type) || canSpawnAtTypes.isEmpty()) && !cannotSpawnAtTypes.contains(type)) return true;
        return false;
    }
    
    public int chance(){
        return chance;
    }
    
    public int chanceAtDepth(int z){
        int result = (int) (chance + (z * depthMultiplier));
//        Log.debug(entity.name() + " chance at " + z + " = " + result);
        return result;
    }
    
    public Entity entity(){
        return entity;
    }
}

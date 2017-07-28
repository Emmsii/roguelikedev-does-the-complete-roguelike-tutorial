package com.mac.rltut.game.entity.util;

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
public class CreatureSpawnProperty extends EntitySpawnProperty{
    
    private final String spawnNear;
    private final String packSize;
    
    public CreatureSpawnProperty(Creature creature, String spawnLevels, String spawnTypes, String spawnNear, int chance, float depthMultiplier, String packSize) {
        super(creature, spawnLevels, spawnTypes, spawnNear, chance, depthMultiplier);
        this.spawnNear = spawnNear;
        this.packSize = packSize;
    }
    
    public boolean canSpawnAtDepth(int z){
        if(canSpawnAtDepth.contains(-1)) return true;
        if(canSpawnAtDepth.contains(z) && cannotSpawnAtDepth.contains(z)){
            Log.warn("Creature spawn depth collision!");
            Log.warn("Creature [" + entity.name() + "] will not spawn on level [" + z + "]");
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
        
    public Creature creature(){
        return (Creature) entity;
    }
    
}

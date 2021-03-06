package com.mac.rltut.game.entity.util;

import com.mac.rltut.game.entity.creature.Boss;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 15/07/2017 at 11:07 AM.
 */
public class BossSpawnProperty extends CreatureSpawnProperty{
    
    private final int spawnEvery; 
    private final List<String> minions;
    private final String minionCount;
    private final boolean unique;

    public BossSpawnProperty(Boss boss, String spawnLevels, int spawnEvery, String spawnTypes, String spawnNear, int chance, String minions, String minionCount, boolean unique) {
        super(boss, spawnLevels, spawnTypes, spawnNear, chance, 1f, "0", null);
        this.spawnEvery = spawnEvery;
        this.minions = getMinions(minions.trim());
        this.minionCount = minionCount;
        this.unique = unique;
    }

    @Override
    public boolean canSpawnAtDepth(int z) {
        if(spawnEvery == 0) return super.canSpawnAtDepth(z);
        return z % spawnEvery == 0 && z != 0 && super.canSpawnAtDepth(z);
    }
    
    public String minionCount(){
        return minionCount;
    }
    
    public boolean isUnique(){
        return unique;
    }
    
    public List<String> minions(){
        return minions;
    }
    
    private List<String> getMinions(String minions){
        List<String> result = new ArrayList<String>();
        if(minions.equalsIgnoreCase("none")) return result;
        String[] split = minions.split(",");
        for(String s : split) result.add(s.trim());
        return result;
    }
}

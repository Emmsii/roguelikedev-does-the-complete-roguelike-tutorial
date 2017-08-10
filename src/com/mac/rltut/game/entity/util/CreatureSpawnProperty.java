package com.mac.rltut.game.entity.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;

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
    private final String equipment;
    
    public CreatureSpawnProperty(Creature creature, String spawnLevels, String spawnTypes, String spawnNear, int chance, float depthMultiplier, String packSize, String equipment) {
        super(creature, spawnLevels, spawnTypes, chance, depthMultiplier);
        this.spawnNear = spawnNear;
        this.packSize = packSize;
        this.equipment = equipment;
    }
    
    //TODO: Change to list
    public List<Equippable> getEquipment(Random random){
        if(equipment == null) return null;
        List<Equippable> result = new ArrayList<Equippable>();
        String[] split = equipment.trim().split(",");

        Pool<Equippable> pool = new Pool<Equippable>(random);
        pool.add(new Equippable(null, null, null, null), 100);
        
        for(String s : split){
            String[] equipmentSplit = s.split(":");
            if(equipmentSplit.length != 2){
                Log.warn("Invalid creature equipment sytnax [" + s + "]");
                continue;
            }
            String name = equipmentSplit[0].trim().toLowerCase();
            int chance = Integer.parseInt(equipmentSplit[1].trim());
            if(!Codex.items.containsKey(name)){
                Log.warn("Unknown item [" + name + "]");
                continue;
            }
            Item item = (Item) Codex.items.get(name).entity();
            if(item instanceof Equippable){
                if(chance == 100) result.add((Equippable) item);
                else pool.add((Equippable) item, chance);
            }
            else Log.warn("Creature equipment is not equippable [" + name + "]");
        }
        
        if(pool.isEmpty()){
            Log.warn("Could not find equipment to give creature.");
            return null;
        }
        Equippable item = pool.get();
        if(item.name() != null) result.add(item);
        return result;
    }
    
    public boolean hasEquipment(){
        return equipment != null;
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

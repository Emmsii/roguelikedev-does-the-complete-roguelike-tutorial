package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Boss;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.CreatureSpawnProperty;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 11/07/2017 at 03:24 PM.
 */
public class CreatureLoader extends DataLoader{
    
    public CreatureLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading creatures...");
        for(DataObject obj : data){
            if(obj.type().equalsIgnoreCase("creature")){
                String name = obj.getString("name");
                Sprite sprite = Sprite.get(obj.getString("sprite"));

                String spawnLevels = obj.hasToken("spawn_levels") ? obj.getString("spawn_levels") : "all";
                String spawnTypes = obj.hasToken("spawn_types") ? obj.getString("spawn_types") : "all";
                String spawnNear = obj.hasToken("spawn_near") ? obj.getString("spawn_near") : "all";
                int spawnWeight = obj.hasToken("spawn_weight") ? obj.getInt("spawn_weight") : 100;
                String packSize = obj.hasToken("pack_size") ? obj.getString("pack_size") : "0";
                
                Creature creature = new Creature(name, sprite);
                CreatureSpawnProperty spawnProperty = new CreatureSpawnProperty(creature, spawnLevels, spawnTypes, spawnNear, spawnWeight, packSize);
                Codex.creatures.put(name, spawnProperty);
                
                //TODO: This is bad, duplicating code. Fix me later.
            }else if(obj.type().equalsIgnoreCase("boss")){
                String name = obj.getString("name");
                Sprite sprite = Sprite.get(obj.getString("sprite"));

                String spawnTypes = obj.hasToken("spawn_types") ? obj.getString("spawn_types") : "all";
                String spawnNear = obj.hasToken("spawn_near") ? obj.getString("spawn_near") : "all";
                int spawnWeight = obj.hasToken("spawn_weight") ? obj.getInt("spawn_weight") : 100;
                int size = obj.hasToken("size") ? obj.getInt("size") : 1;

                String minions = obj.hasToken("minions") ? obj.getString("minions") : "none";
                String minionCount = obj.hasToken("minion_count") ? obj.getString("minion_count") : "0";

                Boss boss = new Boss(name, sprite, size);
                CreatureSpawnProperty spawnProperty = new CreatureSpawnProperty(boss, "all", spawnTypes, spawnNear, spawnWeight, "0");
                Codex.bosses.put(name, spawnProperty);
            }
        }
        
        Log.debug("Loaded " + Codex.creatures.size() + " creatures.");
    }
}

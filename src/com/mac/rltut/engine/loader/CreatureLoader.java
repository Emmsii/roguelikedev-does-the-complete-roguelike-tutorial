package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Boss;
import com.mac.rltut.game.entity.util.BossSpawnProperty;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.util.CreatureSpawnProperty;
import com.mac.rltut.game.entity.item.util.DropTable;

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
            
            CreatureSpawnProperty spawnProperty = null;

            String name = obj.getString("name");
            String description = obj.getString("description");
            Sprite sprite = Sprite.get(obj.getString("sprite"));
            int hp = obj.getInt("hp");
            int mana = obj.hasToken("mana") ? obj.getInt("mana") : 0;
            int manaRegenAmount= obj.hasToken("mana_regen_amount") ? obj.getInt("mana_regen_amount") : 0;
            int manaRegenSpeed= obj.hasToken("mana_regen_speed") ? obj.getInt("mana_regen_speed") : -1;
            int strength = obj.getInt("strength");
            int defense = obj.getInt("defense");
            int accuracy = obj.getInt("accuracy");
            int intelligence = obj.hasToken("intelligence") ? obj.getInt("intelligence") : 0;
            int vision = obj.getInt("vision");
            String ai = obj.hasToken("ai") ? obj.getString("ai") : "aggressive";

            String spawnLevels = obj.hasToken("spawn_levels") ? obj.getString("spawn_levels") : "all";
            String spawnTypes = obj.hasToken("spawn_types") ? obj.getString("spawn_types") : "all";
            String spawnNear = obj.hasToken("spawn_near") ? obj.getString("spawn_near") : "all";
            int spawnWeight = obj.hasToken("spawn_weight") ? obj.getInt("spawn_weight") : 100;
            float depthMultiplier = obj.hasToken("depth_multiplier") ? obj.getFloat("depth_multiplier") : 1f;
            String packSize = obj.hasToken("pack_size") ? obj.getString("pack_size") : "0";
            int size = obj.hasToken("size") ? obj.getInt("size") : 1;

            String[] flags = obj.hasToken("flags") ? parseFlags(obj.getString("flags")) : null;

            DropTable drops = obj.hasToken("drops") ? parseDropTable(obj.getString("drops")) : null;
            
            if(obj.isType("creature")) {
                Creature creature = new Creature(name, description, sprite, size, ai);
                spawnProperty = new CreatureSpawnProperty(creature, spawnLevels, spawnTypes, spawnNear, spawnWeight, depthMultiplier, packSize);
            }else if(obj.isType("boss")) {
                int spawnEvery = obj.hasToken("spawn_every") ? obj.getInt("spawn_every") : 0;
                String minions = obj.hasToken("minions") ? obj.getString("minions") : "none";
                String minionCount = obj.hasToken("minion_count") ? obj.getString("minion_count") : "0";
                boolean unique = obj.hasToken("unique") && obj.getInt("unique") == 1;
                
                Boss boss = new Boss(name, description, sprite, size);
                spawnProperty = new BossSpawnProperty(boss, spawnLevels, spawnEvery, spawnTypes, spawnNear, spawnWeight, minions, minionCount, unique);
            }else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }

            if(spawnProperty != null) {
                spawnProperty.creature().setStats(hp, mana, manaRegenAmount, manaRegenSpeed, strength, defense, accuracy, intelligence, vision, drops);
                if (flags != null) for(String s : flags) spawnProperty.creature().addFlag(s);
                Codex.creatures.put(name.toLowerCase(), spawnProperty);
            }else Log.warn("Cannot add null creature into codex. [" + name + "]");
        }
        
        Log.debug("Loaded " + Codex.creatures.size() + " creatures.");
    }
    
    private String[] parseFlags(String input){
        String[] split = input.split(",");
        for(int i = 0; i < split.length; i++) split[i] = split[i].toLowerCase().trim();
        return split;
    }
    
    private DropTable parseDropTable(String input){
        DropTable drops = new DropTable();
        String[] split = input.trim().split(",");
        for(String s : split) drops.addDrop(s.trim());
        return drops;
    }
}

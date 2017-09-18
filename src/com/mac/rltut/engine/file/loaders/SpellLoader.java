package com.mac.rltut.engine.file.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.*;
import com.mac.rltut.game.effects.spells.Spell;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 04:07 PM.
 */
public class SpellLoader extends DataLoader{
    
    public SpellLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading spells...");

        for (DataObject obj : data) {
            if(obj.isType("spell")) {
                String name = obj.getString("name");
                int cost = obj.getInt("cost");
                Effect effect = parseEffect(obj.getString("effect"));

                Spell spell = new Spell(name, effect, cost);
                Codex.spells.put(name.toLowerCase().trim(), spell);
            }else{
                Log.warn("Unknown spell data type [" + obj.type() + "]");
            }
        }
        
        Log.debug("Loaded " + Codex.spells.size() + " spells.");
    }

    private Effect parseEffect(String input){
        if(input == null) return null;
        String[] split = input.trim().toLowerCase().split(":");
        
        String name = split[0];
        int amount = split.length > 1 ? Integer.parseInt(split[1]) : 0;
        int duration = split.length > 2 ? Integer.parseInt(split[2]) : 1;
        float chance = split.length > 3 ? Float.parseFloat(split[3]) : 1f;
        
        switch (name){
            case "blind": return new Blind(duration, chance);
            case "burn": return new Burn(amount, duration, chance);
            case "freeze": return new Freeze(duration, chance);
            case "heal": return new Heal(amount, chance);
            case "health_regen": return new HealthRegen(amount, duration, chance);
            case "mana_regen": return new ManaRegen(amount, duration, chance);
            case "night_vision": return new NightVision(amount, duration, chance);
            case "poison": return new Poison(amount, duration, chance);
            case "rage": return new Rage(amount, duration,chance);
            default:
                Log.warn("Unknown effect [" + name + "]");
                return null;
        }
    }
}

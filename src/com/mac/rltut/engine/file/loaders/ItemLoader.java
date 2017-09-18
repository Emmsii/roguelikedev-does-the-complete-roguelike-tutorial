package com.mac.rltut.engine.file.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.Heal;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.item.*;
import com.mac.rltut.game.entity.util.ItemSpawnProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 03:03 PM.
 */
public class ItemLoader extends DataLoader {
    
    public ItemLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading items...");
        for(DataObject obj : data) {
            Item item = null;
            ItemSpawnProperty spawnProperty = null;

            String name = obj.getString("name");
            String description = obj.getString("description");
            Sprite sprite = Sprite.get(obj.getString("sprite"));
            String spawnLevels = obj.hasToken("spawn_levels") ? obj.getString("spawn_levels") : "all";
            int spawnChance = obj.hasToken("spawn_chance") ? obj.getInt("spawn_chance") : -1;
            float depthMultiplier = obj.hasToken("depth_multiplier") ? obj.getFloat("depth_multiplier") : 0f;

            String[] flags = obj.hasToken("flags") ? parseStringArray(obj.getString("flags")) : null;

            if (obj.isType("item")) item = new Item(name, description, sprite);
            else if (obj.isType("item_stack")) {
                String amount = obj.getString("amount");
                item = new ItemStack(name, description, sprite, amount, 1);
            } else if (obj.isType("consumable")) {
                Effect effect = null;
                if (obj.hasToken("heal")) effect = new Heal(obj.getInt("heal"), 1f);
                String action = obj.hasToken("action") ? obj.getString("action") : "fumble";
                item = new Consumable(name, description, sprite, action, effect);
            } else if (obj.isType("potion")) {
                item = new Potion(name, description, sprite, null);
            }else if(obj.isType("ammo")) {
                String amount = obj.getString("amount");
                item = new Ammo(name, description, sprite, amount, 1);
            }else if(obj.isType("spellbook")){
                List<Spell> spells = parseSpells(obj.getString("spells"));
                item = new Spellbook(name, description, sprite, spells);
            }else if(obj.isType("equippable")){
                String slot = obj.getString("slot");
                Equippable e = new Equippable(name, description, sprite, getSlot(slot));
                e.setStrengthBonus(obj.hasToken("strength") ? obj.getInt("strength") : 0);
                e.setDefenseBonus(obj.hasToken("defense") ? obj.getInt("defense") : 0);
                e.setAccuracyBonus(obj.hasToken("accuracy") ? obj.getInt("accuracy") : 0);
                e.setIntelligenceBonus(obj.hasToken("intelligence") ? obj.getInt("intelligence") : 0);
                e.setManaRegenAmountBonus(obj.hasToken("mana_amount") ? obj.getInt("mana_amount") : 0);
                e.setManaRegenSpeedBonus(obj.hasToken("mana_speed") ? obj.getInt("mana_speed") : 0);
                e.setDamage(obj.hasToken("damage") ? obj.getString("damage") : "0");
                e.setRangedDamage(obj.hasToken("damage_ranged") ? obj.getString("damage_ranged") : null);
                e.setBlockedSlot(obj.hasToken("blocked_slot") ? obj.getString("blocked_slot") : null);
                if(e.slot() == EquipmentSlot.WEAPON && obj.hasToken("ammo")) e.setAmmoType(obj.getString("ammo"));
                item = e;
            }else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }
            
            spawnProperty = new ItemSpawnProperty(item, spawnLevels, "all", spawnChance, depthMultiplier);
            if(flags != null) for(String s : flags) spawnProperty.entity().addFlag(s);
            if(item != null) Codex.items.put(name, spawnProperty);
            else Log.warn("Cannot add null item into codex. [" + name + "]");
        }
        Log.debug("Loaded " + Codex.items.size() + " items.");
    }

    private List<Spell> parseSpells(String input){
        if(input == null) return null;
        List<Spell> spells = new ArrayList<Spell>();

        String[] split = input.split(",");

        for(String s : split){
            if(!Codex.spells.containsKey(s.trim())){
                Log.warn("Unknown spell [" + s.trim() + "]");
                continue;
            }
            spells.add(Codex.spells.get(s.trim()));
        }
        return spells;
    }

    private EquipmentSlot getSlot(String slot){
        return EquipmentSlot.valueOf(slot.trim().toUpperCase());
    }
}

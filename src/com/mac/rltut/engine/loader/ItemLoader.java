package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.item.Food;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.entity.item.equipment.Equippable;

import java.io.IOException;

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
        for(DataObject obj : data){
            
            Item item = null;
            
            String name = obj.getString("name");
            String description = obj.getString("description");
            Sprite sprite = Sprite.get(obj.getString("sprite"));
            int spawnChance = obj.hasToken("spawn_chance") ? obj.getInt("spawn_chance") : 100;
            
            if(obj.isType("item")) item = new Item(name, description, sprite, spawnChance);
            else if(obj.isType("item_stack")){
                String amount = obj.getString("amount");
                item = new ItemStack(name, description, sprite, spawnChance, amount, 1);
            }else if(obj.isType("food")){
                int healAmount = obj.getInt("heal");
                item = new Food(name, description, sprite, spawnChance, healAmount);
            }else if(obj.isType("equippable")){
                String location = obj.getString("location");
                Equippable e = new Equippable(name, description, sprite, spawnChance, location);
                e.setStrengthBonus(obj.hasToken("strength") ? obj.getInt("strength") : 0);
                e.setDefenseBonus(obj.hasToken("defense") ? obj.getInt("defense") : 0);
                e.setAccuracyBonus(obj.hasToken("accuracy") ? obj.getInt("accuracy") : 0);
                e.setIntelligenceBonus(obj.hasToken("intelligence") ? obj.getInt("intelligence") : 0);
                e.setManaRegenAmountBonus(obj.hasToken("mana_amount") ? obj.getInt("mana_amount") : 0);
                e.setManaRegenSpeedBonus(obj.hasToken("mana_speed") ? obj.getInt("mana_speed") : 0);
                e.setDamage(obj.hasToken("damage") ? obj.getString("damage") : "0");
                item = e;
            }else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }
            
            if(item != null) Codex.items.put(name, item);
            else Log.warn("Cannot add null item into codex. [" + name + "]");
        }
        Log.debug("Loaded " + Codex.items.size() + " items.");
    }
}

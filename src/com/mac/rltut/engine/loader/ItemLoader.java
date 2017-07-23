package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.item.Food;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;

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
            if(obj.type().equalsIgnoreCase("ITEM")){
                String name = obj.getString("name");
                String description = obj.getString("description");
                Sprite sprite = Sprite.get(obj.getString("sprite"));

                Item item = new Item(name, description, sprite);
                Codex.items.put(name, item);
            }else if(obj.type().equalsIgnoreCase("ITEM_STACK")){
                String name = obj.getString("name");
                String description = obj.getString("description");
                Sprite sprite = Sprite.get(obj.getString("sprite"));

                ItemStack stack = new ItemStack(name, description, sprite, 0);
                Codex.items.put(name, stack);
            }else if(obj.type().equalsIgnoreCase("FOOD")){
                String name = obj.getString("name");
                String description = obj.getString("description");
                Sprite sprite = Sprite.get(obj.getString("sprite"));
                
                Food food = new Food(name, description, sprite);
                Codex.items.put(name, food);
            }
        }
        Log.debug("Loaded " + Codex.items.size() + " items.");
    }
}

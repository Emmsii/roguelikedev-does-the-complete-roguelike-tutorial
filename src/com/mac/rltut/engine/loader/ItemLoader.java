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
            
            Item item = null;
            
            String name = obj.getString("name");
            String description = obj.getString("description");
            Sprite sprite = Sprite.get(obj.getString("sprite"));
            
            if(obj.isType("item")) item = new Item(name, description, sprite);
            else if(obj.isType("item_stack")) item = new ItemStack(name, description, sprite, 1);
            else if(obj.isType("food"))item = new Food(name, description, sprite);
            else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }
            
            if(item != null) Codex.items.put(name, item);
            else Log.warn("Cannot add null item into codex. [" + name + "]");
        }
        Log.debug("Loaded " + Codex.items.size() + " items.");
    }
}

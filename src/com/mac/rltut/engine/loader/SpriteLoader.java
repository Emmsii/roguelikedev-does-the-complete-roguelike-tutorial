package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.graphics.Spritesheet;
import com.mac.rltut.engine.parser.DataObject;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/07/2017 at 09:31 AM.
 */
public class SpriteLoader extends DataLoader{
    
    public SpriteLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading sprites...");
        loadDefaults();
        
        for(DataObject obj : data){
            if(obj.type().equalsIgnoreCase("sprite")) {
                String name = obj.getString("name");
                int x = obj.getInt("x");
                int y = obj.getInt("y");
                
                int size = obj.hasToken("size") ? obj.getInt("size") : defaults.getInt("size");
                int tileIndexSize = obj.hasToken("tile_index_size") ? obj.getInt("tile_index_size") : defaults.getInt("tile_index_size");
                
                String sheetName = obj.hasToken("sheet") ? obj.getString("sheet") : defaults.getString("sheet");
                Spritesheet sheet = Spritesheet.get(sheetName);
                
                Sprite newSprite = sheet.cutSprite(x, y, size, size, tileIndexSize);
                Sprite.add(name, newSprite);
            }
        }
        
        Log.debug("Loaded " + Sprite.sprites.size() + " sprites.");
    }
}

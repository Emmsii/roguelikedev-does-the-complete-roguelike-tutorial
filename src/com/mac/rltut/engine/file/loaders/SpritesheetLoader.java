package com.mac.rltut.engine.file.loaders;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Font;
import com.mac.rltut.engine.graphics.Spritesheet;
import com.mac.rltut.engine.parser.DataObject;

import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/07/2017 at 09:36 AM.
 */
public class SpritesheetLoader extends DataLoader{
    
    public SpritesheetLoader(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public void load() {
        Log.debug("Loading spritesheets...");
        for(DataObject obj : data){
            
            Spritesheet sheet = null;
            
            String name = obj.getString("name");
            String fileName = obj.getString("file_name");
            
            if(obj.isType("spritesheet")) sheet = new Spritesheet(fileName);
            else if(obj.isType("font")){
                int charWidth = obj.getInt("char_width");
                int charHeight = obj.getInt("char_height");
                sheet = new Font(fileName, charWidth, charHeight);
            }else{
                Log.error("Unknown object type [" + obj.type() + "]");
                continue;
            }
            
            if(sheet != null) Spritesheet.add(name, sheet);
            else Log.warn("Cannot load null spritesheet. [" + name + "]");
        }
        
        Log.debug("Loaded " + Spritesheet.sheets.size() + " spritesheets.");
    }
}

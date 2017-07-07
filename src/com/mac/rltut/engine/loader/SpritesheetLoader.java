package com.mac.rltut.engine.loader;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Font;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.graphics.Spritesheet;
import com.mac.rltut.engine.parser.DataObject;

import java.io.File;
import java.io.IOException;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/07/2017 at 09:36 AM.
 */
public class SpritesheetLoader extends DataLoader{
    
    public SpritesheetLoader(File file) throws IOException {
        super(file);
    }

    @Override
    public void load() {
        Log.debug("Loading spritesheets...");
        for(DataObject obj : data){
            if(obj.type().equalsIgnoreCase("spritesheet")){
                String name = obj.getString("name");
                String fileName = obj.getString("file_name");
                Spritesheet newSheet = new Spritesheet(fileName);
                Spritesheet.add(name, newSheet);
            }else if(obj.type().equalsIgnoreCase("font")){
                String name = obj.getString("name");
                String fileName = obj.getString("file_name");
                int charWidth = obj.getInt("char_width");
                int charHeight = obj.getInt("char_height");
                Font newFont = new Font(fileName, charWidth, charHeight);
                Spritesheet.add(name, newFont);
            }
        }
        
        Log.debug("Loaded " + Spritesheet.sheets.size() + " spritesheets.");
    }
}

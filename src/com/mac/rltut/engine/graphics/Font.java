package com.mac.rltut.engine.graphics;

import com.esotericsoftware.minlog.Log;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:55 AM.
 */
public class Font extends Spritesheet{
    
    private int charWidth, charHeight;
    private Sprite[] chars;

    public Font() {}
    
    public Font(String name, int charWidth, int charHeight) {
        super(name);
        this.charWidth = charWidth;
        this.charHeight = charHeight;
        this.chars = new Sprite[(width / charWidth) * (height / charHeight)];

        loadChars();
    }

    private void loadChars(){
        for(int y = 0; y < height / charHeight; y++){
            for(int x = 0; x < width / charWidth; x++){
                chars[x + y * (width / charWidth)] = cutSprite("name_" + x + "_" + y, x, y, charWidth, charHeight, charWidth);
            }
        }
    }

    public Sprite charAt(int i){
        if(i < 0 || i >= chars.length){
            Log.warn("Invalid character [" + i + "]");
            return null;
        }
        return chars[i];
    }
}

package com.mac.rltut.engine.graphics;

import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Sprite extends Bitmap{
    
    public static HashMap<String, Sprite> sprites = new HashMap<>();
    public static final List<Sprite> fog = Sprite.loadFogSprites(Spritesheet.get("fog"), 4, 4, 8);

    public Sprite(int width, int height, int[] pixels){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    private static List<Sprite> loadFogSprites(Spritesheet sheet, int w, int h, int size){
        List<Sprite> sprites = new ArrayList<Sprite>();
        for(int y = 0; y < h; y++) for(int x = 0; x < w; x++) sprites.add(sheet.cutSprite(x, y, size, size, size));
        return sprites;
    }
    
    public static Sprite getFogSprite(byte bit){
        if(bit < 0 || bit > 15) return fog.get(15);
        return fog.get(bit);
    }
    
    public static Sprite get(String name){
        return sprites.get(name);
    }
    
    public static void add(String name, Sprite sprite){
        sprites.put(name, sprite);
    }
}
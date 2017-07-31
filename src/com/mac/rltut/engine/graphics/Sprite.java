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

    protected String name;
    
    public Sprite() {}
    
    public Sprite(String name, int width, int height, int[] pixels){
        super(pixels, width, height);
        this.name = name;
    }

    private static List<Sprite> loadFogSprites(Spritesheet sheet, int w, int h, int size){
        List<Sprite> sprites = new ArrayList<Sprite>();
        for(int y = 0; y < h; y++) for(int x = 0; x < w; x++) sprites.add(sheet.cutSprite("fog_" + x + "_" + y, x, y, size, size, size));
        return sprites;
    }
    
    public static Sprite getFogSprite(byte bit){
        if(bit < 0 || bit > 15) return fog.get(15);
        return fog.get(bit);
    }
    
    public static Sprite get(String name){
        if(!sprites.containsKey(name)) Log.warn("Unknown sprite [" + name + "]");
        return sprites.get(name);
    }
    
    public static void add(String name, Sprite sprite){
        sprites.put(name, sprite);
    }
    
    public String name(){
        return name;
    }
}
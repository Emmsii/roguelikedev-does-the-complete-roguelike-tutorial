package com.mac.rltut.engine.graphics;

import com.esotericsoftware.minlog.Log;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:55 AM.
 */
public class Spritesheet extends Bitmap {

    public static HashMap<String, Spritesheet> sheets = new HashMap<>();

    private String name;

    public Spritesheet() {}
    
    public Spritesheet(String name){
        this.name = name;
        load();
    }

    public Sprite cutSprite(String name, int xp, int yp, int w, int h, int tileIndexSize){
        xp *= tileIndexSize;
        yp *= tileIndexSize;
        int[] spritePixels = new int[w * h];
        for(int y = 0; y < h; y++){
            int ya = y + yp;
            for(int x = 0; x < w; x++){
                int xa = x + xp;
                spritePixels[x + y * w] = pixels[xa + ya * width];
            }
        }
        return new Sprite(name, w, h, spritePixels);
    }

    private void load(){
        try {
            InputStream in = Spritesheet.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            BufferedImage image = ImageIO.read(in);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Spritesheet get(String name) {
        if(!sheets.containsKey(name)){
            Log.warn("Spritesheet [" + name + "] does not exist.");
            return null;
        }
        return sheets.get(name);
    }
    
    public static void add(String name, Spritesheet sheet){
        sheets.put(name, sheet);
    }
}

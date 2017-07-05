package com.mac.rltut.engine.graphics;

import com.esotericsoftware.minlog.Log;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:55 AM.
 */
public class Spritesheet extends Bitmap{

    public static final Spritesheet textures = new Spritesheet("minirogue-all");
    public static final Spritesheet fog = new Spritesheet("fog");

    private String name;

    public Spritesheet(String name){
        this.name = name;
        load();
    }

    public Sprite cutSprite(int xp, int yp, int w, int h, int tileSize){
        xp *= tileSize;
        yp *= tileSize;
        int[] spritePixels = new int[w * h];
        for(int y = 0; y < h; y++){
            int ya = y + yp;
            for(int x = 0; x < w; x++){
                int xa = x + xp;
                spritePixels[x + y * w] = pixels[xa + ya * width];
            }
        }
        return new Sprite(w, h, spritePixels);
    }

    private void load(){
        try {
            InputStream in = Spritesheet.class.getClassLoader().getResourceAsStream("textures/" + name + ".png");
            BufferedImage image = ImageIO.read(in);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
            Log.debug("Loaded spritesheet [" + name + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.mac.rltut.engine.graphics;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Sprite extends Bitmap{

    public static final Sprite player = Spritesheet.textures.cutSprite(5, 5, 8, 8, 8);

    public Sprite(int width, int height, int[] pixels){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }
}
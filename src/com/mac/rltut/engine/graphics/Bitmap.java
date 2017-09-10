package com.mac.rltut.engine.graphics;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Bitmap {

    protected int width, height;
    protected int[] pixels;

    public Bitmap() {}

    public Bitmap(int[] pixels, int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int width(){
        return width;
    }

    public int height(){
        return height;
    }

    public int pixel(int x, int y){
        if(x < 0 || y < 0 || x >= width || y >= height) return 0xff000000;
        return pixels[x + y * width];
    }

    public int[] pixels(){
        return pixels;
    }
}


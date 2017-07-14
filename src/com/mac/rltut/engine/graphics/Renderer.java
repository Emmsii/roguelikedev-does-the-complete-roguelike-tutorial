package com.mac.rltut.engine.graphics;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.util.Colors;

import java.awt.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Renderer {

    public static final int FLIP_SPRITE = 1;
    public static final int DARKEN_SPRITE = 2;
    
    private int defaultFontColor = 0xffffff;
    private int defaultBackgroundColor = 0x111111;
    
    private final int width, height;
    private int[] pixels;

    public Renderer(int width, int height){
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public void renderPixel(int color, int xp, int yp){
        if(!inBounds(xp, yp)) return;
        pixels[xp + yp * width] = color;
    }

    public void renderSprite(Sprite sprite, int xp, int yp){
        renderSprite(sprite, xp, yp, 0);    
    }

    public void renderSprite(Sprite sprite, int xp, int yp, int flags){
        renderSpritePrecise(sprite, xp * Engine.instance().tileSize(), yp * Engine.instance().tileSize(), flags);
    }
    
    public void renderSpritePrecise(Sprite sprite, int xp, int yp, int flags){
        for(int y = 0; y < sprite.height; y++){
            int ya = y + yp;
            for(int x = 0; x < sprite.width; x++){
                int xa = x + xp;
                int pixelColor = sprite.pixel((flags & FLIP_SPRITE) == FLIP_SPRITE ? sprite.width - x : x, y);
                if(pixelColor == 0xff000000) pixelColor = defaultBackgroundColor;
                if((flags & DARKEN_SPRITE) == DARKEN_SPRITE) pixelColor = Colors.darken(pixelColor);
                renderPixel(pixelColor, xa, ya);
            }
        }
    }

    public void renderSpriteTint(Sprite sprite, int xp, int yp, int color){
        xp *= Engine.instance().tileSize();
        yp *= Engine.instance().tileSize();
        for(int y = 0; y < sprite.height; y++){
            int ya = y + yp;
            for(int x = 0; x < sprite.width; x++){
                int xa = x + xp;
                int pixelColor = sprite.pixel(x, y);
                if(pixelColor == 0xffffffff) renderPixel(color, xa, ya);
                else renderPixel(defaultBackgroundColor, xa, ya);
            }
        }
    }

    public void darkenSprite(int xp, int yp){
        darkenSprite(xp, yp, Colors.DARKEN_FACTOR);
    }
    
    public void darkenSprite(int xp, int yp, float factor){
        int size = Engine.instance().tileSize();
        xp *= size;
        yp *= size;
        
        for(int y = yp; y < yp + size; y++){
            for(int x = xp; x < xp + size; x++){
                if(!inBounds(x, y)) continue;
                int pixelColor = pixels[x + y * width];
                if(pixelColor == 0xff000000) continue;
                pixels[x + y * width] = Colors.darken(pixelColor, factor);
            }
        }
    }

    public void write(String text, int xp, int yp){
        write(text, xp, yp, defaultFontColor);
    }

    public void writeCenter(String text, int xp, int yp){
        writeCenter(text, xp, yp, defaultFontColor);
    }
    
    public void writeCenter(String text, int xp, int yp, int color){
        write(text, xp - (text.length() / 2), yp, color);
    }
    
    public void write(String text, int xp, int yp, int color){
        if(text == null || text.length() == 0) return;

        for(int i = 0; i < text.length(); i++){
            if(xp + i < 0 || xp + i >= width) continue;
            int c = text.charAt(i);
            renderSpriteTint(Engine.instance().currentFont().charAt(c), xp + i, yp, color);
        }
    }

    public void clear(){
        for(int i = 0; i < pixels.length; i++) pixels[i] = defaultBackgroundColor;
    }

    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public int[] pixels(){
        return pixels;
    }
    
    public void setDefaultFontColor(int color){
        this.defaultFontColor = color;
    }
}

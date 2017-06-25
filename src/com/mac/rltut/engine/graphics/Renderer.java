package com.mac.rltut.engine.graphics;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Renderer {

    public static final int DEFAULT_SPRITE_SIZE = 8;

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
        renderSprite(sprite, xp, yp, false);
    }

    public void renderSprite(Sprite sprite, int xp, int yp, boolean flip){
        renderSpritePrecise(sprite, xp * DEFAULT_SPRITE_SIZE, yp * DEFAULT_SPRITE_SIZE, flip);
    }

    public void renderSpritePrecise(Sprite sprite, int xp, int yp){
        renderSpritePrecise(sprite, xp, yp, false);
    }

    public void renderSpritePrecise(Sprite sprite, int xp, int yp, boolean flip){
        for(int y = 0; y < sprite.height; y++){
            int ya = y + yp;
            for(int x = 0; x < sprite.width; x++){
                int xa = x + xp;
                renderPixel(sprite.pixel(flip ? sprite.width - x : x, y), xa, ya);
            }
        }
    }

    public void renderSpriteTint(Sprite sprite, int xp, int yp, int color){
        xp *= DEFAULT_SPRITE_SIZE;
        yp *= DEFAULT_SPRITE_SIZE;
        for(int y = 0; y < sprite.height; y++){
            int ya = y + yp;
            for(int x = 0; x < sprite.width; x++){
                int xa = x + xp;
                int pixelColor = sprite.pixel(x, y);
                if(pixelColor != 0xff000000) renderPixel(color, xa, ya);

            }
        }
    }

    public void write(String text, int xp, int yp){
        write(text, xp, yp, 0xffffff);
    }

    public void write(String text, int xp, int yp, int color){
        if(text == null || text.length() == 0) return;

        for(int i = 0; i < text.length(); i++){
            int c = text.charAt(i);
            renderSpriteTint(Font.font.charAt(c), xp + i, yp, color);
        }
    }

    public void clear(){
        for(int i = 0; i < pixels.length; i++) pixels[i] = 0;
    }

    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public int[] pixels(){
        return pixels;
    }
}

package com.mac.rltut.game.screen;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:57 AM.
 */
public abstract class Screen {

    protected final String letters = "abcdefghijklmnopqrstuvwxyz";
    
    protected String title;
    protected final int x, y;
    protected final int width, height;

    public Screen(){
        this(null);
    }

    public Screen(String title){
        this(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), title);
    }

    public Screen(int x, int y, int width, int height, String title){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public abstract Screen input(KeyEvent key);
    public abstract void render(Renderer renderer);

    protected void renderBorderFill(Renderer renderer){
        renderBox(0, 0, width, height, true, renderer);
        if(title != null && title.trim().length() != 0) renderer.write(" " + title + " ", this.x + 2, this.y);
    }
    
    protected void renderBorder(Renderer renderer){
        renderBox(0, 0, width, height, false, renderer);
        if(title != null && title.trim().length() != 0) renderer.write(" " + title + " ", this.x + 2, this.y);
    }
    
    protected void renderBox(int x, int y, int w, int h, boolean fill, Renderer renderer){
        for(int yp = 0; yp < h; yp++){
            for(int xp = 0; xp < w; xp++){
                int xa = xp + this.x + x;
                int ya = yp + this.y + y;

                
                if(fill) renderer.renderSprite(Sprite.get("empty"), xa, ya);
                
                if(xp == 0 || xp == w - 1) renderer.renderSprite(Sprite.get("ui_border_ver"), xa, ya);
                else if(yp == 0 || yp == h - 1) renderer.renderSprite(Sprite.get("ui_border_hor"), xa, ya);
                if(xp == 0 && yp == 0) renderer.renderSprite(Sprite.get("ui_border_tl"), xa, ya);
                else if(xp == w - 1 && yp == 0) renderer.renderSprite(Sprite.get("ui_border_tr"), xa, ya);
                else if(xp == 0 && yp == h - 1) renderer.renderSprite(Sprite.get("ui_border_bl"), xa, ya);
                else if(xp == w - 1 && yp == h - 1) renderer.renderSprite(Sprite.get("ui_border_br"), xa, ya);
            }
        }
    }
    
    protected void renderBar(int xp, int yp, int w, int color, Renderer renderer){
        for(int i = 0; i < w; i++){
            renderer.renderBox(xp + i, yp, color);
        }
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public int x(){
        return x;
    }
    
    public int y(){
        return y;
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }
}
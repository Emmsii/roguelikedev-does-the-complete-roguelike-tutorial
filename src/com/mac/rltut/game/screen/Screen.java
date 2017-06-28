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

    public abstract Screen input(KeyEvent e);
    public abstract void render(Renderer renderer);

    protected void renderBorder(Renderer renderer){
        for(int yp = 0; yp < height; yp++){
            for(int xp = 0; xp < width; xp++){
                int xa = xp + this.x;
                int ya = yp + this.y;

                if(xp == 0 || xp == width - 1) renderer.renderSprite(Sprite.uiBorderVer, xa, ya);
                else if(yp == 0 || yp == height - 1) renderer.renderSprite(Sprite.uiBorderHor, xa, ya);
                if(xp == 0 && yp == 0) renderer.renderSprite(Sprite.uiBorderTL, xa, ya);
                else if(xp == width - 1 && yp == 0) renderer.renderSprite(Sprite.uiBorderTR, xa, ya);
                else if(xp == 0 && yp == height - 1) renderer.renderSprite(Sprite.uiBorderBL, xa, ya);
                else if(xp == width - 1 && yp == height - 1) renderer.renderSprite(Sprite.uiBorderBR, xa, ya);
            }
        }
    }
    
    public void setTitle(String title){
        this.title = title;
    }
}
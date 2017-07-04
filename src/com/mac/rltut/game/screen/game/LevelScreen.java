package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:29 AM.
 */
public class LevelScreen extends Screen{
    
    private World world;
    
    private int xPos, yPos, zPos;
    
    public LevelScreen(int x, int y, int width, int height, World world){
        super(x, y, width, height, null);
        this.world = world;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        
        for(int ya = 0; ya < height - 2; ya++){
            int yp = ya + getScrollY();
            for(int xa = 0; xa < width - 2; xa++){
                int xp = xa + getScrollX();
                Sprite sprite = spriteAt(xp, yp, zPos);
                renderer.renderSprite(sprite, xa + 1, ya + 1);
            }
        }
    }
    
    private Sprite spriteAt(int xp, int yp, int zp){
        
        Creature c = world.creature(xp, yp, zp);
        if(c != null) return c.sprite();
        
        Item i = world.item(xp, yp, zp);
        if(i != null) return i.sprite();
        
        return world.tile(xp, yp, zp).sprite();
    }
    
    public void setCameraPosition(int xPos, int yPos, int zPos){
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }
    
    public int getScrollX(){
        return Math.max(0, Math.min(xPos - (width - 1) / 2, world.width() - (width - 1) + 1));
    }

    public int getScrollY(){
        return Math.max(0, Math.min(yPos - (height - 1) / 2, world.height() - (height - 1) + 1));
    }
}

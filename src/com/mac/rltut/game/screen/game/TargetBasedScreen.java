package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Line;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 11:04 AM.
 */
public abstract class TargetBasedScreen extends Screen {
    
    protected Player player;
    protected int sx, sy;
    protected int xp, yp;
    
    public TargetBasedScreen(int x, int y, int w, int h, Player player, int sx, int sy){
        super(x, y, w, h, "");
        this.player = player;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void render(Renderer renderer) {
        for(Point p : new Line(sx, sy, sx + xp, sy + yp)){
            if(p.x < 0 || p.y < 0 || p.x >= width || p.y >= height) continue;
            if(p.x != sx || p.y != sy) {
                if (p.x != sx + xp || p.y != sy + yp) renderer.renderSprite(Sprite.get("ui_dot"), p.x, p.y);                
            }
            if(p.x == sx + xp && p.y == sy + yp) renderer.renderSprite(Sprite.get("ui_selection"), p.x, p.y, Renderer.TRANSPARENT);
        }
    }

    @Override
    public Screen input(KeyEvent e) {
        int px = xp;
        int py = yp;
        
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP: yp--; break;
            case KeyEvent.VK_DOWN: yp++; break;
            case KeyEvent.VK_LEFT: xp--; break;
            case KeyEvent.VK_RIGHT: xp++; break;
            case KeyEvent.VK_ENTER: selectWorldCoordinate(player.x + xp, player.y + yp, sx + xp, sy + yp); break;
            case KeyEvent.VK_ESCAPE: return null;
        }
        
        if(!isAcceptable(player.x + xp, player.y + yp)){
            xp = px;
            yp = py;
        }

        enterWorldCoordinate(player.x + xp, player.y + yp, sx + xp, sy + yp);
        return this;
    }
    
    public boolean isAcceptable(int xa, int ya){
        return true;
    }
    
    public void enterWorldCoordinate(int xa, int ya, int screenX, int screenY){
        
    }
    
    public void selectWorldCoordinate(int xa, int ya, int screenX, int screenY){
        
    }
}

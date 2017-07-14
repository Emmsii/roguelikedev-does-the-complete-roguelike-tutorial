package com.mac.rltut.game.screen.game;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.FieldOfView;
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
    private Creature player;
    
    private byte[][] fogBit;
    
    public LevelScreen(int x, int y, int width, int height, World world, Creature player){
        super(x, y, width, height, "Level 1");
        this.world = world;
        this.player = player;
        this.fogBit = new byte[width - 1][height - 1];
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        world.computeFov(player.x, player.y, player.z, 16, FieldOfView.FOVType.SHADOWCAST);
        
        for(int ya = 0; ya < height - 2; ya++){
            int yp = ya + getScrollY();
            for(int xa = 0; xa < width - 2; xa++){
                int xp = xa + getScrollX();
                
                fogBit[xa + 1][ya + 1] = 0;
                
                if(!world.isExplored(xp, yp, player.z)){
                    if(!world.isExplored(xp, yp - 1, player.z)) fogBit[xa + 1][ya + 1] += 1;
                    if(!world.isExplored(xp, yp + 1, player.z)) fogBit[xa + 1][ya + 1] += 8;
                    if(!world.isExplored(xp + 1, yp, player.z)) fogBit[xa + 1][ya + 1] += 4;
                    if(!world.isExplored(xp - 1, yp, player.z)) fogBit[xa + 1][ya + 1] += 2;
                    continue;
                }
                
                if(world.tile(x, y, player.z).id == 0) continue;

                Sprite sprite = spriteAt(xp, yp, player.z);
                renderer.renderSprite(sprite, xa + 1, ya + 1, world.inFov(xp, yp, player.z) ? 0 : Renderer.DARKEN_SPRITE);
            }
        }
        
        renderCreatures(renderer);
        renderFog(renderer);
        
        if(title != null) renderer.write(title, 3, 0);
    }
        
    private void renderCreatures(Renderer renderer){
        int xp = getScrollX();
        int yp = getScrollY();

        for(Creature c : world.creatures(player.z)){
            if(!world.inFov(c.x, c.y, c.z)) continue;
            renderer.renderSprite(c.sprite(), c.x - xp + 1, c.y - yp + 1);
        }
    }
    
    private void renderFog(Renderer renderer){
        for(int ya = 0; ya < height - 2; ya++) {
            int yp = ya + getScrollY();
            for (int xa = 0; xa < width - 2; xa++) {
                int xp = xa + getScrollX();
                if(world.isExplored(xp, yp, player.z)) continue;
                Sprite fog = Sprite.getFogSprite(fogBit[xa + 1][ya + 1]);
                renderer.renderSprite(fog, xa + 1, ya + 1);                
            }
        }
    }
    
    private Sprite spriteAt(int xp, int yp, int zp){
        Item i = world.item(xp, yp, zp);
        if(i != null) return i.sprite();
        return world.tile(xp, yp, zp).sprite();
    }

    public int getScrollX(){
        return Math.max(0, Math.min(player.x - (width - 1) / 2, world.width() - (width - 1) + 1));
    }

    public int getScrollY(){
        return Math.max(0, Math.min(player.y - (height - 1) / 2, world.height() - (height - 1) + 1));
    }
}

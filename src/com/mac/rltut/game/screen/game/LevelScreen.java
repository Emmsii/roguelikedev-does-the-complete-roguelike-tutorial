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
    
    public static boolean showFov = true; //Debug
    
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
        world.computeFov(player.x, player.y, player.z, player.vision(), FieldOfView.FOVType.SHADOWCAST);
        
        for(int ya = 1; ya < height - 1; ya++){
            int yp = ya + getScrollY() - 1;
            for(int xa = 1; xa < width - 1; xa++){
                int xp = xa + getScrollX() - 1;
                
                fogBit[xa][ya] = 0;

                if(!world.isExplored(xp, yp, player.z) && showFov){
                    if(!world.isExplored(xp, yp - 1, player.z)) fogBit[xa][ya] += 1;
                    if(!world.isExplored(xp, yp + 1, player.z)) fogBit[xa][ya] += 8;
                    if(!world.isExplored(xp + 1, yp, player.z)) fogBit[xa][ya] += 4;
                    if(!world.isExplored(xp - 1, yp, player.z)) fogBit[xa][ya] += 2;
                    continue;
                }
                
                Sprite sprite = spriteAt(xp, yp, player.z);
                renderer.renderSprite(sprite, xa, ya);
            }
        }

        renderCreatures(renderer);
        if(showFov) renderFog(renderer);
        
        if(title != null) renderer.write(title, 3, 0);
    }
        
    private void renderCreatures(Renderer renderer){
        int xp = getScrollX();
        int yp = getScrollY();

        for(Creature c : world.creatures(player.z)){
            int xa = (c.x - xp) + 1;
            int ya = (c.y - yp) + 1;
            if(xa == 0 || ya == 0 || xa == width - 1 || ya == height -1) continue;
            
            boolean inFov = false;
            for(int y = 0; y < c.size(); y++){
                for(int x = 0; x < c.size(); x++){
                    if(world.inFov(c.x + x, c.y + y, player.z)){
                        inFov = true;
                        break;
                    }
                }
            }
            
            if(inFov || !showFov) renderer.renderSprite(c.sprite(), xa, ya);
        }
    }
    
    private void renderFog(Renderer renderer){
        for(int ya = 1; ya < height - 1; ya++){
            int yp = ya + getScrollY() - 1;
            for(int xa = 1; xa < width - 1; xa++){
                int xp = xa + getScrollX() - 1;
                if(!world.isExplored(xp, yp, player.z)){
                    Sprite fog = Sprite.getFogSprite(fogBit[xa][ya]);
                    renderer.renderSprite(fog, xa, ya);
                }else if(!world.inFov(xp, yp, player.z) && world.creature(xp, yp, player.z) != null && world.creature(xp, yp, player.z).size() == 1){
                    if(world.creature(xp, yp, player.z) != null) renderer.renderSprite(world.tile(xp, yp, player.z).sprite(), xa, ya);
                    renderer.darkenSprite(xa, ya);
                }else if(!world.inFov(xp, yp, player.z)) renderer.darkenSprite(xa, ya);
            }
        }
    }
    
    private Sprite spriteAt(int xp, int yp, int zp){
        Item i = world.item(xp, yp, zp);
        if(i != null) return i.sprite();
        return world.tile(xp, yp, zp).sprite();
    }

    public int getScrollX(){
        return Math.max(0, Math.min(player.x - (width - 1) / 2, world.width() - (width - 1)));
    }

    public int getScrollY(){
        return Math.max(0, Math.min(player.y - (height - 1) / 2, world.height() - (height - 1)));
    }
}

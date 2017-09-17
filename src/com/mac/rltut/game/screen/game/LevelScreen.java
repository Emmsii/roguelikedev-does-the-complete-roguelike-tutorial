package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.FieldOfView;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.objects.MapObject;

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
    
    public LevelScreen(int x, int y, int width, int height, String title, World world, Creature player){
        super(x, y, width, height, title);
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
        String levelType = world.level(player.z).type();
        if(levelType.equalsIgnoreCase("default")) setTitle("Forest " + (player.z + 1));
        else setTitle(StringUtil.capitalizeFirst(levelType) + " Forest " + (player.z + 1));
        
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
                renderer.renderSprite(sprite, xa + this.x, ya + this.y);
            }
        }

        renderCreatures(renderer);
        if(showFov) renderFog(renderer);

        renderStatusEffects(renderer);
        
        renderBorder(renderer);
    }
        
    private void renderCreatures(Renderer renderer){
        int xp = getScrollX();
        int yp = getScrollY();

        for(Creature c : world.creatures(player.z)){
            int xa = (c.x - xp) + 1;
            int ya = (c.y - yp) + 1;
            if(xa < 0 || ya < 0 || xa > width - 1 || ya > height -1) continue;
            
            boolean inFov = false;
            for(int y = 0; y < c.size(); y++){
                for(int x = 0; x < c.size(); x++){
                    if(world.inFov(c.x + x, c.y + y, player.z)){
                        inFov = true;
                        break;
                    }
                }
            }
            
            if(inFov || !showFov) if(!c.hasFlag("invisible")) renderer.renderSprite(c.sprite(), xa + this.x, ya + this.y);
        }
    }
    
    private void renderFog(Renderer renderer){
        float darkenAmount = (1f - world.dayNightController().lightPercent()) * 0.4f;

        for(int ya = 1; ya < height - 1; ya++){
            int yp = ya + getScrollY() - 1;
            for(int xa = 1; xa < width - 1; xa++){
                int xp = xa + getScrollX() - 1;
                
                if(!world.isExplored(xp, yp, player.z)){
                    Sprite fog = Sprite.getFogSprite(fogBit[xa][ya]);
                    renderer.renderSprite(fog, xa + this.x, ya + this.y);
                }else if(!world.inFov(xp, yp, player.z) && world.creature(xp, yp, player.z) != null && world.creature(xp, yp, player.z).size() == 1){
                    if(world.creature(xp, yp, player.z) != null) renderer.renderSprite(spriteAt(xp, yp, player.z), xa + this.x, ya + this.y);
                    renderer.darkenSprite(xa + this.x, ya + this.y);
                }else if(!world.inFov(xp, yp, player.z)) renderer.darkenSprite(xa + this.x, ya + this.y);
                
                if(world.isExplored(xp, yp, player.z)){
                    renderer.colorizeSprite(xa + this.x, ya + this.y, darkenAmount);
                    renderer.darkenSprite(xa + this.x, ya + this.y, 1f - (darkenAmount * 0.75f));
                }
            }
        }
    }
    
    private void renderStatusEffects(Renderer renderer){
        int count = 0;
        for(Effect e : player.effects()) if(e.spriteUi() != null) count++;
        count *= 2;
        if(count == 0) return;
        int xp = 1;
        int yp = 1;
        renderBox(xp, yp, count + 2, 4, true, renderer);
        for(Effect e : player.effects()) renderer.renderSprite(e.spriteUi(), (this.x + xp++) * 2, this.y + yp + 1, 0, 2);
    }
    
    private Sprite spriteAt(int xp, int yp, int zp){
        Item i = world.item(xp, yp, zp);
        if(i != null) return i.sprite();
        
        MapObject obj = world.mapObject(xp, yp, zp);
        if(obj != null) return obj.sprite();

        if(world.level(zp).blood(xp, yp) && Config.blood) return Sprite.get("blood");
        
        return world.tile(xp, yp, zp).sprite();
    }

    public int getScrollX(){
        return Math.max(0, Math.min(player.x - (width - 1) / 2, world.width() - (width - 1)));
    }

    public int getScrollY(){
        return Math.max(0, Math.min(player.y - (height - 1) / 2, world.height() - (height - 1)));
    }
}

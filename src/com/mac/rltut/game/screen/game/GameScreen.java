package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:28 AM.
 */
public class GameScreen extends Screen{
    
    private World world;
    
    private LevelScreen levelScreen;
        
    Creature player;
    
    public GameScreen(World world){
        this.world = world;
        init();
    }
    
    private void init(){
        levelScreen = new LevelScreen(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), world);

        player = new Creature(Sprite.player);
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);
    }
    
    
    @Override
    public Screen input(KeyEvent e) {
        
        //Temp movement code        
        int dx = 0, dy = 0, dz = 0;
        
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W: dy--; break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: dy++; break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A: dx--; break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: dx++; break;
            
            case KeyEvent.VK_PAGE_DOWN: dz++; break;
            case KeyEvent.VK_PAGE_UP: dz--; break;
        }
                
        //Temp
        if(dz != 0){
            player.z += dz;
            if(player.z < 0) player.z = 0;
            if(player.z > world.depth() - 1) player.z = world.depth() - 1;
            Point start = world.startPointAt(player.z);
            player.x = start.x;
            player.y = start.y;
        }

        //Temp
        if(!world.tile(player.x + dx, player.y + dy, player.z).solid()) {
            world.move(player.x + dx, player.y + dy, player.z, player);
        }
        
        world.update(player.z);
        
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        levelScreen.setTitle("Level " + (player.z + 1));
        
        levelScreen.setCameraPosition(player.x, player.y, player.z);
        levelScreen.render(renderer);
        
        renderer.writeCenter("WASD/ARROW keys to move, PAGE UP/DOWN to make change levels.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 1);
    }
}

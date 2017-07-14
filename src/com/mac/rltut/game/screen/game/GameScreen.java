package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
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
        player = new Creature("player", Sprite.get("player"));//temp
        new CreatureAI(player);
        levelScreen = new LevelScreen(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), world, player);

        
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);
    }

    @Override
    public Screen input(KeyEvent e) {

        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W: player.moveBy(0, -1, 0); break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: player.moveBy(0, 1, 0); break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A: player.moveBy(-1, 0, 0); break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: player.moveBy(1, 0, 0); break;

            case KeyEvent.VK_PAGE_DOWN: world.moveDown(player); break;
            case KeyEvent.VK_PAGE_UP: world.moveUp(player); break;
        }
        
        
        world.update(player.z);
                
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        levelScreen.setTitle("Level " + (player.z + 1));
        
        levelScreen.render(renderer);
        
        renderer.writeCenter("WASD/ARROW keys to move, PAGE UP/DOWN to make change levels.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 1);
    }
}

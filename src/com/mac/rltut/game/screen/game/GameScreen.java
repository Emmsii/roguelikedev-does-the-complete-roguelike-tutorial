package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.map.Map;
import com.mac.rltut.game.map.MapBuilder;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:28 AM.
 */
public class GameScreen extends Screen{
    
    private Map map;
    
    private MapScreen mapScreen;
        
    Creature player;
    
    public GameScreen(Map map){
        this.map = map;
        init();
    }
    
    private void init(){
//        this.map = new MapBuilder(92, 92, 6, System.currentTimeMillis()).generate().build();
        mapScreen = new MapScreen(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), map);

        player = new Creature(Sprite.player);
        Point spawn = map.randomEmptyPoint(0);
        map.add(spawn.x, spawn.y, spawn.z, player);
    }
    
    
    @Override
    public Screen input(KeyEvent e) {
        
        int dx = 0, dy = 0;
        
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W: dy--; break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: dy++; break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A: dx--; break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: dx++; break;
            
            case KeyEvent.VK_SPACE: init(); break;
        }
        
        if(!map.tile(player.x + dx, player.y + dy, player.z).solid()) {
            map.move(player.x + dx, player.y + dy, player.z, player);
        }
        map.update(player.z);
        
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        mapScreen.setCameraPosition(player.x, player.y, player.z);
        mapScreen.render(renderer);
        
        renderer.writeCenter("WASD/ARROW keys to move, SPACE to make new map.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 1);
    }
}

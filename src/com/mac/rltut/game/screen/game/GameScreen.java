package com.mac.rltut.game.screen.game;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
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
        
    int px = 0, py = 0;
    
    public GameScreen(){
        this.map = new MapBuilder(92, 92, 16, System.currentTimeMillis()).generate().build();
        mapScreen = new MapScreen(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), map);
    }
    
    @Override
    public Screen input(KeyEvent e) {
        
        switch(e.getKeyCode()){
            case KeyEvent.VK_W: py--; break;
            case KeyEvent.VK_S: py++; break;
            case KeyEvent.VK_A: px--; break;
            case KeyEvent.VK_D: px++; break;
        }
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        mapScreen.setCameraPosition(px, py);
        mapScreen.render(renderer);
    }
}

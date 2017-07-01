package com.mac.rltut.game.screen;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.map.Map;
import com.mac.rltut.game.map.MapBuilder;
import com.mac.rltut.game.screen.game.GameScreen;

import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 11:49 AM.
 */
public class LoadingScreen extends Screen{
    
    private Map map;
    private boolean finnished;
    
    public LoadingScreen(MapBuilder mapBuilder){
        finnished = false;
        //TODO: This is a really shit way to do this; but hey, it works!
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                map = mapBuilder.generate().build();
                finnished = true;
                Engine.instance().input(null);
            }
        });
        t.start();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        if(!finnished) return this;
        return new GameScreen(map);
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        renderer.writeCenter("Loading...", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
    }
}

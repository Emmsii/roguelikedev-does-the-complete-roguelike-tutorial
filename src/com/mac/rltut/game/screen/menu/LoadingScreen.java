package com.mac.rltut.game.screen.menu;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.builders.WorldBuilder;
import com.mac.rltut.game.screen.game.GameScreen;

import java.awt.event.KeyEvent;
import java.security.Key;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 11:49 AM.
 */
public class LoadingScreen extends Screen {

    private Game game;
    private boolean finished;

    public LoadingScreen(WorldBuilder worldBuilder){
        finished = false;
        //TODO: This is a really shit way to do this; but hey, it works!
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                game = new Game().newGame(worldBuilder.generate().populate().spawnItems().build());
                finished = true;
                Engine.instance().input(null);
            }
        });
        
        t.start();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        if(!finished || game == null) return this;
        return new GameScreen(game);
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        renderer.writeCenter("Loading...", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
    }
}
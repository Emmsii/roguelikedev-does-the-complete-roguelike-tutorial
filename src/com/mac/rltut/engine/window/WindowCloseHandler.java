package com.mac.rltut.engine.window;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.GameScreen;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class WindowCloseHandler extends WindowAdapter {
    
    @Override
    public void windowClosing(WindowEvent e) {
        Screen screen = Engine.instance().screen();
        if(screen instanceof GameScreen){
            GameScreen gameScreen = (GameScreen) screen;
            FileHandler.saveGame(gameScreen.game());
        }
        
        Log.info("Goodbye!");
        System.exit(0);
    }
}

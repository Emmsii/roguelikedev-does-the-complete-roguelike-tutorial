package com.mac.rltut.engine;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.window.Input;
import com.mac.rltut.engine.window.Panel;
import com.mac.rltut.engine.window.Terminal;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.GameScreen;
import com.mac.rltut.game.screen.menu.TestMenu;

import java.awt.event.KeyEvent;


/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:53 AM.
 */
public class Engine {

    private static Engine instance = null;

    private int widthInTiles, heightInTiles;
    private int windowScale, tileSize;
    private String title;

    private Terminal terminal;
    private Panel panel;
    private Renderer renderer;
    private Input input;

    private Screen screen;

    private Engine(){
        Log.set(Log.LEVEL_DEBUG);
    }

    public void init(int widthInTiles, int heightInTiles, int windowScale, int tileSize, String title){
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.windowScale = windowScale;
        this.tileSize = tileSize;
        this.title = title;

        this.terminal = new Terminal(widthInTiles, heightInTiles, windowScale, tileSize, title);
        this.panel = terminal.panel();
        this.renderer = new Renderer(panel.widthInPixels(), panel.heightInPixels());
        this.input = new Input();

        panel.setRenderer(renderer);
        renderer.setDefaultFontColor(0xffffff);
        terminal.addKeyListener(input);
        screen = new TestMenu();

        render();
    }

    public void input(KeyEvent e){
        if(screen != null) screen = screen.input(e);
        render();
    }

    public void render(){
        renderer.clear();
        if(screen != null) screen.render(renderer);
        terminal.repaint();
    }
    
    public int widthInTiles(){
        return widthInTiles;
    }

    public int heightInTiles(){
        return heightInTiles;
    }
    
    public int tileSize(){
        return tileSize;
    }

    public static Engine instance(){
        if(instance == null) instance = new Engine();
        return instance;
    }
}


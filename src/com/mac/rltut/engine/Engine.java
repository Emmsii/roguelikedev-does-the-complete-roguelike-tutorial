package com.mac.rltut.engine;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Font;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Spritesheet;
import com.mac.rltut.engine.input.Input;
import com.mac.rltut.engine.loader.*;
import com.mac.rltut.engine.window.Panel;
import com.mac.rltut.engine.window.Terminal;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.menu.StartScreen;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;


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
    private String version;
    
    private Terminal terminal;
    private Panel panel;
    private Renderer renderer;
    private Input input;

    private Screen screen;
    
    private Font currentFont;
    
    private Engine(){
        Log.set(Log.LEVEL_DEBUG);
        loadData();
        setFont("cheepicus");
        
    }
    
    private void loadData(){
        Log.debug("Loading data...");
        try {
            new SpritesheetLoader("data/sheets.dat").load();
            new SpriteLoader("data/sprites.dat").load();
            new TileLoader("data/tiles.dat").load();
            new ItemLoader("data/items.dat").load();
            new CreatureLoader("data/creatures.dat").load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int widthInTiles, int heightInTiles, int windowScale, int tileSize, String title, String version){
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.windowScale = windowScale;
        this.tileSize = tileSize;
        this.title = title;
        this.version = version;

        this.terminal = new Terminal(widthInTiles, heightInTiles, windowScale, tileSize, title + " - " + version);
        this.panel = terminal.panel();
        this.renderer = new Renderer(panel.widthInPixels(), panel.heightInPixels());
        this.input = new Input();

        panel.setRenderer(renderer);
        renderer.setDefaultFontColor(0xffffff);
        terminal.addKeyListener(input);
        screen = new StartScreen();

        render();
    }

    public void input(KeyEvent e){
        if(e != null && e.getKeyCode() == KeyEvent.VK_F7) saveScreenshot(renderer.pixels(), widthInTiles * 8, heightInTiles * 8);
        if(screen != null) screen = screen.input(e);
        render();
    }

    public void render(){
        renderer.clear();
        if(screen != null) screen.render(renderer);
        terminal.repaint();
    }
    
    private void saveScreenshot(int[] pixels, int w, int h){
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        img.setRGB(0, 0, w, h, pixels, 0, w);

        try {
            ImageIO.write(img, "png", new File("shot_" + (System.currentTimeMillis() / 1000) + ".png"));
            Log.debug("Screenshot saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setFont(String name){
        currentFont = (Font) Spritesheet.get(name);
    }
    
    public Font currentFont(){
        return currentFont;
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
    
    public String version(){
        return version;
    }

    public static Engine instance(){
        if(instance == null) instance = new Engine();
        return instance;
    }
}


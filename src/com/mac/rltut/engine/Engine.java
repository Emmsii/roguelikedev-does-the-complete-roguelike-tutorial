package com.mac.rltut.engine;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.file.loaders.*;
import com.mac.rltut.engine.graphics.Font;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Spritesheet;
import com.mac.rltut.engine.input.Input;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.window.Panel;
import com.mac.rltut.engine.window.Terminal;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.menu.StartScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:53 AM.
 */
public class Engine {

    private static Engine instance = null;

    private boolean fullscreen;
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
    
    private Engine(){}
    
    private void loadData(){
        Log.debug("Loading data...");
        double start = System.nanoTime();
        try {
            new SpritesheetLoader("data/sheets.dat").load();
            new SpriteLoader("data/sprites.dat").load();
            new TileLoader("data/tiles.dat").load();
            new LevelLoader("data/levels.dat").load();
            new SpellLoader("data/spells.dat").load();
            new ItemLoader("data/items.dat").load();
            new CreatureLoader("data/creatures.dat").load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.debug("Loaded data in " + ((System.nanoTime() - start) / 1000000) + "ms");
    }

    public void init(boolean fullscreen, int widthInTiles, int heightInTiles, int windowScale, int tileSize, String title, String version){
        Log.set(Log.LEVEL_DEBUG);
        FileHandler.init();
        loadData();
        setFont("cheepicus");
        
        this.fullscreen = fullscreen;
        GraphicsDevice device = null;
        if(fullscreen){
            GraphicsDevice[] devices = getMonitorDevices();
            if(Config.monitor >= devices.length || Config.monitor < 0){
                Log.error("Invalid monitor id [" + Config.monitor + "]");
                this.widthInTiles = widthInTiles;
                this.heightInTiles = heightInTiles;
            }else {
                device = devices[Config.monitor];
                Rectangle screenSize = device.getDefaultConfiguration().getBounds();
                this.widthInTiles = (int) Math.floor(screenSize.getWidth() / (tileSize * windowScale));
                this.heightInTiles = (int) Math.floor(screenSize.getHeight() / (tileSize * windowScale));
            }
        }else{
            this.widthInTiles = widthInTiles;
            this.heightInTiles = heightInTiles;
        }
        
        this.windowScale = windowScale;
        this.tileSize = tileSize;
        this.title = title;
        this.version = version;

        this.terminal = new Terminal(fullscreen, this.widthInTiles, this.heightInTiles, windowScale, tileSize, title + " - " + version, new ImageIcon(Engine.class.getClassLoader().getResource("textures/icon.png")));
        this.panel = terminal.panel();
        this.renderer = new Renderer(panel.widthInPixels(), panel.heightInPixels());
        this.input = new Input();

        if(fullscreen && device != null) device.setFullScreenWindow(terminal);
        
        panel.setRenderer(renderer);
        renderer.setDefaultFontColor(Colors.WHITE);
        terminal.addKeyListener(input);
        
        screen = new StartScreen();
        
        render();
    }

    public void input(KeyEvent e){
        if(e != null && e.getKeyCode() == KeyEvent.VK_F7) saveScreenshot("screenshot_" + System.currentTimeMillis());
        if(screen != null) screen = screen.input(e);
        render();
    }

    public void render(){
        renderer.clear();
        if(screen != null) screen.render(renderer);
        terminal.repaint();
    }
    
    public void saveScreenshot(String name){
        BufferedImage img = new BufferedImage(widthInTiles * 8, heightInTiles * 8, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, widthInTiles * 8, heightInTiles * 8, renderer.pixels(), 0, widthInTiles * 8);

        try {
            ImageIO.write(img, "png", new File(name + ".png"));
            Log.debug("Screenshot saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public GraphicsDevice[] getMonitorDevices(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] result = env.getScreenDevices();
        if(env == null || result == null) return null;
        return result;
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
    
    public int defaultFontColor(){
        return renderer.defaultFontColor();
    }
    
    public Screen screen(){
        return screen;
    }
    
    public String title(){
        return title;
    }

    public String version(){
        return version;
    }
    
    public static Engine instance(){
        if(instance == null) instance = new Engine();
        return instance;
    }
}


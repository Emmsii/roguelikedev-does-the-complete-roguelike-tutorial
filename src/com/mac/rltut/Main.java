package com.mac.rltut;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.window.CustomExceptionHandler;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:49 AM.
 */
public class Main {

    public static final String TITLE = "Forest Roguelike";
    public static final String VERSION = "1.1.1";
    public static final int WIDTH = 80;
    public static final int HEIGHT = 45;
    public static final int TILE_SIZE = 8;

    public static final int DEFAULT_SCALE = 2;
    
    public static void main(String[] args){
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler("logs/"));
        Config.load();
        
        String saveVersion = FileHandler.saveGameVersion();
    
        if(saveVersion == null || saveVersion.equals(VERSION)) Config.save();
        
        int scale = Config.scale;
        if(scale < 0){
            Log.warn("Scale cannot be below 0");
            scale = 1;
        }
        
        if(scale > 16){
            Log.warn("Scale cannot be above 16.");
            scale = 16;
        }

        Engine.instance().init(Config.fullscreen, WIDTH, HEIGHT, scale, TILE_SIZE, TITLE, VERSION);
    }
}
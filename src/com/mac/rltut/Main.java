package com.mac.rltut;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.window.CustomExceptionHandler;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:49 AM.
 */
public class Main {

    public static final int DEFAULT_SCALE = 2;
    
    public static void main(String[] args){
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler("logs/"));
        Config.load();
        Engine.instance().init(Config.fullscreen, 80, 45, DEFAULT_SCALE, 8, "RLTUT", "v0.9.3");
    }
}
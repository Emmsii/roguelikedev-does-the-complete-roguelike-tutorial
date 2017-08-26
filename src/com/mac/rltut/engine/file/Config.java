package com.mac.rltut.engine.file;

import com.mac.rltut.Main;

import java.io.*;
import java.util.Properties;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 10:22 AM.
 */
public class Config {

    private static final String USER_FOLDER = System.getProperty("user.home");
    private static final String GAME_FOLDER = USER_FOLDER + "/Forest RL/";
    private static final String FILE_NAME = "properties.txt";
    
    public static boolean fullscreen;
    public static int scale;
    public static int monitor;
    public static boolean autoEquip;
    
    private static void loadDefaults(){
        fullscreen = false;
        scale = Main.DEFAULT_SCALE;
        monitor = 0;
        autoEquip = true;
    }
    
    public static void load(){
        File file = new File(GAME_FOLDER + FILE_NAME);
        if(!file.exists()){
            loadDefaults();
            save();
        }
        
        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream(GAME_FOLDER + FILE_NAME);
            properties.load(inputStream);
            
            fullscreen =  properties.containsKey("fullscreen") ? Boolean.parseBoolean((String) properties.get("fullscreen")) : false;
            scale = properties.containsKey("scale") ? Integer.parseInt((String) properties.get("scale")) : Main.DEFAULT_SCALE;
            monitor = properties.containsKey("monitor") ? Integer.parseInt((String) properties.get("monitor")) : 0;
            autoEquip = properties.containsKey("auto_equip") ? Boolean.parseBoolean((String) properties.get("auto_equip")) : true;
            
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        File file = new File(GAME_FOLDER);
        if(!file.exists()) file.mkdirs();
        
        Properties properties = new Properties();
        
        try {
            OutputStream outputStream = new FileOutputStream(GAME_FOLDER + FILE_NAME);
            properties.setProperty("fullscreen", String.valueOf(fullscreen));
            properties.setProperty("scale", String.valueOf(scale));
            properties.setProperty("monitor", String.valueOf(monitor));
            properties.setProperty("auto_equip", String.valueOf(autoEquip));
            properties.store(outputStream, null);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.mac.rltut.engine.file;

import java.io.*;
import java.util.Properties;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 10:22 AM.
 */
public class Config {
    
    private static final String FILE_NAME = "properties.txt";
    
    public static boolean fullscreen;
    public static int monitor;
    
    private static void loadDefaults(){
        fullscreen = false;
        monitor = 0;
    }
    
    public static void load(){
        File file = new File(FILE_NAME);
        if(!file.exists()){
            loadDefaults();
            save();
        }
        
        Properties properties = new Properties();

        try {
            InputStream inputStream = new FileInputStream(FILE_NAME);
            properties.load(inputStream);
            
            fullscreen = Boolean.parseBoolean((String) properties.get("fullscreen"));
            monitor = Integer.parseInt((String) properties.get("monitor"));
            
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        Properties properties = new Properties();
        
        try {
            OutputStream outputStream = new FileOutputStream(FILE_NAME);
            properties.setProperty("fullscreen", String.valueOf(fullscreen));
            properties.setProperty("monitor", String.valueOf(monitor));
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

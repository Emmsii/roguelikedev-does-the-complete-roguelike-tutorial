package com.mac.rltut.engine;

import com.mac.rltut.engine.util.StringUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;

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
    
    private static void loadDefaults(){
        fullscreen = false;
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

package com.mac.rltut.engine;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.Game;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 04:09 PM.
 */
public class FileHandler {


    private static final String SAVE_FOLDER = "save/";
    private static final String SAVE_NAME = "game.dat";
    private static final String SAVE_LOCATION = SAVE_FOLDER + SAVE_NAME;
    
    private static Kryo kryo;
    private static boolean initialized = false;
    
    public static void init(){
        kryo = new Kryo();
        initialized = true;
    }
    
    public static boolean gameExists(){
        return new File(SAVE_LOCATION).exists();
    }
    
    public static void createSaveFolder(){
        if(gameExists()) return;
        File file = new File(SAVE_FOLDER);
        if(!file.exists()) file.mkdirs();
    }
    
    public static void saveGame(Game game){
        if(!initialized){
            Log.error("File Handler not initialized. Cannot save game.");
            System.exit(-1);
        }

        createSaveFolder();
        
        File file = new File(SAVE_LOCATION);
        FileOutputStream fileOutputStream = null;
        DeflaterOutputStream deflaterOutputStream = null;
        Output output = null;
        
        double start = System.nanoTime();
        Log.debug("Saving game...");
        Log.set(Log.LEVEL_INFO);
        
        try {
            fileOutputStream = new FileOutputStream(file);
            deflaterOutputStream = new DeflaterOutputStream(fileOutputStream);
            output = new Output(deflaterOutputStream);
            kryo.writeObject(output, game);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                output.close();
                deflaterOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.set(Log.LEVEL_DEBUG);
        Log.debug("Game saved in " + ((System.nanoTime() - start) / 1000000) + "ms");
        Log.debug("Size: " + getFileSize(file));
    }
    
    public static Game loadGame(){
        if(!initialized){
            Log.error("File Handler not initialized. Cannot load game.");
            System.exit(-1);
        }
        
        if(!gameExists()){
            Log.warn("Save file does not exist. Cannot load game.");
            return null;
        }
        
        File file = new File(SAVE_LOCATION);
        FileInputStream fileInputStream = null;
        InflaterInputStream inflaterInputStream = null;
        Input input = null;

        Game game = null;
        
        double start = System.nanoTime();
        Log.debug("Loading game...");
        Log.set(Log.LEVEL_INFO);

        try {
            fileInputStream = new FileInputStream(file);
            inflaterInputStream = new InflaterInputStream(fileInputStream);
            input = new Input(inflaterInputStream);
            game = kryo.readObject(input, Game.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
                inflaterInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.set(Log.LEVEL_DEBUG);
        Log.debug("Game loaded in " + ((System.nanoTime() - start) / 1000000) + "ms");
        return game;
    }
    
    public static String getFileSize(File file){
        double bytes = file.length();
        double kilobytes = bytes / 1024;
        double megabytes = kilobytes / 1024;
        if(megabytes < 1) return String.format("%.2f kb", kilobytes);
        return String.format("%.2f mb", megabytes);
    }
}

package com.mac.rltut.engine.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.FieldOfView;
import com.mac.rltut.engine.util.SessionTimer;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.MessageLog;
import com.mac.rltut.game.effects.*;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.*;
import com.mac.rltut.game.entity.creature.ai.*;
import com.mac.rltut.game.entity.creature.stats.Stats;
import com.mac.rltut.game.entity.item.*;
import com.mac.rltut.game.entity.item.util.DropTable;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.world.DayNightController;
import com.mac.rltut.game.world.Level;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.ChestTile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 04:09 PM.
 */
public class FileHandler {

    private static final String USER_FOLDER = System.getProperty("user.home");
    private static final String GAME_FOLDER = USER_FOLDER + "/Forest RL/save/";
    private static final String SAVE_NAME = "game.dat";
    private static final String SAVE_LOCATION = GAME_FOLDER + SAVE_NAME;
    
    private static Kryo kryo;
    private static boolean initialized = false;
    
    public static void init(){
        kryo = new Kryo();
        kryo.setRegistrationRequired(true);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.register(HashSet.class);
        kryo.register(int[].class);
        kryo.register(boolean[][].class);
        kryo.register(boolean[].class);
        kryo.register(byte[][].class);
        kryo.register(byte[].class);
        kryo.register(String[].class);
        
        kryo.register(Creature[][][].class);
        kryo.register(Creature[][].class);
        kryo.register(Creature[].class);

        kryo.register(Item[][][].class);
        kryo.register(Item[][].class);
        kryo.register(Item[].class);

        kryo.register(MapObject[][].class);
        kryo.register(MapObject[].class);
        
        kryo.register(EquipmentSlot[].class);
        kryo.register(Level[].class);
        
        kryo.register(Sprite.class);
        
        kryo.register(Game.class);
        kryo.register(Player.class);
        kryo.register(MessageLog.class);
        kryo.register(ColoredString.class);
        kryo.register(SessionTimer.class);
        
        kryo.register(Entity.class);
        kryo.register(Creature.class);
        kryo.register(NPC.class);
        kryo.register(Wizard.class);
        kryo.register(Boss.class);
        kryo.register(EvilWizard.class);
        kryo.register(EvilNpcAi.class);
        kryo.register(CreatureAI.class);
        kryo.register(PlayerAI.class);
        kryo.register(BossAI.class);
        kryo.register(PackMemberAI.class);
        kryo.register(PackAI.class);
        kryo.register(PassiveAI.class);
        kryo.register(NpcAI.class);
        kryo.register(NeutralAI.class);
        kryo.register(AggressiveAI.class);
        kryo.register(Stats.class);

        kryo.register(Item.class);
        kryo.register(ItemStack.class);
        kryo.register(Equippable.class);
        kryo.register(Consumable.class);
        kryo.register(Potion.class);
        kryo.register(Spellbook.class);
        kryo.register(EquipmentSlot.class);
        kryo.register(DropTable.class);
        kryo.register(DropTable.Drop.class);
        kryo.register(Inventory.class);
        
        kryo.register(MapObject.class);
        kryo.register(Chest.class);
        
        kryo.register(ChestTile.class);

        kryo.register(Spell.class);
        kryo.register(Effect.class);
        kryo.register(Blind.class);
        kryo.register(Burn.class);
        kryo.register(Freeze.class);
        kryo.register(Heal.class);
        kryo.register(HealthRegen.class);
        kryo.register(ManaRegen.class);
        kryo.register(NightVision.class);
        kryo.register(Poison.class);
        kryo.register(Rage.class);
        
        kryo.register(World.class);
        kryo.register(DayNightController.class);
        kryo.register(FieldOfView.class);
        kryo.register(Level.class);

        kryo.register(Point.class);
        
        initialized = true;
    }
    
    public static boolean gameSaveExists(){
        return new File(SAVE_LOCATION).exists();
    }
    
    public static void deleteGameSave(){
        if(!gameSaveExists()) return;
        File file = new File(SAVE_LOCATION);
        file.delete();
        Log.debug("Save file deleted.");
    }
    
    public static void createSaveFolder(){
        if(gameSaveExists()) return;
        File file = new File(GAME_FOLDER);
        if(!file.exists()) file.mkdirs();
    }
        
    public static String saveGameVersion(){
        if(!gameSaveExists()) return null;
        String version = null;
        
        File file = new File(SAVE_LOCATION);
        FileInputStream fileInputStream = null;
        InflaterInputStream inflaterInputStream = null;
        Input input = null;

        try {
            fileInputStream = new FileInputStream(file);
            inflaterInputStream = new InflaterInputStream(fileInputStream);
            input = new Input(inflaterInputStream);
            version = input.readString();
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
        
        return version;
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
            output.writeString(Engine.instance().version());
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
        
        if(!gameSaveExists()){
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
            String version = input.readString();
            Log.debug(version);
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
        
        game.init();
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

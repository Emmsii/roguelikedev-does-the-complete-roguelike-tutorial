package com.mac.rltut.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.ai.PlayerAI;
import com.mac.rltut.game.world.World;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 03:56 PM.
 */
public class Game{
    
    private World world;
    private MessageLog log;
    private Player player;
        
    public Game(){
        
    }
    
    public Game newGame(Player player, World world){
        this.player = player;
        this.world = world;
        
        AStar.instance().init(world);
        log = new MessageLog();
        new PlayerAI(player, log);
        
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);

        Kryo kryo = new Kryo();

        Log.debug("Saving game...");
        Log.set(Log.LEVEL_INFO);
        
        double start = System.nanoTime();
        try {
            Output output = new Output(new DeflaterOutputStream(new FileOutputStream("game.dat")));
            kryo.writeObject(output, this);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.set(Log.LEVEL_DEBUG);
        Log.debug("Game saved in " + ((System.nanoTime() - start) / 1000000) + "ms");
        Log.debug("Loading game...");
        Log.set(Log.LEVEL_INFO);
        start = System.nanoTime();
        try {
            Input input = new Input(new InflaterInputStream(new FileInputStream("game.dat")));
            Game newGame = kryo.readObject(input, Game.class);
            input.close();
            Log.set(Log.LEVEL_DEBUG);
            Log.debug("Game loaded in " + ((System.nanoTime() - start) / 1000000) + "ms");
            return newGame;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        

        return null;
    }
    
    public Game loadGame(){
        Log.warn("TODO: Load game!");
        return null;
    }
    
    public void update(){
        world.dayNightController().update();
        
        int z = player.z;
        int min = z - 1 < 0 ? 0 : z - 1;
        int max = z + 1 >= world.depth() - 1 ? world.depth() - 1: z + 1;
        for(int level = min; level <= max; level++) {
            world.update(level);
        }
        
    }
    
    public World world(){
        return world;
    }

    public MessageLog log(){
        return log;
    }
    
    public Player player(){
        return player;
    }

}

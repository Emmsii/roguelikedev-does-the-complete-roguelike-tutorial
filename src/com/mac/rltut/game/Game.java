package com.mac.rltut.game;

import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.ai.PlayerAI;
import com.mac.rltut.game.world.World;

import java.io.File;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 03:56 PM.
 */
public class Game{
    
    private World world;
    private MessageLog log;
    private Player player;
        
    public Game(){}
    
    public Game newGame(Player player, World world){
        this.player = player;
        this.world = world;
        
        AStar.instance().init(world);
        log = new MessageLog();
        new PlayerAI(player, log);
        
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);
        
        FileHandler.saveGame(this);
        
        return this;
    }
    
    public void update(){
        world.dayNightController().update();
        
        int z = player.z;
        int min = z - 1 < 0 ? 0 : z - 1;
        int max = z + 1 >= world.depth() - 1 ? world.depth() - 1: z + 1;
        for(int level = min; level <= max; level++) world.update(level);
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

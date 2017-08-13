package com.mac.rltut.game;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.SessionTimer;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.ai.PlayerAI;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.SpellbookGenerator;
import com.mac.rltut.game.world.World;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 03:56 PM.
 */
public class Game{
    
    private World world;
    private MessageLog log;
    private Player player;
        
    private SessionTimer sessionTimer;
    private long gameStartTime;
    
    public Game(){
        
    }
    
    public Game newGame(Player player, World world){
        this.player = player;
        this.world = world;
        this.log = new MessageLog();
        this.gameStartTime = System.currentTimeMillis();
        
        new PlayerAI(player, log);
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);

        player.inventory().add((Item) Codex.items.get("sword").entity());
        for(int i = 0; i < 10; i++){
            player.inventory().add(SpellbookGenerator.generate(20, new Random()));
        }
        
        init();
        FileHandler.saveGame(this);

        Date date = new Date(gameStartTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");
        Log.debug("New game started " + dateFormat.format(date));
        
        return this;
    }
    
    public void init(){
        AStar.instance().init(world);
        if(sessionTimer == null) sessionTimer = new SessionTimer(System.currentTimeMillis());
        else{
            SessionTimer newSessionTimer = new SessionTimer(System.currentTimeMillis(), sessionTimer.durationMilliseconds());
            sessionTimer = newSessionTimer;
        }
    }
    
    public void update(){
        sessionTimer.update();
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

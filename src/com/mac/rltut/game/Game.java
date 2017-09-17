package com.mac.rltut.game;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.file.FileHandler;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.pathfinding.astar.AStar;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.SessionTimer;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.*;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.ai.PlayerAI;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.SpellbookGenerator;
import com.mac.rltut.game.entity.util.ItemSpawnProperty;
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
    
    public Game(){}
    
    public Game newGame(Player player, World world){
        this.player = player;
        this.world = world;
        this.log = new MessageLog();
        this.gameStartTime = System.currentTimeMillis();
        
        new PlayerAI(player, log);
        Point spawn = world.startPointAt(0);
        world.add(spawn.x, spawn.y, spawn.z, player);

        player.inventory().add(SpellbookGenerator.newSpellbook(0, new Random()));
        
        log.add("You've drawn the short straw I'm afraid. You need to travel through the forest to rescue the king!");
        
        init();
        FileHandler.saveGame(this);

        Date date = new Date(gameStartTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMM yyyy");
        Log.debug("New game started " + dateFormat.format(date));
        
        return this;
    }

    private Item getEquipmentFromSlot(int z, boolean useSpawnChance, EquipmentSlot... slots){
        Pool<ItemSpawnProperty> pool = new Pool<ItemSpawnProperty>();
        for (ItemSpawnProperty spawnProperty : Codex.items.values()) {
            if(spawnProperty.chance() <= 0 || spawnProperty.chanceAtDepth(z) <= 0 || !spawnProperty.canSpawnAtDepth(z)) continue;
            if (spawnProperty.entity() instanceof Equippable) {
                Equippable e = (Equippable) spawnProperty.entity();
                for (EquipmentSlot s : slots) {
                    if (e.slot() == s) {
                        pool.add(spawnProperty, useSpawnChance ? spawnProperty.chanceAtDepth(z) : 100);
                        break;
                    }
                }
            }
        }

        if(pool.isEmpty()) {
            Log.warn("No equipment spawned on level " + z);
            return null;
        }

        return (Item) pool.get().entity().newInstance();
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
    
    public SessionTimer sessionTimer(){
        return sessionTimer;
    }
    
    public long gameStartTime(){
        return gameStartTime;
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

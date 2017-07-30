package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.stats.PlayerStats;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:37 AM.
 */
public class Player extends Creature{
    
    private PlayerStats stats;
    
    public Player(String name, Sprite sprite) {
        super(name, "its you", sprite, "player");
        this.stats = new PlayerStats(this);
    }
    
    public PlayerStats stats(){
        return stats;
    }
    
}

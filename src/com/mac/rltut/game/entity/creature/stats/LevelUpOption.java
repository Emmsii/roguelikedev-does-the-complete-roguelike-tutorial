package com.mac.rltut.game.entity.creature.stats;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:27 AM.
 */
public abstract class LevelUpOption {
    
    private String name;
    
    public LevelUpOption(String name){
        this.name = name;
    }
    
    public abstract void invoke(Creature creature);
    
    public String name(){
        return name;
    }
}

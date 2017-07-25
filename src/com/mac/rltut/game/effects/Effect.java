package com.mac.rltut.game.effects;

import com.mac.rltut.game.entity.creature.Creature;
import jdk.nashorn.internal.ir.Terminal;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 09:15 AM.
 */
public class Effect {
    
    protected int duration;
    
    public Effect(int duration){
        this.duration = duration;
    }
    
    public Effect(Effect other){
        this.duration = other.duration;
    }
    
    public void update(Creature creature){
        duration--;
    }
    
    public void onUseSelf(Creature creature){
        
    }
    
    public void onUseOther(Creature other){
        
    }
    
    public void start(Creature creature){
        
    }
    
    public void stop(Creature creature){
        
    }
    
    public boolean isDone(){
        return duration < 1;
    }
}

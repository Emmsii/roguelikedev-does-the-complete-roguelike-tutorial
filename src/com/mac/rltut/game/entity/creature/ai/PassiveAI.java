package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 08:29 PM.
 */
public class PassiveAI extends CreatureAI{
    
    public PassiveAI(Creature creature) {
        super(creature);
    }
    
    public void update(){
        wander(0.4f);
    }
}

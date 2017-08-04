package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 04/08/2017 at 07:37 PM.
 */
public class PassiveAI extends CreatureAI {
    
    public PassiveAI(){}
    
    public PassiveAI(Creature creature){
        super(creature);
    }

    @Override
    public void update() {
        wander(0.5f);
    }
}

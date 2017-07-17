package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/07/2017 at 09:07 AM.
 */
public class PlayerAI extends CreatureAI{
    
    public PlayerAI(Creature creature) {
        super(creature);
    }

    @Override
    public void notify(String message) {
        //message log 
    }
}

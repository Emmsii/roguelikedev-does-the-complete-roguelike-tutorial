package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 08:29 PM.
 */
public class AggressiveAI extends CreatureAI{
    
    public AggressiveAI(Creature creature) {
        super(creature);
    }

    @Override
    public void update() {
        if(creature.canSee(creature.world().player())){
            //hunt
        }else{
            wander(0.5f);
        }
    }
}

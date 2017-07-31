package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 08:03 PM.
 */
public class BossAI extends CreatureAI{
    
    private PackAI pack;

    public BossAI() {}
    
    public BossAI(Creature creature) {
        super(creature);
    }
    
    public void setPack(PackAI pack){
        this.pack = pack;
    }
}

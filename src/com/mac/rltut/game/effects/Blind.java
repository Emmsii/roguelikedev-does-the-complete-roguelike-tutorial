package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Blind extends Effect{
    
    public Blind(){}
    
    public Blind(int duration){
        super("blind", "loose vision for " + duration + " turns", duration, 1f, false);
    }

    @Override
    public void start(Creature creature) {
        creature.modifyVision(-creature.vision() + 3);
        creature.doAction(new ColoredString("loose eyesight"));
    }

    @Override
    public void stop(Creature creature) {
        creature.modifyVision(creature.vision());
    }
}

package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Freeze extends Effect{

    protected Freeze(){}
    
    public Freeze(int duration){
        super("freeze", "freezing", "freeze another creature for " + duration + " turns", duration, 1f, false);
    }

    @Override
    public void start(Creature creature) {
        creature.addFlag("frozen");
        creature.announce(new ColoredString("is frozen"));
    }

    @Override
    public void stop(Creature creature) {
        creature.removeFlag("frozen");
        creature.announce(new ColoredString("is no longer frozen"));
    }
}

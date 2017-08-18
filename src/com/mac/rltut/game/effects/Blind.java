package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Blind extends Effect{

    protected Blind(){}

    public Blind(int duration, float chance){
        super("blind", "blinding", new ColoredString("blinded"), "loose vision for " + duration + " turns", duration, chance);
    }

    @Override
    public void start(Creature creature) {
        creature.modifyVision(-creature.vision() + 3);
        creature.announce(new ColoredString("is blinded"));
    }

    @Override
    public void stop(Creature creature) {
        creature.modifyVision(creature.vision());
    }
}

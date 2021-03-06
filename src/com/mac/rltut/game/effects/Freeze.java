package com.mac.rltut.game.effects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Freeze extends Effect{

    protected Freeze(){}
    
    public Freeze(int duration, float chance){
        super("freeze", "freezing", "freeze another creature for " + duration + " turns", duration, chance, Sprite.get("ui_frozen"));
    }

    @Override
    public void start(Creature creature) {
        creature.addFlag("frozen");
        creature.doAction(new ColoredString("become frozen"));
    }

    @Override
    public void stop(Creature creature) {
        creature.removeFlag("frozen");
        creature.doAction(new ColoredString("become unfrozen"));
    }
}

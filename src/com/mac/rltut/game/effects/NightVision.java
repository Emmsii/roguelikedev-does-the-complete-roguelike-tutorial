package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class NightVision extends Effect{
    
    private int amount;

    public NightVision(){}
    
    public NightVision(int amount, int duration){
        super("night vision", "increase vision by " + amount + " for " + duration + " turns", duration, 1f, true);
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.modifyVisionBonus(amount);
        creature.doAction(new ColoredString("look further into the forest"));
    }

    @Override
    public void stop(Creature creature) {
        creature.modifyVisionBonus(-amount);
    }
}

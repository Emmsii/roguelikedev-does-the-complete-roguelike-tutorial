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

    protected NightVision(){}
    
    public NightVision(int amount, int duration, float chance){
        super("night vision", "night vision", "increase vision by " + amount + " for " + duration + " turns", duration, chance, true);
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.setVisionBonus(amount);
        creature.doAction(new ColoredString("see further into the forest"));
    }

    @Override
    public void stop(Creature creature) {
        creature.setVisionBonus(0);
        creature.notify(new ColoredString("your vision returns to normal"));
    }
}

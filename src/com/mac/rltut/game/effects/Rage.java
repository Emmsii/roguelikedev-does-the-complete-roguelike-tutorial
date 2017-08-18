package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Rage extends Effect{
    
    private int amount;

    protected Rage(){}
    
    public Rage(int amount, int duration, float chance){
        super("rage", "rage", new ColoredString("enraged", Colors.RED), "sacrifice " + amount + " for " + (-amount) + " for " + duration + " turns", duration, chance);
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        if(creature.defense() - amount < 0) amount = creature.defense();
        creature.modifyStrength(amount);
        creature.modifyDefense(-amount);
        creature.doAction(new ColoredString("enter a fit of rage"));
    }

    @Override
    public void stop(Creature creature) {
        creature.modifyStrength(-amount);
        creature.modifyDefense(amount);
        creature.doAction(new ColoredString("calm down"));
    }
}

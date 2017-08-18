package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Burn extends Effect{
    
    private int amount;

    public Burn(){}

    public Burn(int amount, int duration, float chance){
        super("burn", "burn",  new ColoredString("burning", Colors.RED), "take " + amount + " damage for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance);
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.doAction(new ColoredString("burn"));
    }

    @Override
    public void update(Creature creature) {
        super.update(creature);
        creature.modifyHp(-amount, "burning");
    }
}

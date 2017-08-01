package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class ManaRegen extends Effect{
    
    private int amount;

    public ManaRegen(){}
    
    public ManaRegen(int amount, int duration){
        super("regen mana", "regenerate " + amount + " mana per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, 1f, true);
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.doAction(new ColoredString("feel magic slowly coming back to you..."));
    }

    @Override
    public void update(Creature creature) {
        super.update(creature);
        creature.modifyMana(amount);
    }
}
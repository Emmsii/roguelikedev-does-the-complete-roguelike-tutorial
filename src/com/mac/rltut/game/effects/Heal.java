package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Heal extends Effect{
    
    private int amount;

    public Heal(){}
    
    public Heal(int amount){
        super("heal", "gain health", 1, 1f, true);
        this.amount = amount;
    }

    @Override
    public void onUseSelf(Creature creature) {
        creature.modifyHp(amount, "too much health");
        creature.doAction(new ColoredString("feel restored"));
    }
}
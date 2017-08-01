package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Leach extends Effect{
    
    private int amount;

    public Leach(){}
    
    public Leach(int amount, float chance){
        super("life leach", "steal " + amount + " health from another", 1, chance, true);
        this.amount = amount;
    }

    @Override
    public void onUseOther(Creature other) {
        other.modifyHp(-amount, "life leach");
    }

    @Override
    public void onUseSelf(Creature creature) {
        creature.modifyHp(amount, "too much health");
        creature.doAction(new ColoredString("leach " + amount + " life"));
    }
}

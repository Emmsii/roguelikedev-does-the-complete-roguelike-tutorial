package com.mac.rltut.game.effects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class HealthRegen extends Effect{
    
    private int amount;

    public HealthRegen(){}
    
    public HealthRegen(int amount, int duration, float chance){
        super("regen health", "health regen", "regenerate " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, Sprite.get("ui_healing"));
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.doAction(new ColoredString("feel life slowly coming back to you..."));
    }

    @Override
    public void update(Creature creature) {
        super.update(creature);
        creature.modifyHp(amount, "too much health");
    }
}

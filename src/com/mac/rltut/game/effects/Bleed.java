package com.mac.rltut.game.effects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Bleed extends Effect{
    
    private int amount;

    public Bleed(){}

    public Bleed(int amount, int duration, float chance){
        super("bleed", "bleeding", "take " + amount + " damage for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, Sprite.get("ui_bleeding"));
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.doAction(new ColoredString("bleed"));
    }

    @Override
    public void update(Creature creature) {
        super.update(creature);
        creature.modifyHp(-amount, "bleeding");
        if(Math.random() < 0.5) creature.world().level(creature.z).setBlood(creature.x, creature.y, true);
    }
}

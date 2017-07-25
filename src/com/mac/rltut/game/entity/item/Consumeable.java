package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:04 PM.
 */
public class Consumeable extends Item {
    
    private Effect effect;
    
    public Consumeable(String name, String description, Sprite sprite, int spawnChance, Effect effect) {
        super(name, description, sprite, spawnChance);
        this.effect = effect;
    }
    
    public void consume(Creature creature){
        effect.start(creature);
        creature.inventory().remove(this);
    }
}

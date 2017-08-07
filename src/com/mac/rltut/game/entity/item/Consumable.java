package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:04 PM.
 */
public class Consumable extends Item {
    
    private Effect effect;
    private String action;

    protected Consumable() {}
    
    public Consumable(String name, String description, Sprite sprite, String action, Effect effect) {
        super(name, description, sprite);
        this.effect = effect;
        this.action = action;
    }
    
    public void consume(Creature creature){
        creature.doAction(new ColoredString("%s the %s"), action, name);
        if(effect != null) creature.addEffect(effect);
        creature.inventory().remove(this);
    }
    
    public Effect effect(){
        return effect;
    }
    
    public String action(){
        return action;
    }
}

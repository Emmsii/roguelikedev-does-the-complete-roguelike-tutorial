package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.Effect;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 08:55 AM.
 */
public class Potion extends Consumable{

    protected Potion(){}
    
    public Potion(String name, String description, Sprite sprite, Effect effect) {
        super(name, description, sprite, "drink", effect);
    }
}

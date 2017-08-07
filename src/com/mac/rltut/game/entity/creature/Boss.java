package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 14/07/2017 at 01:41 PM.
 */
public class Boss extends Creature{

    protected Boss(){}
    
    public Boss(String name, String description, Sprite sprite) {
        this(name, description, sprite, 1);
    }

    public Boss(String name, String description, Sprite sprite, int size) {
        super(name, description, sprite, size, "boss");
    }
}

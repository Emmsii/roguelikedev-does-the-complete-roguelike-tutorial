package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 14/07/2017 at 01:41 PM.
 */
public class Boss extends Creature{
    
    public Boss(String name, Sprite sprite) {
        this(name, sprite, 1);
    }

    public Boss(String name, Sprite sprite, int size) {
        super(name, sprite, size);
    }
}

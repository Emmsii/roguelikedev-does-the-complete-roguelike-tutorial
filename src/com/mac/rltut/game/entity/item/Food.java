package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:04 PM.
 */
public class Food extends Item{
    
    private int healAmount;
    
    public Food(String name, String description, Sprite sprite) {
        super(name, description, sprite);
    }
}

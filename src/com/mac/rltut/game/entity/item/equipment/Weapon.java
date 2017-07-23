package com.mac.rltut.game.entity.item.equipment;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.item.Item;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 10:13 AM.
 */
public class Weapon extends Item {
    
    private String damage;
    
    public Weapon(String name, String description, Sprite sprite, String damage) {
        super(name, description, sprite);
        this.damage = damage;
    }
    
    public String damage(){
        return damage;
    }

    @Override
    public String name() {
        return super.name() + " [" + damage + "]";
    }
}

package com.mac.rltut.game.entity.item.equipment;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.item.Item;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 10:13 AM.
 */
public class Armor extends Item {
    
    private int defenceBonus;
    
    public Armor(String name, String description, Sprite sprite, int defenceBonus) {
        super(name, description, sprite);
        this.defenceBonus = defenceBonus;
    }
    
    public int defenceBonus(){
        return defenceBonus;
    }
}

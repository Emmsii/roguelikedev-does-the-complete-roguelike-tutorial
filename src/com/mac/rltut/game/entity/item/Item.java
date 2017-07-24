package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.Entity;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 09:11 AM.
 */
public class Item extends Entity {
    
    private int spawnChance;
    
    public Item(String name, String description, Sprite sprite, int spawnChance) {
        super(name, description, sprite);
        this.spawnChance = spawnChance;
    }

    @Override
    public void update() {
        
    }
    
    public int spawnChance(){
        return spawnChance;
    }
}

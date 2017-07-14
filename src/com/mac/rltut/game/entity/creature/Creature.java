package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.Entity;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 28/06/2017 at 03:13 PM.
 */
public class Creature extends Entity {
    
    private int size;

    public Creature(String name, Sprite sprite) {
        this(name, sprite, 1);
    }
        
    public Creature(String name, Sprite sprite, int size) {
        super(name, sprite);
        this.size = size;
    }

    @Override
    public void update() {
        
    }
    
    public int size(){
        return size;
    }
}

package com.mac.rltut.game.world.objects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.Entity;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 02:33 PM.
 */
public abstract class MapObject extends Entity{

    protected MapObject(){}
    
    public MapObject(String name, String description, Sprite sprite) {
        super(name, description, sprite);
    }
}

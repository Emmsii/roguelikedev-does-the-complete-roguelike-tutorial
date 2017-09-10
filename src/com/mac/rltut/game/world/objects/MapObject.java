package com.mac.rltut.game.world.objects;

import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.world.tile.Tile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 02:33 PM.
 */
public abstract class MapObject extends Entity{

    protected Tile tile;
    
    protected MapObject(){}
    
    public MapObject(String name, String description, Tile tile) {
        super(name, description, tile.sprite());
        this.tile = tile;
    }
    
    public Tile tile(){
        return tile;
    }
}

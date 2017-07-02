package com.mac.rltut.game.map.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:20 AM.
 */
public class EmptyTile extends Tile{
    
    public EmptyTile(Sprite sprite, String type) {
        super(sprite, type);
        this.solid = false;
        this.see = true;
        this.fly = true;
    }
}

package com.mac.rltut.game.map.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:22 AM.
 */
public class ImpassableTile extends Tile{
    
    public ImpassableTile(int id, Sprite sprite, String type) {
        super(id, sprite, type);
        this.solid = true;
        this.see = true;
        this.fly = true;
    }
}

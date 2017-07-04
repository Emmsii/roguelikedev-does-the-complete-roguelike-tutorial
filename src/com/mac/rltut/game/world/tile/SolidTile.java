package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:19 AM.
 */
public class SolidTile extends Tile{
    
    public SolidTile(Sprite sprite, String type) {
        super(sprite, type);
        this.solid = true;
        this.see = false;
        this.fly = false;
    }
}

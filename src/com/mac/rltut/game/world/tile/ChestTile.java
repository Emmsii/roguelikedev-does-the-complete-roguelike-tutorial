package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:19 AM.
 */
public class ChestTile extends Tile{
    
    private Sprite openSprite;

    public ChestTile(byte id, String name, Sprite closedSprite, Sprite openSprite, String type, boolean solid, boolean canSee, boolean canFly) {
        super(id, name, closedSprite, type, solid, canSee, canFly);
        this.openSprite = openSprite;
    }

    public Sprite closedSprite(){
        return sprite();
    }
    
    public Sprite openSprite(){
        return openSprite;
    }
}

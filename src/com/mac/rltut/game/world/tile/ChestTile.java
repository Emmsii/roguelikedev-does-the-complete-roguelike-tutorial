package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:19 AM.
 */
public class ChestTile extends Tile{
    
    private Sprite openSprite;
    private Sprite closedSprite;
    
    public ChestTile() {}
    
    public ChestTile(byte id, String name, String description, Sprite closedSprite, Sprite openSprite, String type, boolean solid, boolean canSee, boolean canFly) {
        super(id, name, description, closedSprite, type, solid, canSee, canFly);
        this.openSprite = openSprite;
        this.closedSprite = closedSprite;
    }

    public Sprite closedSprite(){
        return closedSprite;
    }
    
    public Sprite openSprite(){
        return openSprite;
    }
}

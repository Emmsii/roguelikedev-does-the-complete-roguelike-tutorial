package com.mac.rltut.game.map.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:19 AM.
 */
public class ChestTile extends Tile{
    
    private Sprite openSprite;
    
    public ChestTile(Sprite closedSprite, Sprite openSprite, String type) {
        super(closedSprite, type);
        this.openSprite = openSprite;
        this.solid = true;
        this.see = false;
        this.fly = false;
    }
    
    public Sprite closedSprite(){
        return sprite();
    }
    
    public Sprite openSprite(){
        return openSprite;
    }
}

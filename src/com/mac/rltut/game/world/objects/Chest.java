package com.mac.rltut.game.world.objects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.world.tile.ChestTile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 02:33 PM.
 */
public class Chest extends MapObject {
    
    private ChestTile tile;
    private Inventory<Item> inventory;
    private boolean open;
    
    public Chest(ChestTile tile) {
        super(tile.name(), tile.description(), tile.sprite());
        this.tile = tile;
        this.inventory = new Inventory<Item>(6);
    }

    @Override
    public void update() {
        
    }
    
    public Inventory<Item> inventory(){
        return inventory;
    }

    public void setOpen(boolean value){
        open = value;
    }
    
    public boolean isOpen(){
        return open;
    }
    
    @Override
    public Sprite sprite() {
        return open ? tile.openSprite() : tile.closedSprite();
    }
    
}

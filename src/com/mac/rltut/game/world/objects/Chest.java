package com.mac.rltut.game.world.objects;

import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.world.tile.Tile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 02:33 PM.
 */
public class Chest extends MapObject {
    
    private Inventory<Item> inventory;

    protected Chest(){}
    
    public Chest(Tile tile) {
        super(tile.name(), tile.description(), tile);
        this.inventory = new Inventory<Item>(6);
    }

    @Override
    public void update() {
        
    }
    
    public Inventory<Item> inventory(){
        return inventory;
    }
    
}

package com.mac.rltut.game.entity.item;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:42 PM.
 */
public class ItemStack extends Item{
    
    private int amount;
    
    public ItemStack(String name, String description, Sprite sprite, int amount) {
        super(name, description, sprite);
        this.amount = amount;
    }
    
    public void modifyAmount(int amount){
        Log.debug("AMOUNT: " + amount);
        this.amount += amount;
    }
    
    public int amount(){
        return amount;
    }
}

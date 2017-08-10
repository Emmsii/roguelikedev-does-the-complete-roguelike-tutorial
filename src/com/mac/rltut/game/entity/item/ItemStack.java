package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:42 PM.
 */
public class ItemStack extends Item{
    
    private String spawnAmount;
    private int amount;

    protected ItemStack(){}
    
    public ItemStack(String name, String description, Sprite sprite, String spawnAmount, int amount) {
        super(name, description, sprite);
        this.spawnAmount = spawnAmount;
        this.amount = amount;
    }
    
    public void modifyAmount(int amount){
        this.amount += amount;
    }
    
    public void setAmount(int amount){
        this.amount = amount;
    }
    
    public String spawnAmount(){
        return spawnAmount;
    }
    
    public int amount(){
        return amount;
    }
}

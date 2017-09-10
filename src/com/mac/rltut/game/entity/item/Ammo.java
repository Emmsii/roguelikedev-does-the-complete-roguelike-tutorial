package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/09/2017 at 02:25 PM.
 */
public class Ammo extends ItemStack{

    private boolean equipped;
    
    public Ammo(){}
    
    public Ammo(String name, String description, Sprite sprite, String spawnAmount, int amount){
        super(name, description, sprite, spawnAmount, amount);
        equipped = false;
    }
    
    public void setEquipped(boolean equipped){
        this.equipped = equipped;
    }
    
    public boolean equipped(){
        return equipped;
    }
}

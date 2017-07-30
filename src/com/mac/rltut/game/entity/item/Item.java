package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.Entity;

import java.io.DataInputStream;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 09:11 AM.
 */
public class Item extends Entity {
    
    private boolean unique;

    protected int strengthBonus;
    protected int defenseBonus;
    protected int accuracyBonus;
    protected int intelligenceBonus;
    protected int manaRegenAmountBonus;
    protected int manaRegenSpeedBonus;
    
    public Item(String name, String description, Sprite sprite) {
        super(name, description, sprite);
        this.unique = false;
    }

    @Override
    public void update() {
        
    }
    
    public void setUnique(boolean unique){
        this.unique = unique;
    }
    
    public void setStrengthBonus(int strengthBonus){
        this.strengthBonus = strengthBonus;
    }

    public void setDefenseBonus(int defenseBonus){
        this.defenseBonus = defenseBonus;
    }

    public void setAccuracyBonus(int accuracyBonus){
        this.accuracyBonus = accuracyBonus;
    }

    public void setIntelligenceBonus(int intelligenceBonus){
        this.intelligenceBonus = intelligenceBonus;
    }

    public void setManaRegenAmountBonus(int manaRegenAmountBonus){
        this.manaRegenAmountBonus = manaRegenAmountBonus;
    }

    public void setManaRegenSpeedBonus(int manaRegenSpeedBonus){
        this.manaRegenSpeedBonus = manaRegenSpeedBonus;
    }
    
    public boolean unique(){
        return unique;
    }

    public int strengthBonus(){
        return strengthBonus;
    }

    public int defenseBonus(){
        return defenseBonus;
    }

    public int accuracyBonus(){
        return accuracyBonus;
    }

    public int intelligenceBonus(){
        return intelligenceBonus;
    }

    public int manaRegenAmountBonus(){
        return manaRegenAmountBonus;
    }

    public int manaRegenSpeedBonus(){
        return manaRegenSpeedBonus;
    }

}

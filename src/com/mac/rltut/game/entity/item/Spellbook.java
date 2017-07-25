package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.Effect;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{
    
    private Effect blessing;
    
    private int strengthBonus;
    private int defenseBonus;
    private int accuracyBonus;
    private int intelligenceBonus;
    
    private int manaRegenAmountBonus;
    private int manaRegenSpeedBonus;
    
    public Spellbook(String name, String description, Sprite sprite, int spawnChance, Effect blessing) {
        super(name, description, sprite, spawnChance);
    }

    public void bless(Equippable equippable){
        equippable.setUnique(true);
        equippable.setName("Unique " + equippable.name());
        
        equippable.setStrengthBonus(strengthBonus);
        equippable.setDefenseBonus(defenseBonus);
        equippable.setAccuracyBonus(accuracyBonus);
        equippable.setIntelligenceBonus(intelligenceBonus);
        equippable.setManaRegenAmountBonus(manaRegenAmountBonus);
        equippable.setManaRegenSpeedBonus(manaRegenSpeedBonus);
        
        if(blessing != null) equippable.setEffect(blessing);
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
}

package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 24/07/2017 at 03:33 PM.
 */
public class Equippable extends Item{

    protected String slot;
    protected boolean equipped;
    
    private int strengthBonus;
    private int defenseBonus;
    private int accuracyBonus;
    private int intelligenceBonus;
    
    private int manaRegenAmountBonus;
    private int manaRegenSpeedBonus;
    
    private String damage;
    
    private Effect effect;
    
    public Equippable(String name, String description, Sprite sprite, int spawnChance, String slot) {
        super(name, description, sprite, spawnChance);
        this.slot = slot;
        this.equipped = false;
    }
    
    public void equip(Creature creature){
        Equippable alreadyEquipped = creature.getEquippedAt(slot);
        if(alreadyEquipped != null && alreadyEquipped != this) creature.unequip(creature.getEquippedAt(slot));

        if(isEquipped()){
            unequip(creature);
            return;
        }
        
        creature.setEquippable(slot, this);
        creature.doAction(new ColoredString("equip a %s"), name);
        equipped = true;
    }
    
    public void unequip(Creature creature){
        Equippable alreadyEquipped = creature.getEquippedAt(slot);
        if(alreadyEquipped != null) creature.doAction(new ColoredString("unequip a %s"), name);
        creature.setEquippable(slot, null);
        equipped = false;
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

    public void setDamage(String damage){
        this.damage = damage;
    }

    public void setEffect(Effect effect){
        this.effect = effect;
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
    
    public String damage(){
        return damage;
    }
    
    public Effect effect(){
        return effect;
    }
    
    public String slot(){
        return slot;
    }
    
    public boolean isEquipped(){
        return equipped;
    }
}
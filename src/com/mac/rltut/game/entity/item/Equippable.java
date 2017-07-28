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

    protected EquipmentSlot slot;
    protected boolean equipped;
    
    private String damage;
    private String rangedDamage;
    
    private Effect effect;
    
    public Equippable(String name, String description, Sprite sprite, EquipmentSlot slot) {
        super(name, description, sprite);
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

    public void setDamage(String damage){
        this.damage = damage;
    }
    
    public void setRangedDamage(String rangedDamage){
        this.rangedDamage = rangedDamage;
    }

    public void setEffect(Effect effect){
        this.effect = effect;
    }
    
    public String damage(){
        return damage;
    }
    
    public String rangedDamage(){
        return rangedDamage;
    }
    
    public Effect effect(){
        return effect;
    }
    
    public EquipmentSlot slot(){
        return slot;
    }
    
    public boolean isEquipped(){
        return equipped;
    }
}

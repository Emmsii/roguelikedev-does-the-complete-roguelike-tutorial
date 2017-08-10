package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
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
    protected EquipmentSlot blockedSlot;
    
    private String damage;
    private String rangedDamage;
    
    private Effect effect;

    protected Equippable(){}
    
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
        
        if(blockedSlot != null){
            Equippable blocked = creature.getEquippedAt(blockedSlot);
            if(blocked != null){
                creature.notify(new ColoredString("You cannot equip a %s with a %s equipped.", Colors.ORANGE), StringUtil.capitalizeEachWord(name), StringUtil.capitalizeEachWord(blocked.name()));
                return;
            }
        }
        
        for (Equippable equipped : creature.equippedItems().values()) {
            if (equipped == null || equipped.blockedSlot == null) continue;
            if (equipped.blockedSlot == slot) {
                creature.notify(new ColoredString("You cannot equip a %s with a %s equipped.", Colors.ORANGE), StringUtil.capitalizeEachWord(name), StringUtil.capitalizeEachWord(equipped.name()));
                return;
            }
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
        
    public int score(){
        float result = 0;
        
        if(damage != null) {
            String[] splitDamage = damage.split("d");
            int rolls = Integer.parseInt(splitDamage[0].trim());
            int sides = Integer.parseInt(splitDamage[1].trim());
            result += sides * rolls;
        }

        if(rangedDamage != null) {
            result *= 0.75f;
            String[] splitDamage = rangedDamage.toLowerCase().split("d");
            int rolls = Integer.parseInt(splitDamage[0].trim());
            int sides = Integer.parseInt(splitDamage[1].trim());
            result += sides * rolls;
        }
        
        result += strengthBonus;
        result += defenseBonus;
        result += accuracyBonus;
        result += intelligenceBonus;
        result += manaRegenAmountBonus / 2;
        result += manaRegenSpeedBonus / 2;
        
        if(effect != null) result *= 1.35f;
        
        return (int) result;
    }

    public void setDamage(String damage){
        this.damage = damage;
    }
    
    public void setRangedDamage(String rangedDamage){
        this.rangedDamage = rangedDamage;
    }

    public void setBlockedSlot(String blockedSlot){
        if(blockedSlot == null) this.blockedSlot = null;
        else this.blockedSlot = EquipmentSlot.valueOf(blockedSlot.toUpperCase().trim());
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

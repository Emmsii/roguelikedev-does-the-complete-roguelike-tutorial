package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

import java.awt.*;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{
    
    private Effect effect;
    private int manaCost;
    private EquipmentSlot[] slots;
    
    public Spellbook(String name, String description, Sprite sprite, EquipmentSlot[] slots) {
        super(name, description, sprite);
        this.slots = slots;
    }

    public void bless(Creature creature, Equippable equippable){
        creature.doAction(new ColoredString("bless the %s", Color.CYAN.getRGB()), equippable.name());
        equippable.setUnique(true);
        equippable.setName("Blessed " + equippable.name());
        
        equippable.setStrengthBonus(strengthBonus);
        equippable.setDefenseBonus(defenseBonus);
        equippable.setAccuracyBonus(accuracyBonus);
        equippable.setIntelligenceBonus(intelligenceBonus);
        equippable.setManaRegenAmountBonus(manaRegenAmountBonus);
        equippable.setManaRegenSpeedBonus(manaRegenSpeedBonus);
        
        if(effect != null) equippable.setEffect(effect);
        
        creature.inventory().remove(this);
        creature.notify(new ColoredString("The book looses its magic!"));
    }
    
    public void setEffect(Effect effect){
        this.effect = effect;
    }
    
    public void setManaCost(int manaCost){
        this.manaCost = manaCost;
    }
    
    public Effect effect(){
        return new Effect(effect);
    }
    
    public int manaCost(){
        return manaCost;
    }
    
    public boolean validSlot(EquipmentSlot slot){
        for(EquipmentSlot s : slots) if(s == slot) return true;
        return false;
    }
}

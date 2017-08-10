package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{
    
    private Effect effect;
    private int manaCost;
    private EquipmentSlot[] slots;

    protected Spellbook(){}
    
    public Spellbook(String name, String description, Sprite sprite, EquipmentSlot[] slots) {
        super(name, description, sprite);
        this.slots = slots;
    }

    public void bless(Creature creature, Equippable equippable){
        if(creature.mana() < manaCost) return;
        creature.modifyMana(-manaCost);
        
        creature.doAction(new ColoredString("enhance the %s", Colors.BLUE), equippable.name());
        equippable.setUnique(true);

        if(effect == null) equippable.setName("Enhanced " + equippable.name());
        else equippable.setName("Magical " + equippable.name());
        
        equippable.setStrengthBonus(strengthBonus);
        equippable.setDefenseBonus(defenseBonus);
        equippable.setAccuracyBonus(accuracyBonus);
        equippable.setIntelligenceBonus(intelligenceBonus);
        equippable.setManaRegenAmountBonus(manaRegenAmountBonus);
        equippable.setManaRegenSpeedBonus(manaRegenSpeedBonus);
        
        if(effect != null) equippable.setEffect(effect);
        
        creature.inventory().remove(this);
        creature.notify(new ColoredString("The book looses its magic and vanishes!"));
    }
    
    public void setEffect(Effect effect){
        this.effect = effect;
    }
    
    public void setManaCost(int manaCost){
        this.manaCost = manaCost;
    }
    
    public Effect effect(){
        if(effect == null) return null;
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

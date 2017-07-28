package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;

import java.awt.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{
    
    private Effect blessing;
    
    public Spellbook(String name, String description, Sprite sprite, Effect blessing) {
        super(name, description, sprite);
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
        
        if(blessing != null) equippable.setEffect(blessing);
        
        creature.inventory().remove(this);
        creature.notify(new ColoredString("The book looses its magic!"));
    }
}

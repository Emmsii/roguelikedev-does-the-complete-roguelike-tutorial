package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:04 PM.
 */
public class Food extends Item{
    
    private int healAmount;
    
    public Food(String name, String description, Sprite sprite, int spawnChance, int healAmount) {
        super(name, description, sprite, spawnChance);
        this.healAmount = healAmount;
    }
    
    public void consume(Creature creature){
        creature.modifyHp(healAmount, "Was healed by some food.");
        creature.inventory().remove(this);
    }
}

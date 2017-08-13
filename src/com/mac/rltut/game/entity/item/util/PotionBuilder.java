package com.mac.rltut.game.entity.item.util;

import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.EffectBuilder;
import com.mac.rltut.game.entity.item.Consumable;
import com.mac.rltut.game.entity.item.Potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 06:34 PM.
 */
public class PotionBuilder {
    
    public static Potion randomPotion(int z, Random random){
        Effect potionEffect = EffectBuilder.randomPotionEffect(z, random);
        
        List<Potion> potions = new ArrayList<Potion>();
        potions.add((Potion) Codex.items.get("red potion").entity());
        potions.add((Potion) Codex.items.get("blue potion").entity());
        potions.add((Potion) Codex.items.get("purple potion").entity());
        potions.add((Potion) Codex.items.get("green potion").entity());
        potions.add((Potion) Codex.items.get("yellow potion").entity());

        Potion potion = potions.get(random.nextInt(potions.size()));
        return new Potion(potionEffect.adjective() + " potion", potion.description(), potion.sprite(), potionEffect);
    }
}

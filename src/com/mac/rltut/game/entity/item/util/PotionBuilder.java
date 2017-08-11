package com.mac.rltut.game.entity.item.util;

import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.EffectBuilder;
import com.mac.rltut.game.entity.item.Consumable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 06:34 PM.
 */
public class PotionBuilder {
    
    public static Consumable randomPotion(int z, Random random){
        Effect potionEffect = EffectBuilder.randomPotionEffect(z, random);
        List<Consumable> potions = new ArrayList<Consumable>();
        potions.add((Consumable) Codex.items.get("red potion").entity());
        potions.add((Consumable) Codex.items.get("blue potion").entity());
        potions.add((Consumable) Codex.items.get("purple potion").entity());
        potions.add((Consumable) Codex.items.get("green potion").entity());
        potions.add((Consumable) Codex.items.get("yellow potion").entity());
        
        Consumable potion = potions.get(random.nextInt(potions.size()));
        return new Consumable(potionEffect.adjective() + " potion", potion.description(), potion.sprite(), potion.action(), potionEffect);
    }
}

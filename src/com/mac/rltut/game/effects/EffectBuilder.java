package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/07/2017 at 02:08 PM.
 */
public class EffectBuilder {
    
    public EffectBuilder() {}
    
    public static Effect randomItemEffect(int z, Random random){
        Pool<Effect> pool = new Pool<Effect>(random);
        pool.add(new Leach(5 + ((z / 3) * 5), 0.1f), 30);
        pool.add(new EffectOther(new Poison(1 + ((z / 2) + 1), 5 + ((z / 3) * 2), 0.75f)), 100);
        pool.add(new EffectOther(new Blind(10 + (z / 4) * 2)), 50);
        return pool.get();
    }
    
    public static Effect randomPotionEffect(int z, Random random){
        Pool<Effect> pool = new Pool<Effect>(random);
        
        pool.add(new Heal(10 + (z / 2) * 2), 100);
        pool.add(new HealthRegen(2 + (z / 3) + 1, 5 + (z / 3) + 1), 100);
        pool.add(new ManaRegen(2 + z, 10 + (z / 3) + 1), 80);
        pool.add(new Rage(3 + z, 15 + (z / 5) * 5), 25);
        pool.add(new NightVision(16, 20 + (z / 4) * 4), 40);
        
        return pool.get();
    }
    
//    public static Effect effectOther(Effect effect){
//        return new Effect(effect.name, effect.description, 1, 1f, true){
//            @Override
//            public void onUseOther(Creature other) {
//                other.addEffect(effect);
//            }
//        };
//    }

//    public static Effect heal(int amount){
//        return new Effect("heal", "gain health", 1, 1f, true){
//            @Override
//            public void onUseSelf(Creature creature) {
//                if(Math.random() > chance) return;
//                creature.modifyHp(amount, "too much health");
//                creature.doAction(new ColoredString("feel restored"));
//            }
//        };
//    }
//    
//    public static Effect healthRegen(int amount, int duration){
//        return new Effect("regen health", "regenerate " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, 1f, false){
//            
//            @Override
//            public void start(Creature creature) {
//                creature.doAction(new ColoredString("feel life slowly coming back to you..."));
//            }
//
//            @Override
//            public void update(Creature creature) {
//                super.update(creature);
//                creature.modifyHp(amount, "too much health");
//            }
//        };
//    }
//
//    public static Effect manaRegen(int amount, int duration){
//        return new Effect("regen mana", "regenerate " + amount + " mana per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, 1f, false){
//
//            @Override
//            public void start(Creature creature) {
//                creature.doAction(new ColoredString("feel magic slowly coming back to you..."));
//            }
//            
//            @Override
//            public void update(Creature creature) {
//                super.update(creature);
//                creature.modifyMana(amount);
//            }
//        };
//    }
//    
//    //TODO: Check if def - amount is < 1. 
//    public static Effect rage(int amount, int duration){
//        return new Effect("rage", "sacrifice " + amount + " for " + (-amount) + " for " + duration + " turns", duration, 1f, false){
//            
//            @Override
//            public void start(Creature creature) {
//                creature.modifyStrength(amount);
//                creature.modifyDefense(-amount);
//                creature.doAction(new ColoredString("entered a fit of rage"));
//            }
//
//            @Override
//            public void stop(Creature creature) {
//                creature.modifyStrength(-amount);
//                creature.modifyDefense(amount);
//                creature.doAction(new ColoredString("calm down"));
//            }
//        };
//    }
//    
//    public static Effect nightVision(int amount, int duration){
//        return new Effect("night vision", "increase vision by " + amount + " for " + duration + " turns", duration, 1f, false){
//            @Override
//            public void start(Creature creature) {
//                creature.modifyVisionBonus(amount);
//                creature.doAction(new ColoredString("look further into the forest"));
//            }
//
//            @Override
//            public void stop(Creature creature) {
//                creature.modifyVisionBonus(-amount);
//            }
//        };
//    }
//    
//    public static Effect blind(int duration){
//        return new Effect("blind", "loose vision for " + duration + " turns", duration, 1f, false){
//            @Override
//            public void start(Creature creature) {
//                creature.modifyVision(-creature.vision() + 3);
//                creature.doAction(new ColoredString("loose eyesight"));
//            }
//
//            @Override
//            public void stop(Creature creature) {
//                creature.modifyVision(creature.vision());
//            }
//        };
//    }
//    
//    public static Effect poison(int amount, int duration, float chance){
//        return new Effect("poison", "loose " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, false){
//
//            @Override
//            public void start(Creature creature) {
//                creature.doAction(new ColoredString("feel poisoned"));
//            }
//            
//            @Override
//            public void update(Creature creature) {
//                super.update(creature);
//                creature.modifyHp(-amount, "poison");
//            }
//        };
//    }
//    
//    public static Effect leach(int amount, float chance){
//        return new Effect("life leach", "steal " + amount + " health from another", 1, chance, true){
//            
//            @Override
//            public void onUseOther(Creature other) {
//                other.modifyHp(-amount, "life leach");
//            }
//
//            @Override
//            public void onUseSelf(Creature creature) {
//                creature.modifyHp(amount, "too much health");
//                creature.doAction(new ColoredString("leach " + amount + " life"));
//            }
//        };
//    }
}

package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/07/2017 at 02:08 PM.
 */
public class EffectBuilder {
    
    public static Effect randomItemEffect(int z, Random random){
        List<Effect> effects = new ArrayList<Effect>();
        effects.add(effectToOther(poison(1 + ((z / 2) + 1), 5 + ((z / 3) * 2), 0.75f)));
        effects.add(leach(5 + ((z / 3) * 5), 0.1f));
        return effects.get(random.nextInt(effects.size()));
    }
    
    public static Effect effectToOther(Effect effect){
        return new Effect(effect.name, effect.description, 1, 1f, true){
            @Override
            public void onUseOther(Creature other) {
                other.addEffect(effect);
            }
        };
    }
    
    public static Effect heal(int amount, float chance){
        return new Effect("heal", "gain health", 1, chance, true){
            @Override
            public void onUseSelf(Creature creature) {
                if(Math.random() > chance) return;
                creature.modifyHp(amount, "too much health");
            }
        };
    }
    
    public static Effect healthRegen(int amount, int duration, float chance){
        return new Effect("regen health", "regenerate " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, false){
            @Override
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(amount, "too much health");
            }
        };
    }

    public static Effect manaRegen(int amount, int duration){
        return new Effect("regen mana", "regenerate " + amount + " mana per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, 1, false){
            @Override
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyMana(amount);
            }
        };
    }
    
    public static Effect rage(int amount, int duration){
        return new Effect("rage", "sacrifice " + amount + " for " + (-amount) + " for " + duration + " turns", duration, 1, false){
            @Override
            public void start(Creature creature) {
                creature.modifyStrength(amount);
                creature.modifyDefense(-amount);
                creature.doAction(new ColoredString("entered a fit of rage"));
            }

            @Override
            public void stop(Creature creature) {
                creature.modifyStrength(-amount);
                creature.modifyDefense(amount);
                creature.doAction(new ColoredString("calm down"));
            }
        };
    }
    
    public static Effect nightVision(int amount, int duration){
        return new Effect("night vision", "increase vision by " + amount + " for " + duration + " turns", duration, 1, false){
            @Override
            public void start(Creature creature) {
                creature.modifyVisionBonus(amount);
            }

            @Override
            public void stop(Creature creature) {
                creature.modifyVisionBonus(-amount);
            }
        };
    }
    
    public static Effect blind(int duration){
        return new Effect("blind", "loose vision for " + duration + " turns", duration, 1, false){
            @Override
            public void start(Creature creature) {
                creature.modifyVision(-creature.vision() + 3);
                creature.doAction(new ColoredString("loose eyesight"));
            }

            @Override
            public void stop(Creature creature) {
                creature.modifyVision(creature.vision());
            }
        };
    }
    
    public static Effect poison(int amount, int duration, float chance){
        return new Effect("poison", "loose " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, false){
            @Override
            public void update(Creature creature) {
                super.update(creature);
                creature.modifyHp(-amount, "poison");
            }
        };
    }
    
//    public static Effect poisonOther(int amount, int duration, float chance){
//        return new Effect("poison", "poison another creature, loosing " + amount + " health per turn for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, false){
//            @Override
//            public void onUseOther(Creature other) {
//                other.addEffect(poison(amount, duration, chance));
//            }
//        };
//    }
    
    public static Effect leach(int amount, float chance){
        return new Effect("life leach", "steal " + amount + " health from another", 1, chance, true){
            
            @Override
            public void onUseOther(Creature other) {
                other.modifyHp(-amount, "life leach");
            }

            @Override
            public void onUseSelf(Creature creature) {
                creature.modifyHp(amount, "too much health");
                creature.doAction(new ColoredString("leach " + amount + " life"));
            }
        };
    }
}

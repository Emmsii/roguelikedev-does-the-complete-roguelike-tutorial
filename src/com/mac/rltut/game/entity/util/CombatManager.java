package com.mac.rltut.game.entity.util;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.maths.Dice;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.NPC;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 09:32 AM.
 */
public class CombatManager {
    
    private Creature attacker;
    private Creature defender;
    
    public CombatManager(Creature attacker, Creature defender){
        this.attacker = attacker;
        this.defender = defender;
    }
    
    private void commonAttack(Equippable weapon, int damage, String causeOfDeath, ColoredString action, Object ... params){
        attacker.incrementStat("damage_dealt", damage);
        defender.incrementStat("damage_received", damage);
        
        attacker.doAction(action, params);
        defender.damage(damage, causeOfDeath);
        doEffect(weapon, defender);
        if(defender.hp() < 1) attacker.gainXp(defender);
        attacker.setHasPerformedAction(true);
    }
    
    public void meleeAttack(){
        if(attacker.id == defender.id) return;
        if(defender instanceof NPC && !((NPC) defender).canAttack()) return;
        if(attacker instanceof Player) ((Player) attacker).stats().incrementValue("attack_attempt");
        
        int maxAttackerRoll = attacker.accuracy() + attacker.accuracyBonus();
        int maxDefenderRoll = defender.defense() + defender.defenseBonus();
        
        int attackHitRoll = Dice.roll("1d" + maxAttackerRoll);
        int defenderBlockRoll = Dice.roll("1d" + maxDefenderRoll);
        
        if(attackHitRoll > defenderBlockRoll || attackHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            attacker.incrementStat("hits", 1);
            
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage("melee");
            commonAttack(weapon, damage, "a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("attack" + (!defender.isPlayer() ? " the" : "") + " %s for %d damage"), defender.name(), damage);
        }else{
            attacker.incrementStat("misses", 1);
            defender.incrementStat("blocks", 1);
            defender.doAction(new ColoredString("block the attack"));
        }
        defender.setAttackedBy(attacker);
    }
    
    public void rangedAttack(){
        if(attacker.id == defender.id) return;
        if(defender instanceof NPC && !((NPC) defender).canAttack()) return;
        if(attacker instanceof Player) ((Player) attacker).stats().incrementValue("attack_attempt");
        
        int distance = MathUtil.distance(attacker.x, attacker.y, defender.x, defender.y);
        
        int distancePenalty = (int) Math.round(((Math.pow(1.3, distance - (attacker.accuracy() / 2))) - (attacker.accuracy() / 2)) * 0.255);
        if(distancePenalty < 0) distancePenalty = 0;
        
        int attackerHitRoll = Dice.roll("1d" + (attacker.accuracy() + attacker.accuracyBonus())) - distancePenalty;
        int defenderBlockRoll = Dice.roll("1d" + (defender.defense() + defender.defenseBonus()));
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            attacker.incrementStat("hits", 1);
            
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage("ranged");
            commonAttack(weapon, damage, "a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("fire a %s at " + (!defender.isPlayer() ? "the " : "") + "%s for %d damage"), weapon.name(), defender.name(), damage);
        }else{
            attacker.incrementStat("misses", 1);
            defender.incrementStat("blocks", 1);
            
            defender.doAction(new ColoredString("block the arrow"));
        }
        defender.setAttackedBy(attacker);
    }
    
    public void thrownAttack(Item item){
        commonAttack(null, 1, "thrown " + item.name(), new ColoredString("throw a %s at the %s for %d damage"), item.name(), defender.name(), 1);

        if(item instanceof Potion){
            Effect effect = ((Consumable) item).effect();
            if(effect != null && Math.random() <= effect.chance()){
                defender.addEffect(effect.newInstance());
            }else{
                attacker.notify(new ColoredString("the potion shatters on impact but has no effect on the " + defender.name()));
            }
        }
    }
    
    private int getDamage(String type){
        int damage = (attacker.strength() + attacker.strengthBonus()) / 2;
        if(damage < 1) damage = 1;
        Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
        if(weapon != null){
            if(type.equalsIgnoreCase("melee")) damage += Dice.roll(weapon.damage());
            else if(type.equalsIgnoreCase("ranged")) damage += Dice.roll(weapon.rangedDamage());
        }
        return damage;
    }
   
    private static void doEffect(Equippable equippable, Creature defender){
        if(equippable == null || equippable.effect() == null || defender.hp() < 1) return;
        Effect effect = equippable.effect();
        if(Math.random() <= effect.chance()) defender.addEffect(equippable.effect().newInstance());
    }
    
}

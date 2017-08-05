package com.mac.rltut.game.entity.util;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.maths.Dice;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;

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
        defender.damage(damage, causeOfDeath);
        doEffect(weapon, attacker, defender);
        
        attacker.doAction(action, params);
        if(defender.hp() < 1) attacker.gainXp(defender);
    }
    
    public void meleeAttack(){
        if(attacker.id == defender.id) return;
        int attackHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus();
        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();
        
        if(attackHitRoll > defenderBlockRoll || attackHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage();
            commonAttack(weapon, damage, "killed by a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("attack the %s for %d damage"), defender.name(), damage);
        }else{
            defender.doAction(new ColoredString("block the attack"));
        }
        defender.setAttackedBy(attacker);
    }
    
    public void rangedAttack(){
        if(attacker.id == defender.id) return;
        
        int distance = MathUtil.distance(attacker.x, attacker.y, defender.x, defender.y);
        
        int distancePenalty = (int) Math.round(((Math.pow(1.3, distance - (attacker.accuracy() / 2))) - (attacker.accuracy()/ 2)) * 0.25);//TODO: TEMP MAYBE
        if(distancePenalty < 0) distancePenalty = 0;
        
        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus() - distancePenalty;
        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage();
            commonAttack(weapon, damage, "killed by a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("fire a %s at the %s for %d damage"), weapon.name(), defender.name(), damage);
        }else{
            defender.doAction(new ColoredString("block the attack"));
        }
        defender.setAttackedBy(attacker);
    }
    
    private int getDamage(){
        int damage = Dice.roll("1d" + attacker.strength()) + attacker.strengthBonus();
        Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
        if(weapon != null) damage += Dice.roll(weapon.damage());
        return damage;
    }
    
//    public void meleeAttack(){
////        Log.debug("Melee attack between " + attacker.name() + " and " + defender.name());
//        
//        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus();
//        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();
//                
////        Log.debug("Hit Roll: " + attackerHitRoll + "(1d" + attacker.accuracy() + ")");
////        Log.debug("Block Roll: " + defenderBlockRoll + "(1d" + defender.defense() + " + " + defender.defenseBonus() + ")");
//
//        Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
//        int damage = Dice.roll("1d" + attacker.strength()) + attacker.strengthBonus();
//        if(weapon != null) damage += Dice.roll(weapon.damage());
//        
//        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
//            //Attacker hits
////            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + " + " + attacker.strengthBonus() + (weapon != null ? " + " + weapon.damage() : "") + ")");
//            attacker.doAction(new ColoredString("attack the %s for %d damage"), defender.name(), damage);
//            defender.damage(damage, "melee attack from " + attacker.name());
//
//            
//            doEffect(weapon, attacker, defender);
//            
//            if(defender.hp() < 1) attacker.gainXp(defender);
//        }else{
//            //Defender blocks attack
//            defender.doAction(new ColoredString("block the attack"));
//        }
//    }
//    
//    public void rangedAttack(){
//        if(attacker.id == defender.id) return;
////        Log.debug("Ranged attack between " + attacker.name() + " and " + defender.name());
//        
//        int distance = MathUtil.distance(attacker.x, attacker.y, defender.x, defender.y);
//        
//        int distancePenalty = (int) Math.round(((Math.pow(1.3, distance - (attacker.accuracy() / 2))) - (attacker.accuracy()/ 2)) * 0.25);//TODO: TEMP MAYBE
//        if(distancePenalty < 0) distancePenalty = 0;
//        
//        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus() - distancePenalty;
//        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();
//
////        Log.debug("Hit Roll: " + attackerHitRoll + "(1d" + attacker.accuracy() + ")");
////        Log.debug("Block Roll: " + defenderBlockRoll + "(1d" + defender.defense() + " + " + defender.defenseBonus() + ")");
//        
//        Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
//        int damage = Dice.roll("1d" + attacker.strength()) + attacker.strengthBonus();
//        if(weapon != null) damage += Dice.roll(weapon.damage());
//        
//        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
////            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + " + " + attacker.strengthBonus() + (weapon != null ? " + " + weapon.damage() : "") + ")");
//            attacker.doAction(new ColoredString("fire the %s at the %s for %d damage"), StringUtil.capitalizeEachWord(attacker.getEquippedAt(EquipmentSlot.WEAPON).name()), defender.name(), damage);
//            defender.damage(damage, "ranged attack from " + attacker.name());
//            
//            doEffect(weapon, attacker, defender);
//            
//            if(defender.hp() < 1) attacker.gainXp(defender);
//        }else if(attackerHitRoll < defenderBlockRoll && attackerHitRoll < attackerHitRoll / 4){
//            attacker.doAction(new ColoredString("miss completely"));
//        }else{
//            defender.doAction(new ColoredString("block the attack"));
//        }
//    }
    
    private static void doEffect(Equippable equippable, Creature attacker, Creature defender){
        if(equippable == null || equippable.effect() == null || !equippable.effect().canUseWithItem() || defender.hp() < 1) return;
        Effect effect = equippable.effect();
        if(Math.random() > effect.chance()) return;
        effect.onUseSelf(attacker);
        effect.onUseOther(defender);
    }
    
}

package com.mac.rltut.game.entity.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Dice;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.EquipmentSlot;

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
    
    public void meleeAttack(){
        Log.debug("Melee attack between " + attacker.name() + " and " + defender.name());
        
        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus();
        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();
                
        Log.debug("Hit Roll: " + attackerHitRoll + "(1d" + attacker.accuracy() + ")");
        Log.debug("Block Roll: " + defenderBlockRoll + "(1d" + defender.defense() + " + " + defender.defenseBonus() + ")");
        
        int damage = Dice.roll("1d" + attacker.strength()) + attacker.strengthBonus();
        if(attacker.getEquippedAt(EquipmentSlot.WEAPON) != null) damage += Dice.roll(attacker.getEquippedAt(EquipmentSlot.WEAPON).damage());
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            //Attacker hits
            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + " + " + attacker.strengthBonus() + (attacker.getEquippedAt(EquipmentSlot.WEAPON) != null ? " + " + attacker.getEquippedAt(EquipmentSlot.WEAPON).damage() : "") + ")");
            attacker.doAction(new ColoredString("attack the %s for %d damage"), defender.name(), damage);
            defender.damage(damage, "melee attack from " + attacker.name());
            if(defender.hp() < 1) attacker.gainXp(defender);
        }else{
            //Defender blocks attack
            defender.doAction(new ColoredString("block the attack"));
        }
    }
    
    public void rangedAttack(){
        if(attacker.id == defender.id) return;
        Log.debug("Ranged attack between " + attacker.name() + " and " + defender.name());
        
        int distance = MathUtil.distance(attacker.x, attacker.y, defender.x, defender.y);
        
        int distancePenalty = (int) Math.round(((Math.pow(1.3, distance - (attacker.accuracy() / 2))) - (attacker.accuracy()/ 2)) * 0.25);//TODO: TEMP MAYBE
        if(distancePenalty < 0) distancePenalty = 0;
        
        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy()) + attacker.accuracyBonus() - distancePenalty;
        int defenderBlockRoll = Dice.roll("1d" + defender.defense()) + defender.defenseBonus();

        Log.debug("Hit Roll: " + attackerHitRoll + "(1d" + attacker.accuracy() + ")");
        Log.debug("Block Roll: " + defenderBlockRoll + "(1d" + defender.defense() + " + " + defender.defenseBonus() + ")");
        
        int damage = Dice.roll("1d" + attacker.strength()) + attacker.strengthBonus();
        if(attacker.getEquippedAt(EquipmentSlot.WEAPON) != null) damage += Dice.roll(attacker.getEquippedAt(EquipmentSlot.WEAPON).damage());
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + " + " + attacker.strengthBonus() + (attacker.getEquippedAt(EquipmentSlot.WEAPON) != null ? " + " + attacker.getEquippedAt(EquipmentSlot.WEAPON).damage() : "") + ")");
            attacker.doAction(new ColoredString("fire a %s at the %s for %d damage"), attacker.getEquippedAt(EquipmentSlot.WEAPON).name(), defender.name(), damage);
            defender.damage(damage, "ranged attack from " + attacker.name());
            if(defender.hp() < 1) attacker.gainXp(defender);
        }else if(attackerHitRoll < defenderBlockRoll && attackerHitRoll < attackerHitRoll / 4){
            attacker.doAction(new ColoredString("miss completely"));
        }else{
            defender.doAction(new ColoredString("block the attack"));
        }
    }
    
}

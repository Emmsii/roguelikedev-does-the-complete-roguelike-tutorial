package com.mac.rltut.game.entity.creature.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Dice;
import com.mac.rltut.game.entity.creature.Creature;

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
        if(attacker.getEquippedAt("weapon") != null) damage += Dice.roll(attacker.getEquippedAt("weapon").damage());
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            //Attacker hits
            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + (attacker.getEquippedAt("weapon") != null ? " + " + attacker.getEquippedAt("weapon").damage() : "") + ")");
            attacker.doAction(new ColoredString("attack the %s for %d damage"), defender.name(), damage);
            defender.damage(damage, "melee attack from " + attacker.name());
            if(defender.hp() < 1) attacker.gainXp(defender);
        }else{
            //Defender blocks attack
            defender.doAction(new ColoredString("block the attack"));
        }
    }
    
}

package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Dice;

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
        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy());
        int defenderBlockRoll = Dice.roll("1d" + defender.defense());
        
        Log.debug("Hit Roll: " + attackerHitRoll + "(1d" + attacker.accuracy() + ")");
        Log.debug("Block Roll: " + defenderBlockRoll + "(1d" + defender.defense() + ")");
        int damage = Dice.roll("1d" + attacker.strength());
        if(attackerHitRoll > defenderBlockRoll){
            //Attacker hits
            Log.debug("Damage: " + damage + " (1d" + attacker.strength() + ")");
            attacker.doAction(new ColoredString("attack the %s for %d damage"), defender.name(), damage);
            defender.damage(damage, "melee attack from " + attacker.name());
            if(defender.hp() < 1) attacker.gainXp(defender);
        }else if(damage < 1){
            attacker.doAction(new ColoredString("completely miss the %s"), defender.name());
        }else{
            //Defender blocks attack
            defender.doAction(new ColoredString("block the attack"));
        }
        
    }
}

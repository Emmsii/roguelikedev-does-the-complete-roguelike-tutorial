package com.mac.rltut.game.entity.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.maths.Dice;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.NPC;
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
        attacker.doAction(action, params);
        defender.damage(damage, causeOfDeath);
        doEffect(weapon, attacker, defender);
        if(defender.hp() < 1) attacker.gainXp(defender);
        attacker.setHasPerformedAction(true);
    }
    
    public void meleeAttack(){
        if(attacker.id == defender.id || defender instanceof NPC) return;
        int attackHitRoll = Dice.roll("1d" + attacker.accuracy() + attacker.accuracyBonus());
        int defenderBlockRoll = Dice.roll("1d" + defender.defense() + defender.defenseBonus());
        
        if(attackHitRoll > defenderBlockRoll || attackHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage("melee");
            commonAttack(weapon, damage, "killed by a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("attack" + (!defender.isPlayer() ? " the " : "") + " %s for %d damage"), defender.name(), damage);
        }else{
            defender.doAction(new ColoredString("block the attack"));
        }
        defender.setAttackedBy(attacker);
    }
    
    public void rangedAttack(){
        if(attacker.id == defender.id || defender instanceof NPC) return;
        
        int distance = MathUtil.distance(attacker.x, attacker.y, defender.x, defender.y);
        
        int distancePenalty = (int) Math.round(((Math.pow(1.3, distance - (attacker.accuracy() / 2))) - (attacker.accuracy() / 2)) * 0.25);//TODO: TEMP MAYBE
        if(distancePenalty < 0) distancePenalty = 0;
        Log.debug("Dist Pen: " + distancePenalty);
        
        int attackerHitRoll = Dice.roll("1d" + attacker.accuracy() + attacker.accuracyBonus()) - distancePenalty;
        int defenderBlockRoll = Dice.roll("1d" + defender.defense() + defender.defenseBonus());
        
        if(attackerHitRoll > defenderBlockRoll || attackerHitRoll == defenderBlockRoll && Math.random() <= 0.5){
            Equippable weapon = attacker.getEquippedAt(EquipmentSlot.WEAPON);
            int damage = getDamage("ranged");
            commonAttack(weapon, damage, "killed by a " + attacker.name() + (weapon != null ? weapon.name() : ""), new ColoredString("fire a %s at " + (!defender.isPlayer() ? "the " : "") + "%s for %d damage"), weapon.name(), defender.name(), damage);
        }else{
            defender.doAction(new ColoredString("block the arrow"));
        }
        defender.setAttackedBy(attacker);
    }
    
    public void thrownAttack(Item item){
        commonAttack(null, 1, "thrown " + item.name(), new ColoredString("throw a %s at the %s for %d damage"), item.name(), defender.name(), 1);

        if(item instanceof Potion){
            Effect effect = ((Consumable) item).effect();
            if(effect != null && Math.random() <= effect.chance()){
//                new EffectOther(effect).onUseOther(defender);
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
   
    private static void doEffect(Equippable equippable, Creature attacker, Creature defender){
        if(equippable == null || equippable.effect() == null || !equippable.effect().canUseWithItem() || defender.hp() < 1) return;
        Effect effect = equippable.effect();
        if(Math.random() > effect.chance()) return;
        effect.onUseSelf(attacker);
        effect.onUseOther(defender);
    }
    
}

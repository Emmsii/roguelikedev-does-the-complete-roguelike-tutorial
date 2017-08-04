package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.util.CombatManager;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 04/08/2017 at 07:37 PM.
 */
public class NeutralAI extends CreatureAI {
    
    private Creature attackedBy;
    
    public NeutralAI(){}
    
    public NeutralAI(Creature creature){
        super(creature);
    }

    @Override
    public void update() {
        if(creature.hasFlag("slow") && Math.random() < 0.25) return;
        
        if(creature.attackedBy() != null && creature.aggressionCooldown() > 0){
            attackedBy = creature.attackedBy();
            if(canUseRanged(attackedBy)){
                new CombatManager(creature, attackedBy).rangedAttack();
            }else if(creature.hasFlag("smart") || creature.canSee(attackedBy)){
                pathTo(attackedBy.x, attackedBy.y);
            }
        }else if(canPickup()){
            creature.pickup();
        }else{
            wander(0.5f);
        }
    }
}

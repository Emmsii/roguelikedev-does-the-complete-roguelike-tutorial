package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.util.CombatManager;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 04/08/2017 at 07:37 PM.
 */
public class AggressiveAI extends CreatureAI {
    
    private Creature target;

    protected AggressiveAI(){}
    
    public AggressiveAI(Creature creature){
        super(creature);
    }

    @Override
    public void update() {
        if (creature.hasFlag("slow") && Math.random() < 0.25) return;
        target = creature.world().player();
        
        if(creature.attackedBy() != null) target = creature.attackedBy();
        
        if(equipBestWeapon()) return;
        else if(canUseRanged(target) && Math.random() <= 0.9 && MathUtil.distance(creature.x, creature.y, target.x, target.y) > creature.accuracy() + 3){
            new CombatManager(creature, target).rangedAttack();
        }else if(canSee(target.x, target.y, target.z)){
            pathTo(target.x, target.y);
        }else if(canPickup()){
            creature.pickup();
        }else{
            wander(0.5f);
        }
        
    }
}

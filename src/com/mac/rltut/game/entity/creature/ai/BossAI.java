package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.util.CombatManager;

import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 08:03 PM.
 */
public class BossAI extends CreatureAI{
    
    private PackAI pack;
    private Creature target;

    protected BossAI() {}
    
    public BossAI(Creature creature) {
        super(creature);
    }

    @Override
    public void update() {
        if(creature.hasFlag("slow") && Math.random() < 0.25) return;
        target = creature.world().player();
        
        if(canSee(target.x, target.y, target.z)) creature.setAttackedBy(target);
                
        if(creature.attackedBy() != null && creature.aggressionCooldown() > 0){
            if(canUseSpell(target) && Math.random() <= 0.6) {
                List<Spell> spells = creature.availableSpells();
                creature.castSpell(spells.get((int) (Math.random() * spells.size())), target.x, target.y);
            }else if(canUseRanged(target)){
                new CombatManager(creature, target).rangedAttack();
            } else pathTo(target.x, target.y);
        }else wander(0.5f);
        
    }

    public void setPack(PackAI pack){
        this.pack = pack;
    }
}

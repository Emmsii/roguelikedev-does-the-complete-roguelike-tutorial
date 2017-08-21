package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.EvilWizard;
import com.mac.rltut.game.entity.util.CombatManager;

import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 12:36 PM.
 */
public class EvilNpcAi extends NpcAI{

    protected EvilNpcAi(){}

    public EvilNpcAi(Creature creature){
        super(creature);
    }

    @Override
    public void update() {
        if(creature instanceof EvilWizard){
            EvilWizard wizard = (EvilWizard) creature;

            if(wizard.readyToFight() && wizard.canSee(wizard.world().player())){
                wizard.setCanAttack(true);
                Creature target = creature.world().player();
                if(canUseSpell(target) && Math.random() <= 0.6) {
                    List<Spell> spells = creature.availableSpells();
                    creature.castSpell(spells.get((int) (Math.random() * spells.size())), target.x, target.y);
                }else if(canUseRanged(target)){
                    new CombatManager(creature, target).rangedAttack();
                } else pathTo(target.x, target.y);
            }
        }
    }
}

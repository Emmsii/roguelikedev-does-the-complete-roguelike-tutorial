package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.util.CombatManager;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 07:53 PM.
 */
public class PackMemberAI extends CreatureAI{
    
    private PackAI pack;

    protected PackMemberAI() {}
    
    public PackMemberAI(Creature creature, PackAI pack) {
        super(creature);
        this.pack = pack;
    }
    
    public void update(){
        if(creature.hasFlag("slow") && Math.random() < 0.25) return;
        boolean canSee = pack.canSeeCreature(creature.world().player()); 
        if(canSee){
            pack.packTarget = creature.world().player();
            pack.packLastSeen = new Point(pack.packTarget.x, pack.packTarget.y, pack.packTarget.z);
        }
        
        if(equipBestWeapon()) return;
        
        if(creature.hasFlag("smart")){
            if(pack.packLastSeen != null) {
                if(canUseRanged(pack.packTarget)) new CombatManager(creature, pack.packTarget).rangedAttack();
                else {
                    int length = pathTo(pack.packLastSeen.x, pack.packLastSeen.y);
                    if (length == 1) {
                        pack.packTarget = null;
                        pack.packLastSeen = null;
                    }
                }
            }else packWander();
        }else{
            if(pack.packTarget != null){
                if(canSee){
                    if(canUseRanged(pack.packTarget)) new CombatManager(creature, pack.packTarget).rangedAttack();
                    else pathTo(pack.packTarget.x, pack.packTarget.y);
                }else{
                    pack.packTarget = null;
                    packWander();
                }
            }else packWander();
        }
        
    }
    
    private void packWander(){
        Point averagePosition = pack.getAveragePackPosition();

        int x = (int) Math.round(Math.random() * 2 - 1);
        int y = (int) Math.round(Math.random() * 2 - 1);
        if(MathUtil.distance(x + creature.x, y + creature.y, averagePosition.x, averagePosition.y) < pack.maxWanderDistance() / 2){
            creature.moveBy(x, y, 0);
        }else{
            Point target = creature.world().randomEmptyPointInRadius(averagePosition, pack.maxWanderDistance() / 2);
            if(target == null) return;
            pathTo(target.x, target.y);
        }
    }
    
    public PackAI pack(){
        return pack;
    }
}

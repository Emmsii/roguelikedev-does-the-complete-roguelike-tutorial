package com.mac.rltut.game.entity.creature.ai;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.pathfinding.Path;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 07:53 PM.
 */
public class PackMemberAI extends CreatureAI{
    
    private PackAI pack;
    
    public PackMemberAI(Creature creature, PackAI pack) {
        super(creature);
        this.pack = pack;
    }
    
    public void update(){
        if(pack.canSeePlayer()){
            pathTo(creature.world().player().x, creature.world().player().y);
        }else{
            packWander();
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

package com.mac.rltut.game.entity.creature.ai;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.pathfinding.Path;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;

import java.time.LocalDate;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 20/07/2017 at 08:29 PM.
 */
public class AggressiveAI extends CreatureAI{
    
    private Creature target;
    private Point lastSeen;
    
    public AggressiveAI(Creature creature) {
        super(creature);
    }

    @Override
    public void update() {
        if(creature.hasFlag("slow") && Math.random() < 0.25) return;
        
        boolean canSee = creature.canSee(creature.world().player());
        
        if(canSee){
            target = creature.world().player();
            lastSeen = new Point(target.x, target.y, target.z);
            Log.debug("CAN SEE PLAYER");
        }
        
        if(!creature.hasFlag("smart")){
            if(canSee && target != null) pathTo(target.x, target.y);
            else wander(0.5f);
        }else if(creature.hasFlag("smart")){
            if(lastSeen != null) {
                Log.debug("Pathing to last seen point");
                int length = pathTo(lastSeen.x, lastSeen.y);
                if (length == 1 && !canSee) {
                    Log.debug("Lost player");
                    target = null;
                    lastSeen = null;
                }
            }else{
                wander(0.5f);
            }
        }
        
       
        
       
    }
}

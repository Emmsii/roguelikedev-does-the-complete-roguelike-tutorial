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
    
    public AggressiveAI(Creature creature) {
        super(creature);
    }

    @Override
    public void update() {
        if(creature.canSee(creature.world().player())){
            //hunt
            Creature player = creature.world().player();
            Path path = new Path(creature, player.x, player.y);
            if(path.hasNext()){
                Point next = path.getNext();
                creature.moveBy(next.x - creature.x, next.y - creature.y, 0);
            }
            
        }else{
            wander(0.5f);
        }
    }
}

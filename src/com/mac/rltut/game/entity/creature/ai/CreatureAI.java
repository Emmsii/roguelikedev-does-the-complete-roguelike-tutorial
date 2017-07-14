package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 14/07/2017 at 11:11 AM.
 */
public class CreatureAI {
    
    protected Creature creature;
    
    public CreatureAI(Creature creature){
        this.creature = creature;
        creature.setAi(this);
    }
    
    public void update(){
        if(creature.name().equalsIgnoreCase("player")) return;
        if(Math.random() < 0.4){
            int x = (int) Math.round(Math.random() * 2 - 1);
            int y = (int) Math.round(Math.random() * 2 - 1);
            creature.moveBy(x, y, 0);
        }
    }
    
    public boolean tryMove(int xp, int yp, int zp){
        if(!canEnter(xp, yp, zp)) return false;
        
        creature.world().move(xp, yp, zp, creature);
        return true;
    }
    
    public boolean canEnter(int xp, int yp, int zp){
        for(int y = 0; y < creature.size(); y++){
            for(int x = 0; x < creature.size(); x++){
                if(creature.world().tile(x + xp, y + yp, zp).solid()) return false;
                Creature c = creature.world().creature(x + xp, y + yp, zp);
                if(c == null) continue;
                else if(c.id!= creature.id) return false;
            }
        }
        return true;
    }
}

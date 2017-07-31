package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.pathfinding.Path;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.maths.Line;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.stats.LevelUpController;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.Tile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 14/07/2017 at 11:11 AM.
 */
public class CreatureAI {
    
    protected Creature creature;
    
    public CreatureAI() {}
    
    public CreatureAI(Creature creature){
        this.creature = creature;
        if(creature != null) creature.setAi(this);
    }
    
    public void update(){
        
    }
    
    protected int pathTo(int xp, int yp){
        Path path = new Path(creature, xp, yp);
        if(path.hasNext()){
            Point next = path.getNext();
            creature.moveBy(next.x - creature.x, next.y - creature.y, 0);
        }
        return path.length();
    }
    
    protected void wander(float frequency){
        if(Math.random() < frequency){
            int x = (int) Math.round(Math.random() * 2 - 1);
            int y = (int) Math.round(Math.random() * 2 - 1);
            creature.moveBy(x, y, 0);
        }
    }
    
    public void onGainLevel(){
        new LevelUpController().autoLevelUp(creature);
    }
    
    public boolean onMove(int xp, int yp, int zp){
        if(!canEnter(xp, yp, zp)) return false;
        creature.world().move(xp, yp, zp, creature);
        return true;
    }
    
    public boolean canEnter(int xp, int yp, int zp){
        for(int y = 0; y < creature.size(); y++){
            for(int x = 0; x < creature.size(); x++){
                Tile t = creature.world().tile(x + xp, y + yp, zp); //TODO: SOLID!
                if(t.canFly() && creature.hasFlag("can_fly")) continue;
                MapObject obj = creature.world().mapObject(x + xp, y + yp, zp);
                if(t.solid() || obj != null) return false;
                Creature c = creature.world().creature(x + xp, y + yp, zp);
                if(c == null) continue;
                else if(c.id!= creature.id) return false;
            }
        }
        return true;
    }
    
    public boolean canSee(int xp, int yp, int zp){
        if(creature.z != zp) return false;
        if((creature.x - xp) * (creature.x - xp) + (creature.y - yp) * (creature.y - yp) > creature.vision() * creature.vision()) return false;
        for(Point p : new Line(creature.x, creature.y, xp, yp)){
            if(creature.world().tile(p.x, p.y, zp).canSee() || p.x == xp && p.y == yp) continue;
            return false;
        }
        
        return true;
    }
    
    public void notify(ColoredString message){
        
    }
}

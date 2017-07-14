package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.world.World;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 28/06/2017 at 03:13 PM.
 */
public class Creature extends Entity {
    
    private int size;

    private CreatureAI ai;
    
    public Creature(String name, Sprite sprite) {
        this(name, sprite, 1);
    }
        
    public Creature(String name, Sprite sprite, int size) {
        super(name, sprite);
        this.size = size;
    }

    public void setStats(){
        
    }

    @Override
    public void init(int id, World world) {
        super.init(id, world);
    }

    @Override
    public void update() {
        ai.update();
    }
    
    /* Movement Methods */
    
    public boolean moveBy(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(x + xp, y + yp, z + zp)) return false;
        
        return ai.tryMove(x + xp, y + yp, z + zp);
    }
    
    public int size(){
        return size;
    }
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
}

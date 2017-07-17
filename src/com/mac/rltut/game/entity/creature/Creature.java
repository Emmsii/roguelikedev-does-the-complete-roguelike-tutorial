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

    private int maxHp;
    private int hp;
    private int vision;
    
    public Creature(String name, Sprite sprite) {
        this(name, sprite, 1);
    }
        
    public Creature(String name, Sprite sprite, int size) {
        super(name, sprite);
        this.size = size;
    }

    public void setStats(int maxHp, int vision){
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.vision = vision;
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
    
    //Move creature by amount
    public boolean moveBy(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(x + xp, y + yp, z + zp)) return false;
        
        Creature other = world.creature(x + xp, y + yp, z + zp);
        if(other == null){
            return ai.tryMove(x + xp, y + yp, z + zp);
        }else{
            //Attack Melee
            return true;
        }
    }
    
    //Move creature to position
    public boolean moveTo(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(xp, yp, zp));
        
        Creature other = world.creature(xp, yp, zp);
        if(other == null) return ai.tryMove(xp, yp, zp);
        
        return false;
    }
    
    /* Combat Methods */
    
    /* Item Methods */
    
    /* Log Methods */
    
    /* Util Methods */
    
    /* Modifier Methods */
    
    public void modifyHp(int amount){
        hp += amount;
        
        if(hp > maxHp) hp = maxHp;
        else if(hp < 1){
            //Die
            world.remove(this);
        }
    }
    
    /* Getter Methods */
    
    public int size(){
        return size;
    }
    
    public int maxHp(){
        return maxHp;
    }
    
    public int hp(){
        return hp;
    }
    
    public int vision(){
        return Math.min(vision, world.dayNightController().light());
    }
    
    /* Setter Methods */
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
}

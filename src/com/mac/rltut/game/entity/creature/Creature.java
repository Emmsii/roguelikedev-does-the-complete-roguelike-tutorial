package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.world.World;
import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    private int maxMana;
    private int mana;
    
    private int strength;
    private int defense;
    private int accuracy;
    private int intelligence;
    private int vision;
    
    private String causeOfDeath;
    
    public Creature(String name, Sprite sprite) {
        this(name, sprite, 1);
    }
        
    public Creature(String name, Sprite sprite, int size) {
        super(name, sprite);
        this.size = size;
    }

    public void setStats(int maxHp, int maxMana, int strength, int defense, int accuracy, int intelligence, int vision){
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = mana;
        this.strength = strength;
        this.defense = defense;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
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
            new CombatManager(this, other).meleeAttack();
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
    
    public void notify(String message, Object ... params){
        ai.notify(String.format(message, params));
    }

    public void doAction(String message, Object ... params){
        for(Creature other : getCreaturesWhoSeeMe()){
            if(other == this) other.notify("You " + message + ".", params);
            else other.notify("The %s %s.", StringUtil.makeSecondPerson(message), params);
        }
    }
    
    /* Util Methods */
    
    public List<Creature> getCreaturesWhoSeeMe(){
        List<Creature> others = new ArrayList<Creature>();
        
        for(Creature c : world.creatures(z)){
            if(c.id == this.id) continue;
            if(c.canSee(this)) others.add(c);
        }
        
        return others;
    }
    
    public boolean canSee(Creature c){
        return canSee(c.x, c.y, c.x);
    }
    
    public boolean canSee(int xp, int yp, int zp){
        return ai.canSee(xp, yp, zp);
    }
    
    /* Modifier Methods */
    
    public void modifyHp(int amount, String causeOfDeath){
        this.causeOfDeath = causeOfDeath;
        hp += amount;
                
        if(hp > maxHp) hp = maxHp;
        else if(hp < 1){
            doAction("die");
            world.remove(this);
        }
    }
    
    public void modifyMana(int amount){
        mana += amount;
        
        if(mana > maxMana) mana = maxMana;
        else if(mana < 0) mana = 0; 
    }
    
    public void modifyMaxHp(int amount){
        maxHp += amount;
    }
    
    public void modifyMaxMana(int amount){
        maxMana += amount;
    }
    
    public void modifyStrength(int amount){
        strength += amount;
    }
    
    public void modifyDefense(int amount){
        defense += amount;
    }
    
    public void modifyAccuracy(int amount){
        accuracy += amount;
    }
    
    public void modifyIntelligence(int amount){
        intelligence += amount;
    }
    
    public void modifyVision(int amount){
        vision += amount;
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
    
    public int maxMana(){
        return maxMana;
    }
    
    public int mana(){
        return mana;
    }
    
    public int strength(){
        return strength;
    }
    
    public int defense(){
        return defense;
    }
    
    public int accuracy(){
        return accuracy;
    }
    
    public int intelligence(){
        return intelligence;
    }
    
    public int vision(){
        return Math.min(vision, world.dayNightController().light());
    }
    
    /* Setter Methods */
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
}

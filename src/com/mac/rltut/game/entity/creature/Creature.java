package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.world.World;

import java.awt.*;
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
    private String aiType;

    private int maxHp;
    private int hp;
    private int maxMana;
    private int mana;
    
    private int strength;
    private int defense;
    private int accuracy;
    private int intelligence;
    private int vision;
    
    private int xp;
    private int level;
    
    private String causeOfDeath;
    
    private int timeStationary;
    private boolean hasMoved;
    
    public Creature(String name, Sprite sprite, String aiType) {
        this(name, sprite, 1, aiType);
    }
        
    public Creature(String name, Sprite sprite, int size, String aiType) {
        super(name, sprite);
        this.size = size;
        this.level = 1;
        this.aiType = aiType;
    }

    public void setStats(int maxHp, int maxMana, int strength, int defense, int accuracy, int intelligence, int vision){
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
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
        
        if(!hasMoved) timeStationary++;
        else timeStationary = 0;
    }
    
    /* Movement Methods */
    
    //Move creature by amount
    public boolean moveBy(int xp, int yp, int zp){
        hasMoved = false;
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(x + xp, y + yp, z + zp)) return false;
        
        Creature other = world.creature(x + xp, y + yp, z + zp);
        if(other == null){
            hasMoved = ai.onMove(x + xp, y + yp, z + zp);
            return hasMoved;
        }else{
            //Attack Melee
            if(!isPlayer() && other.isPlayer() || isPlayer()) new CombatManager(this, other).meleeAttack();
            hasMoved = true;
            return true;
        }
    }
    
    //Move creature to position
    public boolean moveTo(int xp, int yp, int zp){
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(xp, yp, zp));
        
        Creature other = world.creature(xp, yp, zp);
        if(other == null) return ai.onMove(xp, yp, zp);
        
        return false;
    }
    
    /* Combat Methods */

    public void damage(int amount, String causeOfDeath){
        modifyHp(-amount, causeOfDeath);
    }
    
    public void gainXp(Creature other){
        if(isPlayer()){
            Player player = (Player) this;
            player.stats().addKill(other.name);
        }
        
        int amount = other.maxHp + other.strength + other.defense - level;
        if(amount > 0) modifyXp(amount);
    }
    
    /* Item Methods */
    
    /* Log Methods */
    
    public void notify(ColoredString message, Object ... params){
        message.text = String.format(message.text, params);
        ai.notify(message);
    }

    public void doAction(ColoredString message, Object ... params){
        for(Creature other : getCreaturesWhoSeeMe()){
            if(other == this) other.notify(new ColoredString("You " + message.text + ".", message.color), params);
            else other.notify(new ColoredString(String.format("The %s %s.", name, StringUtil.makeSecondPerson(message.text)), message.color), params);
        }
    }
    
    /* Util Methods */
    
    public List<Creature> getCreaturesWhoSeeMe(){
        List<Creature> others = new ArrayList<Creature>();
        for(Creature c : world.creatures(z)) if(c.canSee(this)) others.add(c);
        return others;
    }
    
    public boolean canSee(Creature c){
        return canSee(c.x, c.y, c.z);
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
            doAction(new ColoredString("die", Color.RED.getRGB()));
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
    
    public void modifyXp(int amount){
        xp += amount;
        
        notify(new ColoredString("You %s %d xp.", amount < 0 ? Color.red.getRGB() : Color.green.getRGB()), amount < 0 ? "lose" : "gain", amount);
        
        while(xp > (int) (Math.pow(level, 1.75) * 25)){
            level++;
            doAction(new ColoredString("advance to level %d", Color.GREEN.getRGB()), level);
            ai.onGainLevel();
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
    
    public int xp(){
        return xp;
    }
    
    public int level(){
        return level;
    }
    
    public String aiType(){
        return aiType;
    }
    
    public CreatureAI ai(){
        return ai;
    }
    
    public int timeStationary(){
        return timeStationary;
    }
    
    //TODO: CHANGE THIS!
    public boolean isPlayer(){
        return this instanceof Player;
    }
    
    /* Setter Methods */
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
}

package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.*;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.entity.creature.util.CombatManager;
import com.mac.rltut.game.entity.item.DropTable;
import com.mac.rltut.game.entity.item.Inventory;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.entity.item.equipment.Weapon;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.MapObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 28/06/2017 at 03:13 PM.
 */
public class Creature extends Entity {
    
    private int size;

    private CreatureAI ai;
    private String aiType;

    private Inventory<Item> inventory;
    
    private Weapon weapon;
        
    private Set<String> flags;
    private DropTable dropTable;
    
    private int maxHp;
    private int hp;
    private int maxMana;
    private int mana;
    private int manaRegenAmount;
    private int manaRegenSpeed;
    
    private int strength;
    private int defense;
    private int accuracy;
    private int intelligence;
    private int vision;
    
    private int xp;
    private int level;
    private int gold;
    
    private String causeOfDeath;
    
    private int timeStationary;
    private boolean hasMoved;
    private MapObject mapObject;
    
    private int tick;
    
    public Creature(String name, String description, Sprite sprite, String aiType) {
        this(name, description, sprite, 1, aiType);
    }
        
    public Creature(String name, String description, Sprite sprite, int size, String aiType) {
        super(name, description, sprite);
        this.size = size;
        this.inventory = new Inventory<Item>();
        this.flags = new HashSet<String>();
        this.level = 1;
        this.aiType = aiType;
    }

    public void setStats(int maxHp, int maxMana, int manaRegenAmount, int manaRegenSpeed, int strength, int defense, int accuracy, int intelligence, int vision, DropTable dropTable){
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.manaRegenAmount = manaRegenAmount;
        this.manaRegenSpeed = manaRegenSpeed;
        this.strength = strength;
        this.defense = defense;
        this.accuracy = accuracy;
        this.intelligence = intelligence;
        this.vision = vision;
        this.dropTable = dropTable;
    }

    @Override
    public void init(int id, World world) {
        super.init(id, world);
    }

    @Override
    public void update() {
        tick++;
        ai.update();
        
        if(tick % manaRegenSpeed == 0) modifyMana(manaRegenAmount);
        
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

        MapObject obj = world.mapObject(xp, yp, zp);
        if(obj != null){
            return false;
        }
        
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
    
    public void pickup(){
        Item item = world.item(x, y, z);
        
        if(item == null){
            doAction(new ColoredString("grab at nothing"));
            return;
        }
        
        if(inventory.isFull()) notify(new ColoredString("You are carrying too much.", Color.ORANGE.getRGB()));
        else{
            String str = "pickup a %s";
            if(item instanceof ItemStack) str += " x" + ((ItemStack) item).amount() + "";
            doAction(new ColoredString(str), item.name());
            world.remove(item);
            
            if(item instanceof ItemStack && item.name().equalsIgnoreCase("gold pile")) gold += ((ItemStack) item).amount();
            else inventory.add(item);
        }
    }
    
    public void drop(Item item){
        if(world.addAtEmptyPoint(x, y, z, item)){
            doAction(new ColoredString("drop a %s"), item.name());
            inventory.remove(item);
        }else{
            notify(new ColoredString("There is nowhere to drop the %s.", Color.ORANGE.getRGB()), item.name());
        }
    }
    
    public void dropFromDropTable(){
        if(dropTable == null) return;
        Set<String> dropped = new HashSet<String>();
        for(int i = 0; i < dropTable.count(); i++) {
            Item item = dropTable.getItem();
            if (item != null && !dropped.contains(item.name())) {
                drop(item);
                dropped.add(item.name());
            }
        }
    }
    
    public void equip(Item item){
        if(item instanceof Weapon){
            weapon = (Weapon) item;
            doAction(new ColoredString("equip a %s"), item.name());
        }
    }
    
    public void unequip(Item item){
        if(item instanceof Weapon){
            weapon = null;
            doAction(new ColoredString("unequip a %s"), item.name());
        }
    }
    
    public Chest tryOpen(){
        for(Point p : new Point(x, y, z).neighboursAll()) {
            if (world.mapObject(p.x, p.y, p.z) instanceof Chest) {
                return (Chest) world.mapObject(p.x, p.y, p.z);
            }
        }
        return null;
    }
    
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
            world.addCorpse(this);
            dropFromDropTable();
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
    
    public void modifyGold(int amount){
        gold += amount;
        if(amount < 0) gold = 0;
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
    
    public int manaRegenAmount(){
        return manaRegenAmount;
    }
    
    public int getManaRegenSpeed(){
        return manaRegenSpeed;
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

    public int strengthBonus(){
        return 7;
    }
    
    public int defenseBonus(){
        return 2;
    }
    
    public int accuracyBonus(){
        return -3;
    }
    
    public int intelligenceBonus(){
        return 1;
    }
    
    public int manaRegenAmountBonus(){
        return 2;
    }

    public int manaRegenSpeedBonus(){
        return 0;
    }
    
    public int xp(){
        return xp;
    }
    
    public int level(){
        return level;
    }
    
    public int gold(){
        return gold;
    }
    
    public Inventory<Item> inventory(){
        return inventory;
    }
    
    public Weapon weapon(){
        return weapon;
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
    
    public MapObject mapObject(){
        return mapObject;
    }
    
    public boolean hasFlag(String flag){
        return flags.contains(flag.toLowerCase().trim());
    }
    
    public boolean isPlayer(){
        return this instanceof Player;
    }
    
    /* Setter Methods */
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
    
    public void addFlag(String flag){
        flags.add(flag.toLowerCase().trim());
    }
    
    public void setMapObject(MapObject mapObject){
        this.mapObject = mapObject;
    }
}

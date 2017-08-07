package com.mac.rltut.game.entity.creature;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.entity.item.util.DropTable;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.entity.util.CombatManager;
import com.mac.rltut.game.world.World;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.MapObject;

import java.util.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 28/06/2017 at 03:13 PM.
 */
public class Creature extends Entity {
    
    private int size;

    private CreatureAI ai;
    private String aiType;

    private DropTable dropTable;
    private Inventory<Item> inventory;
    private HashMap<EquipmentSlot, Equippable> equippedItems;
         
    private List<Effect> effects;
    private Set<String> flags;
    private Set<String> immuneTo;
    
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
    private int visionBonus;
    
    private int xp;
    private int level;
    private int gold;
    
    private String causeOfDeath;
    
    private int timeStationary;
    private boolean hasMoved;
    private boolean hasUsedEquipment;
    
    private int tick;
    
    private Creature attackedBy;
    private int aggressionCooldown;

    protected Creature() {}
    
    public Creature(String name, String description, Sprite sprite, String aiType) {
        this(name, description, sprite, 1, aiType);
    }
        
    public Creature(String name, String description, Sprite sprite, int size, String aiType) {
        super(name, description, sprite);
        this.size = size;
        this.flags = new HashSet<String>();
        this.immuneTo = new HashSet<String>();
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
        this.equippedItems = new HashMap<EquipmentSlot, Equippable>();
        this.effects = new ArrayList<Effect>();
        this.inventory = new Inventory<Item>();
        
        if(hasFlag("prefer_night") && world.dayNightController().isDay()) addFlag("invisible"); 
        if(hasFlag("prefer_day") && world.dayNightController().isNight()) addFlag("invisible"); 
    }

    @Override
    public void update() {
        tick++;
        
        regenMana();
        updateFlags();
        updateEffects();
        if(!hasFlag("invisible")) ai.update();
        
        if(aggressionCooldown > 0) aggressionCooldown--; 
        else if(aggressionCooldown == 0) attackedBy = null;
        
        if(!hasMoved) timeStationary++;
        else timeStationary = 0;
    }
    
    private void regenMana(){
        if(tick % manaRegenSpeed == 0) modifyMana(manaRegenAmount());
    }
    
    private void updateFlags(){
        if(hasFlag("prefer_night")){
            if(world().dayNightController().isNight() && hasFlag("invisible") && !canSee(world.player())) removeFlag("invisible");
            if(world().dayNightController().isDay() && !hasFlag("invisible") && !canSee(world.player())) addFlag("invisible");
        }
        if(hasFlag("prefer_day")){
            if(world().dayNightController().isNight() && !hasFlag("invisible") && !canSee(world.player())) addFlag("invisible");
            if(world().dayNightController().isDay() && hasFlag("invisible") && !canSee(world.player())) removeFlag("invisible");
        }
    }
    
    private void updateEffects(){
        List<Effect> done = new ArrayList<Effect>();
        
        for(Effect e : effects){
            e.update(this);
            if(e.isDone()){
                e.stop(this);
                done.add(e);
            }
        }
        
        effects.removeAll(done);
    }
    
    /* Movement Methods */
    
    //Move creature by amount
    public boolean moveBy(int xp, int yp, int zp){
        hasMoved = false;
        if(xp == 0 && yp == 0 && zp == 0) return false;
        if(!world.inBounds(x + xp, y + yp, z + zp)) return false;
        
        
        List<Creature> others = new ArrayList<Creature>();
        
        for(int ya = 0; ya < size(); ya++){
            int yb = y + yp + ya;
            for(int xa = 0; xa < size; xa++){
                int xb = x + xp + xa;
                Creature other =  world.creature(xb, yb, z + zp);
                if(other != null && other.id != id) others.add(other);
            }
        }
        
        if(others.isEmpty()) hasMoved = ai.onMove(x + xp, y + yp, z + zp);
        
        for(Creature other : others) {
            if(!isPlayer() && other.isPlayer() || isPlayer()) new CombatManager(this, other).meleeAttack();
            hasMoved = true;
        }
        
        return hasMoved;
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
    
    public void addItem(Item item){
        
    }
    
    public void pickup(){
        Item item = world.item(x, y, z);
        
        if(item == null){
            doAction(new ColoredString("grab at nothing"));
            return;
        }
        
        if(inventory.isFull()) notify(new ColoredString("You are carrying too much.", Colors.ORANGE));
        else{
            String str = "pickup a %s";
            if(item instanceof ItemStack) str += " x" + ((ItemStack) item).amount() + "";
            doAction(new ColoredString(str), item.name());
            world.remove(item);
            
            if(item instanceof ItemStack && item.name().equalsIgnoreCase("gold")) gold += ((ItemStack) item).amount();
            else inventory.add(item);
            
            if(item instanceof Equippable){
                Equippable e = (Equippable) item;
                if(getEquippedAt(e.slot()) == null) e.equip(this);
            }
        }
    }
    
    public void drop(Item item){
        Point itemSpawn = world.getEmptyItemDropPoint(x, y, z);
                
        if(itemSpawn != null) {
            if(item instanceof Equippable) ((Equippable) item).unequip(this);
            doAction(new ColoredString("drop a %s"), item.name());
            inventory().remove(item);
            world.add(itemSpawn.x, itemSpawn.y, itemSpawn.z, item);
            if(itemSpawn.x == x && itemSpawn.y == y && itemSpawn.z == z) notify(new ColoredString("A %s lands at your feet."), item.name());
        }else{
            notify(new ColoredString("There is nowhere to drop the %s.", Colors.ORANGE), item.name());
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
        if(item instanceof Equippable) ((Equippable) item).equip(this);
        else notify(new ColoredString("You cannot equip a %s."), item.name());
    }
    
    public void unequip(Item item){
        if(item instanceof Equippable) ((Equippable) item).unequip(this);
        else notify(new ColoredString("You cannot unequip a %s."), item.name());
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
        if(ai != null) ai.notify(message);
    }

    //TODO: Merge doAction and announce methods
    
    public void doAction(ColoredString message, Object ... params){
        for(Creature other : getCreaturesWhoSeeMe()){
            if(other == this) other.notify(new ColoredString("You " + message.text + ".", message.color), params);
            else other.notify(new ColoredString(String.format("The %s %s.", name, StringUtil.makeSecondPerson(message.text)), message.color), params);
        }
    }
    
    public void announce(ColoredString message, Object ... params){
        for(Creature other : getCreaturesWhoSeeMe()){
            if(other == this) other.notify(new ColoredString(message.text, message.color), params);
            else other.notify(new ColoredString(String.format("The %s %s", name, message.text), message.color), params);
        }
    }
    
    public void say(ColoredString message){
//        for(Creature other : getCreaturesWhoSeeMe()) other.notify(new ColoredString(StringUtil.capitalizeEachWord(name) + "" + message.text, message.color));
    }

    public void shout(ColoredString message){
        String[] verbs = { "shouts", "bellows", "yells" };
        String text = "\"" + message.text + "\"" + (isPlayer() ? " you" : " the " + name) + " " + verbs[(int) (Math.random() * verbs.length)];
        if(isPlayer()) text = text.substring(0, text.length() - 1);
        for(Creature other : getCreaturesWhoSeeMe()) other.notify(new ColoredString(text + ".", message.color));
    }
    
    /* Util Methods */
    
    public List<Creature> getCreaturesWhoSeeMe(){
        List<Creature> others = new ArrayList<Creature>();
        if(hasFlag("invisible")) return others;
        for(Creature c : world.creatures(z)) if(c.canSee(this)) others.add(c);
        return others;
    }
    
    public boolean canSee(Creature c){
        return canSee(c.x, c.y, c.z);
    }
    
    public boolean canSee(int xp, int yp, int zp) {
        return ai != null && ai.canSee(xp, yp, zp);
    }
    
    /* Modifier Methods */
    
    public void modifyHp(int amount, String causeOfDeath){
        if(hp < 1) return;
        this.causeOfDeath = causeOfDeath;
        hp += amount;
                
        if(hp > maxHp) hp = maxHp;
        else if(hp < 1){
            doAction(new ColoredString("die", Colors.RED));
            world.remove(this);
            world.addCorpse(this);
            dropFromDropTable();
            for(int i = inventory().count() - 1; i >= 0; i--) drop(inventory.get(i));
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
    
    public void modifyVisionBonus(int amount){
        visionBonus += amount;
    }
    
    public void modifyXp(int amount){
        xp += amount;
        
        notify(new ColoredString("You %s %d xp.", amount < 0 ? Colors.RED : Colors.GREEN), amount < 0 ? "lose" : "gain", amount);
        
        while(xp > (int) (Math.pow(level, 1.75) * 25)){
            level++;
            doAction(new ColoredString("advance to level %d", Colors.GREEN), level);
            ai.onGainLevel();
        }
    }
    
    public void modifyGold(int amount){
        gold += amount;
        if(amount < 0) gold = 0;
    }
    
    public void addEffect(Effect effect){
        if(effect == null) return;
        effect.start(this);
        effects.add(effect);
    }

    /* Getter Methods */
    
    public int totalLevel(){
        int total = strength + defense + accuracy + intelligence;
        return total; 
    }
    
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
        return manaRegenAmount + (intelligence / 2) + intelligenceBonus();
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
        if(visionBonus > 0) return visionBonus;
        return Math.min(vision, world.dayNightController().light());
    }

    public int strengthBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).strengthBonus();
        } 
        return result;
    }
    
    public int defenseBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).defenseBonus();
        }
        return result;
    }
    
    public int accuracyBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).accuracyBonus();
        }
        return result;
    }
    
    public int intelligenceBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).intelligenceBonus();
        }
        return result;
    }
    
    public int manaRegenAmountBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).manaRegenAmountBonus();
        }
        return result;
    }

    public int manaRegenSpeedBonus(){
        int result = 0;
        for(EquipmentSlot s : equippedItems.keySet()){
            if(equippedItems.get(s) == null) continue;
            result += equippedItems.get(s).manaRegenSpeedBonus();
        }
        return result;
    }
    
    public Creature attackedBy(){
        return attackedBy;
    }
    
    public int aggressionCooldown(){
        return aggressionCooldown;
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
    
    public HashMap<EquipmentSlot, Equippable> equippedItems(){
        return equippedItems;
    }
    
    public Equippable getEquippedAt(EquipmentSlot slot){
        return equippedItems.get(slot);
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

    public boolean hasUsedEquipment(){
        return hasUsedEquipment;
    }
    
    public boolean hasFlag(String flag){
        return flags.contains(flag.toLowerCase().trim());
    }
    
    public boolean immuneTo(String effectName){
        return immuneTo.contains(effectName.toLowerCase().trim());
    }
    
    public boolean isPlayer(){
        return this instanceof Player;
    }
        
    /* Setter Methods */
    
    public void setAi(CreatureAI ai){
        this.ai = ai;
    }
    
    public void addImmunity(String effectName){
        immuneTo.add(effectName);
    }
    
    public void removeImmunity(String effectName){
        immuneTo.remove(effectName);
    }
    
    public void addFlag(String flag){
        flags.add(flag.toLowerCase().trim());
    }
    
    public void removeFlag(String flag){
        flags.remove(flag.toLowerCase().trim());
    }
        
    public void setHasUsedEquipment(boolean hasUsedEquipment){
        this.hasUsedEquipment = hasUsedEquipment;
    }
    
    public void setEquippable(EquipmentSlot slot, Equippable equippable){
        equippedItems.put(slot, equippable);
    }
    
    public void setAttackedBy(Creature attackedBy){
        this.attackedBy = attackedBy;
        if(aggressionCooldown == 0 && hp > 0 && !isPlayer() && aiType.equalsIgnoreCase("neutral")) doAction(new ColoredString("get angry", Colors.RED));
        aggressionCooldown = 10;
    }
    
    @Override
    public Entity newInstance() {
        Creature c = (Creature) super.newInstance();
        return c;
    }
}

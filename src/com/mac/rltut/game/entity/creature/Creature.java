package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.engine.util.maths.Line;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.Entity;
import com.mac.rltut.game.entity.creature.ai.CreatureAI;
import com.mac.rltut.game.entity.item.*;
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
    private Ammo ammo;
         
    private List<Spell> knownSpells;
    private List<Effect> effects;
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
    private int diamonds;
    
    private String causeOfDeath;
    private boolean hasWon;
    
    private int timeStationary;
    private boolean hasMoved;
    private boolean hasPerformedAction;
    
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
        this.immuneTo = new HashSet<String>();
        this.knownSpells = new ArrayList<Spell>();
        this.level = 1;
        this.aiType = aiType;
        this.hasMoved = false;
        this.hasPerformedAction = false;
        this.hasWon = false;
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
        int speed = manaRegenSpeed - manaRegenSpeedBonus();
        if(speed == 0) return;
        if(tick % speed == 0) modifyMana(manaRegenAmount() + manaRegenAmountBonus());
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
                if(other != null && other.id != id && !others.contains(other)) others.add(other);
            }
        }
        
        if(others.isEmpty()) hasMoved = ai.onMove(x + xp, y + yp, z + zp);
        
        for(Creature other : others) {
            if(!isPlayer() && other.isPlayer() || isPlayer()) {
                new CombatManager(this, other).meleeAttack();
                hasMoved = true;
            }
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
    
    public void castSpell(Spell spell, int xp, int yp){
        if(spell == null) return;
        
        if(mana < spell.manaCost()){
            notify(new ColoredString("You don't have enough mana to cast %s [%d]", Colors.RED), StringUtil.capitalizeEachWord(spell.name()), spell.manaCost());
            return;
        }
        
        Creature other = world.creature(xp, yp, z);
        if(spell.effectOther() != null){
            if(other != null){
                doAction(new ColoredString("cast %s"), StringUtil.capitalizeEachWord(spell.name()));
                other.addEffect(spell.effectOther());
            }else doAction(new ColoredString("miss"));
        }
        if(spell.effectSelf() != null) addEffect(spell.effectSelf());
        
        modifyMana(-spell.manaCost());
    }
    
    public void gainXp(Creature other){
        addKill(other.name);
        int amount = other.maxHp + other.strength + other.defense - level;
        if(other instanceof Boss) amount *= 5;
        if(amount > 0) modifyXp(amount);
    }
    
    public void throwItem(Item item, int xp, int yp, int zp){
        Point end = new Point(x, y, 0);
        
        for(Point p : new Line(x, y, xp, yp)){
            if(world.solid(p.x, p.y, z)) break;
            end = p;
        }
        
        xp = end.x;
        yp = end.y;
        
        Creature other = world.creature(xp, yp, zp);
        if(other != null) new CombatManager(this, other).thrownAttack(item);
        else doAction(new ColoredString("throw a %s"), item.name());
        
        if(item instanceof Equippable) unequip(item);
        inventory().remove(item);
        if(item instanceof Potion) return;
        Point spawn = world.getEmptyItemDropPoint(xp, yp, zp);
        world.add(spawn.x, spawn.y, spawn.z, item);
    }
    
    /* Item Methods */

    public void pickup(){
        Item item = world.pickMushroom(x, y, z);
        if(world.item(x, y, z) != null) item = world.item(x, y, z);
        
        if(item == null){
            doAction(new ColoredString("grab at nothing"));
            return;
        }
        
        if(inventory.isFull()) notify(new ColoredString("You are carrying too much.", Colors.ORANGE));
        else{
            String str = "pickup a %s";

            if(item instanceof ItemStack) str = "pickup " + ((ItemStack) item).amount() + " %s";
            doAction(new ColoredString(str), item.name());
            world.remove(item);

            if(item instanceof ItemStack){
                String name = item.name();
                if(name.equalsIgnoreCase("gold")) gold += ((ItemStack) item).amount();
                else if(name.equalsIgnoreCase("diamond")) diamonds += ((ItemStack) item).amount();
            } else inventory.add(item);

            if (item instanceof Equippable) {
                Equippable e = (Equippable) item;
                if (getEquippedAt(e.slot()) == null && Config.autoEquip) e.equip(this);
            }
        }
    }
    
    public void drop(Item item){
        Point itemSpawn = world.getEmptyItemDropPoint(x, y, z);
                
        if(itemSpawn != null) {
            if(item instanceof Equippable) ((Equippable) item).unequip(this);
            if(item instanceof ItemStack) doAction(new ColoredString("drop %s"), item.name());
            else doAction(new ColoredString("drop a %s"), item.name());
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
            if(other == this) other.notify(new ColoredString("You " + message.text + ".", message.color), params);
            else other.notify(new ColoredString(String.format("The %s %s.", name, message.text), message.color), params);
        }
    }
    
    public void say(ColoredString message){
        for(Creature other : getCreaturesWhoSeeMe()){
            other.notify(new ColoredString(StringUtil.capitalizeEachWord(name) + ": " + message.text, message.color));
        }
    }

    public void shout(ColoredString message){
        String[] verbs = { "shouts", "bellows", "yells" };
        String text = "\'" + message.text + "\'" + (isPlayer() ? " you" : " the " + name) + " " + verbs[(int) (Math.random() * verbs.length)];
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
        if(ai == null) return false;
        return ai.canSee(xp, yp, zp);
    }
    
    public void addKill(String name){
        
    }
    
    public void incrementStat(String key, int amount){
        
    }
    
    /* Modifier Methods */
    
    public void modifyHp(int amount, String causeOfDeath){
        if(hp < 1) return;
        this.causeOfDeath = "killed by " + causeOfDeath;
        hp += amount;
                
        if(hp > maxHp) hp = maxHp;
        else if(hp < 1){
            doAction(new ColoredString("die", Colors.RED));
            world.remove(this);
            if(!hasFlag("no_corpse")) world.addCorpse(this);
            dropFromDropTable();
            for(int i = inventory().count() - 1; i >= 0; i--) drop(inventory.get(i));
            if(this instanceof EvilWizard) world.player().setHasWon(true);
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
    
    public void setVisionBonus(int amount){
        visionBonus = amount;
    }
    
    public void modifyXp(int amount){
        xp += amount;
        
        notify(new ColoredString("You %s %d xp.", amount < 0 ? Colors.RED : Colors.GREEN), amount < 0 ? "lose" : "gain", amount);
        
        while(xp > (int) (Math.pow(level, 1.75) * 35)){
            level++;
            modifyMaxHp(5);
            modifyHp((int) (maxHp * 0.2), "too much health");
            doAction(new ColoredString("advance to level %d", Colors.GREEN), level);
            if(ai != null) ai.onGainLevel();
        }
    }
    
    public void modifyGold(int amount){
        gold += amount;
        if(gold < 0) gold = 0;
    }
    
    public void modifyDiamonds(int amount){
        diamonds += amount;
        if(diamonds < 0) diamonds = 0;
    }
    
    public void addEffect(Effect effect){
        if(effect == null) return;
        if(immuneTo(effect.name().toLowerCase())){
            announce(new ColoredString("is immune to %s"), effect.adjective());
            return;
        }
        
        //Cannot have duplicate effects.
        if(isEffectedBy(effect)) return;
        effect.start(this);
        effects.add(effect);
    }

    /* Getter Methods */
    
    public int combatLevel(){
        int total = strength + defense + accuracy;
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
        return Math.min(vision + visionBonus + world.level(z).visibilityModifier(), world.dayNightController().light() + visionBonus + world.level(z).visibilityModifier());
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
    
    public int diamonds(){
        return diamonds;
    }
    
    public Inventory<Item> inventory(){
        return inventory;
    }
    
    public List<Effect> effects(){
        return effects;
    }
    
    public Ammo ammo(){
        return ammo;
    }
    
    public HashMap<EquipmentSlot, Equippable> equippedItems(){
        return equippedItems;
    }
    
    public Equippable getEquippedAt(EquipmentSlot slot){
        return equippedItems.get(slot);
    }
    
    public List<Spell> knownSpells(){
        return knownSpells;
    }
    
    public List<Spell> availableSpells(){
        List<Spell> available = new ArrayList<Spell>();
        for(Spell s : knownSpells) if(mana >= s.manaCost()) available.add(s);
        return available;
    }
    
    public Spell getRandomSpell(){
        return knownSpells.get((int) (Math.random() * knownSpells.size()));
    }
    
    public boolean isEffectedBy(Effect effect){
        for(Effect e : effects) if(e.name().equalsIgnoreCase(effect.name())) return true;
        return false;
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

    public boolean hasPerformedAction(){
        return hasPerformedAction;
    }
        
    public boolean immuneTo(String effectName){
        return immuneTo.contains(effectName.toLowerCase().trim());
    }
    
    public String causeOfDeath(){
        return causeOfDeath;
    }
    
    public boolean isPlayer(){
        return this instanceof Player;
    }
        
    public boolean hasWon(){
        return hasWon;
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
    
    public void addKnownSpell(Spell spell){
        knownSpells.add(spell);
    }
         
    public void setHasPerformedAction(boolean hasPerformedAction){
        this.hasPerformedAction = hasPerformedAction;
    }
    
    public void setEquippable(EquipmentSlot slot, Equippable equippable){
        equippedItems.put(slot, equippable);
    }
    
    public void setAmmo(Ammo ammo) {
        if(this.ammo != null) {
            this.ammo.setEquipped(false);
            if (this.ammo == ammo) {
                this.ammo = null;
                return;
            }
        }
        this.ammo = ammo;
        if(this.ammo != null) this.ammo.setEquipped(true);
    }
    
    public void setAttackedBy(Creature attackedBy){
        this.attackedBy = attackedBy;
        if(aggressionCooldown == 0 && hp > 0 && !isPlayer() && aiType.equalsIgnoreCase("neutral")) doAction(new ColoredString("get angry", Colors.RED));
        aggressionCooldown = 10;
    }
    
    public void setHasWon(boolean hasWon){
        this.hasWon = hasWon;
    }
    
    @Override
    public Entity newInstance() {
        Creature c = (Creature) super.newInstance();
        return c;
    }
}

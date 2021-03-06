package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.pathfinding.Path;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.maths.Line;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.stats.LevelUpController;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.Tile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 14/07/2017 at 11:11 AM.
 */
public class CreatureAI {
    
    protected Creature creature;

    protected CreatureAI() {}

    public CreatureAI(Creature creature){
        this.creature = creature;
        if(creature != null) creature.setAi(this);
    }
    
    public void update(){

    }

    protected int pathTo(Point point){
        return pathTo(point.x, point.y);
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
            int x = (int) Math.round((Math.random() * 2) - 1);
            int y = (int) Math.round((Math.random() * 2) - 1);
            creature.moveBy(x, y, 0);
        }
    }
    
    protected boolean canPickup(){
        if(creature.inventory().isFull()) return false;
        return Math.random() < 0.5 && creature.hasFlag("smart");
    }
    
    protected boolean canUseRanged(Creature other){
        Equippable weapon = creature.getEquippedAt(EquipmentSlot.WEAPON);
        if(weapon == null || creature.ammo() == null) return false;
        return weapon.rangedDamage() != null && weapon.ammoType().equalsIgnoreCase(creature.ammo().name()) && creature.canSee(other);
    }
    
    protected boolean canUseSpell(Creature other){
        if(creature.knownSpells().isEmpty() || creature.availableSpells().isEmpty() || !creature.canSee(other)) return false;
        
        int minMannaRequired = Integer.MAX_VALUE;
        for(Spell spell : creature.availableSpells()) if(spell.manaCost() < minMannaRequired) minMannaRequired = spell.manaCost();
        if(creature.mana() < minMannaRequired) return false;
        
        boolean canUseSpell = false;
        for(Spell spell : creature.availableSpells()){
            if(!other.isEffectedBy(spell.effect())){
                canUseSpell = true;
                break;
            }
        }
        
        return canUseSpell;
    }
    
    protected boolean equipBestWeapon(){
        if(creature.inventory().isEmpty()) return false;
        Equippable best = creature.getEquippedAt(EquipmentSlot.WEAPON);
        int bestScore = best != null ? best.score() : Integer.MIN_VALUE;
        boolean betterItemFound = false;

        for(Item i : creature.inventory().items()){
            if(!(i instanceof Equippable)) continue;
            Equippable equippable = (Equippable) i;
            if(equippable.score() > bestScore) {
                best = equippable;
                bestScore = best.score();
                betterItemFound = true;
            }
        }
        
        if(betterItemFound){
            creature.equip(best);
            return true;
        }
        
        return false;
    }
    
    public void onGainLevel(){
        new LevelUpController().autoLevelUp(creature);
    }
    
    public boolean onMove(int xp, int yp, int zp){
//        
//        if(creature.world().tile(xp, yp, zp).type().equalsIgnoreCase("tree") && !creature.world().tile(xp, yp, zp).name().startsWith("stump")){
//            if(creature.getEquippedAt(EquipmentSlot.WEAPON) != null && creature.getEquippedAt(EquipmentSlot.WEAPON).name().equalsIgnoreCase("axe")) {
//                creature.doAction(new ColoredString("swing an axe at the tree"));
//                if (Math.random() < 0.4) {
//                    
//                    Item log = (Item) Codex.items.get("log").entity().newInstance();
//                    if (!creature.inventory().isFull()) {
//                        creature.doAction(new ColoredString("chop a log off the tree"));
//                        creature.inventory().add(log);
//                    } else {
//                        creature.notify(new ColoredString("There is no room for the log in your inventory.", Colors.ORANGE));
//                        Point drop = creature.world().getEmptyItemDropPoint(creature.x, creature.y, creature.z);
//                        creature.world().add(drop.x, drop.y, drop.z, log);
//                    }
//
//                    if(Math.random() < 0.7){
//                        Point drop = creature.world().getEmptyItemDropPoint(creature.x, creature.y, creature.z);
//                        creature.world().add(drop.x, drop.y, drop.z, (Item) Codex.items.get("apple").entity().newInstance());
//                    }
//                    
//                    if(Math.random() < 0.4){
//                        creature.world().level(zp).setTile(xp, yp, Tile.getTile(Math.random() < 0.5 ? "stump1" : "stump2").id);
//                        creature.notify(new ColoredString("The tree falls."));
//                    }
//                }
//            }
//        }
//        
        if(!canEnter(xp, yp, zp)) return false;
        creature.world().move(xp, yp, zp, creature);
        return true;
    }
    
    public boolean canEnter(int xp, int yp, int zp){
        for(int y = 0; y < creature.size(); y++){
            for(int x = 0; x < creature.size(); x++){
                Tile t = creature.world().tile(x + xp, y + yp, zp);
                if(t.canFly() && creature.hasFlag("can_fly")) continue;
                MapObject obj = creature.world().mapObject(x + xp, y + yp, zp);
                if(t.solid() || (obj != null && obj.tile().solid())) return false;
                Creature c = creature.world().creature(x + xp, y + yp, zp);
                if(c == null) continue;
                else if(c.id != creature.id) return false;
            }
        }
        return true;
    }
    
    public boolean canSee(int xp, int yp, int zp){
        if(creature.z != zp) return false;
        if((creature.x - xp) * (creature.x - xp) + (creature.y - yp) * (creature.y - yp) > creature.vision() * creature.vision()) return false;
        
        boolean blocked = false;
                
        for(int ys = 0; ys < creature.size(); ys++){
            int ya = ys + yp;
            for(int xs = 0; xs < creature.size(); xs++){
                int xa = xs + xp;
                for(Point p : new Line(creature.x, creature.y, xa, ya)){
                    if(creature.world().tile(p.x, p.y, zp).canSee() || p.x == xa && p.y == ya) continue;
                    blocked = true;
                    break;
                }
                if(!blocked) return true;
            }
        }
        
        return false;
    }
    
    public void notify(ColoredString message){
        
    }
    
}

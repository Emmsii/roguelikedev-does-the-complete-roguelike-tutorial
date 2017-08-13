package com.mac.rltut.game.world;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.FieldOfView;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.effects.*;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Consumable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public class World {
    
    private int width, height, depth;
    private long seed;

    private Level[] levels;

    private int entityId;
    private HashMap<Integer, List<Creature>> creatureList;
    private HashMap<Integer, List<Item>> itemList;
    private Creature[][][] creatureArray;
    private Item[][][] itemArray;

    private FieldOfView fov;
    private int[] totalExplorableTiles;
    
    private DayNightController dayNightController;

    private Player player;

    protected World() {}
    
    public World(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.levels = new Level[depth];
        this.creatureList = new HashMap<>();
        this.itemList = new HashMap<>();
        this.creatureArray = new Creature[width][height][depth];
        this.itemArray = new Item[width][height][depth];
        this.entityId = 0;
        
        this.fov = new FieldOfView(this);
        this.totalExplorableTiles = new int[depth];
        
        this.dayNightController = new DayNightController(600, 5, 20, 3);

        for(int z = 0; z < depth; z++) {
            creatureList.put(z, new ArrayList<>());
            itemList.put(z, new ArrayList<>());
        }
    }    
    
    public void init(){
        for(int z = 0; z < depth; z++){
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    setExplored(x, y, z, false);
                    setVisible(x, y, z, true);
                    level(z).setBlood(x, y, false);
                    if(!solid(x, y, z)) totalExplorableTiles[z]++;
                }
            }
        }
    }
    
    public void update(int z){
        List<Creature> toUpdate = new ArrayList<Creature>(creatureList.get(z));
        for (Creature c : toUpdate) c.update();
    }
    
    /* FOV Methods */
    
    public void computeFov(int x, int y, int z, int radius, FieldOfView.FOVType type){
        fov.clearFov();
        fov.compute(x, y, z, radius, type);
    }
    
    public void setExplored(int x, int y, int z, boolean value){
        level(z).setExplored(x, y, value);
    }
    
    public void setVisible(int x, int y, int z, boolean value){
        level(z).setVisible(x, y, value);
    }
    
    public boolean isExplored(int x, int y, int z){
        return level(z).isExplored(x, y);
    }
    
    public boolean isVisible(int x, int y, int z){
        return level(z).isVisible(x, y);
    }
    
    public boolean inFov(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        return fov.inFov(x, y, z);
    }
    
    /* Util Methods */
    
    public Point randomEmptyPoint(int z){
        int x, y;
        
        do{
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }while(solid(x, y, z) || creature(x, y, z) != null);
        
        return new Point(x, y, z);
    }
    
    public Point randomEmptyPointNearType(int z, String type){
        int x, y;
        boolean nearType;
        
        do{
            nearType = false;
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
                        
            for(Point p : new Point(x, y, z).neighboursAll()){
                if(tile(p.x, p.y, p.z).isType(type)) {
                    nearType = true;
                    break;
                }
            }
            
        }while(solid(x, y, z) || creature(x, y, z) != null || !nearType);

        return new Point(x, y, z);
    }

    public Point randomEmptyPointInRadius(Point point, int radius){
        int x, y;
        int half = radius / 2;
        int tries = 0;
        do{
            if(tries++ > width * half) return null;
            x = MathUtil.range(point.x - half, point.x + half, new Random());
            y = MathUtil.range(point.y - half, point.y + half, new Random());
            
        }while (solid(x, y, point.z) || creature(x, y, point.z) != null || MathUtil.distance(x, y, point.x, point.y) > half);
        
        return new Point(x, y, point.z);
    }

    /* Entity Methods */

    //Entity Adders --------------------
    
    public void add(int x, int y, int z, Creature creature){
        if(!inBounds(x, y, z)) return;
        creatureList.get(z).add(creature);
        
        for(int ya = 0; ya < creature.size(); ya++){
            int yb = y + ya;
            for(int xa = 0; xa < creature.size(); xa++){
                int xb = x + xa;
                creatureArray[xb][yb][z] = creature;
            }
        }
        creature.x = x;
        creature.y = y;
        creature.z = z;
        creature.init(entityId++, this);
        if(creature.isPlayer()) this.player = (Player) creature;
    }
    
    public void add(int x, int y, int z, Item item){
        if(!inBounds(x, y, z)) return;
        itemList.get(z).add(item);
        itemArray[x][y][z] = item;
        item.x = x;
        item.y = y;
        item.z = z;
        item.init(entityId++, this);
    }
    
    public void addCorpse(Creature dead){
        if(!inBounds(dead.x, dead.y, dead.z) || tile(dead.x, dead.y, dead.z).isType("water")) return;
        addAtEmptyPoint(dead.x, dead.y, dead.z, new Consumable(dead.name() + " corpse", String.format("The corpse of a %s", dead.name().toLowerCase()), Sprite.get("corpse"), "eat", Math.random() < 0.45 ? new Poison(2, 7, 1f) : new Heal(5, 1f)));
        if(dead.hasFlag("no_blood")) return;
        for(int y = -1; y <= 1; y++){
            int ya = y + dead.y;
            for(int x = -1; x <= 1; x++){
                int xa = x + dead.x;
                if(solid(xa, ya, dead.z) || mapObject(xa, ya, dead.z) != null) continue;
                if(Math.random() < 0.275) level(dead.z).setBlood(xa, ya, true);
            }
        }
    }
    
    public Point getEmptyItemDropPoint(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;

        List<Point> points = new ArrayList<Point>();
        List<Point> checked = new ArrayList<Point>();

        points.add(new Point(x, y, z));

        while(!points.isEmpty()){
            Point p = points.remove(0);
            checked.add(p);

            if(solid(p.x, p.y, p.z)) continue;
            if(item(p.x, p.y, p.z) == null) return p;
            else{
                List<Point> neighbours = p.neighboursCardinal();
                neighbours.removeAll(checked);
                points.addAll(neighbours);
            }
        }
        return null;
    }
    
    public boolean addAtEmptyPoint(int x, int y, int z, Item item){
        
        if(!inBounds(x, y, z)) return false;
        
        List<Point> points = new ArrayList<Point>();
        List<Point> checked = new ArrayList<Point>();
        
        points.add(new Point(x, y, z));
        
        while(!points.isEmpty()){
            Point p = points.remove(0);
            checked.add(p);
            
            if(solid(p.x, p.y, p.z)) continue;
            if(item(p.x, p.y, p.z) == null){
                add(p.x, p.y, p.z, item);
                Creature c = creature(p.x, p.y, p.z);
                if(c != null){
                    if(item instanceof ItemStack) c.notify(new ColoredString("%s lands at your feet."), StringUtil.capitalizeFirst(item.name()));
                    else c.notify(new ColoredString("A %s lands at your feet."), item.name());
                }
                return true;
            }else{
                List<Point> neighbours = p.neighboursCardinal();
                neighbours.removeAll(checked);
                points.addAll(neighbours);
            }
        }
        return false;
    }

    //Entity Removers --------------------
    
    public void remove(Creature creature){
        creatureList.get(creature.z).remove(creature);
        for(int ya = 0; ya < creature.size(); ya++) {
            int yb = creature.y + ya;
            for (int xa = 0; xa < creature.size(); xa++) {
                int xb = creature.x + xa;
                creatureArray[xb][yb][creature.z] = null;
            }
        }
    }
    
    public void remove(Item item){
        itemList.get(item.z).remove(item);
        itemArray[item.x][item.y][item.z] = null;
    }

    //Entity Movement --------------------
    
    public void move(int xp, int yp, int zp, Creature creature){
        if(!inBounds(xp, yp, zp)) return;

        for(int ya = 0; ya < creature.size(); ya++) {
            int yb = creature.y + ya;
            for (int xa = 0; xa < creature.size(); xa++) {
                int xb = creature.x + xa;
                creatureArray[xb][yb][creature.z] = null;
            }
        }

        if(creature.z != zp) switchLevels(creature.z, zp, creature);
        creature.x = xp;
        creature.y = yp;
        creature.z = zp;

        for(int ya = 0; ya < creature.size(); ya++) {
            int yb = creature.y + ya;
            for (int xa = 0; xa < creature.size(); xa++) {
                int xb = creature.x + xa;
                creatureArray[xb][yb][creature.z] = creature;
            }
        }
    }
    
    public void moveUp(Creature creature){
        Point spawn = startPointAt(creature.z - 1);
        if(spawn != null) move(spawn.x, spawn.y, spawn.z, creature);
    }

    public void moveDown(Creature creature){
        Point spawn = startPointAt(creature.z + 1);
        if(spawn != null) move(spawn.x, spawn.y, spawn.z, creature);
    }
    
    public void switchLevels(int oldZ, int newZ, Creature creature){
        creatureList.get(newZ).add(creature);
        creatureList.get(oldZ).remove(creature);
    }

    //Entity Getters --------------------
    
    public Creature creature(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        Creature c = creatureArray[x][y][z];
        if(c != null && c.hasFlag("invisible")) return null;
        return c;
    }
    
    public List<Creature> creatures(int z){
        if(!inBounds(0, 0, z)) return null;
        return creatureList.get(z);
    }
    
    public Item item(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        return itemArray[x][y][z];
    }
    
    public List<Item> items(int z){
        if(!inBounds(0, 0, z)) return null;
        return itemList.get(z);
    }
    
    public Player player(){
        return player;
    }

    //Entity Utils ------------------
    
    /* World Methods */
    
    public Level level(int z){
        return levels[z];        
    }

    public void setLevel(int z, Level level){
        if(!inBounds(0, 0, z)) return;
        levels[z] = level;
    }
    
    public Tile tile(int x, int y, int z){
        if(!inBounds(x, y, z)) return Tile.getTile("empty");
        return levels[z].tile(x, y);
    }

    public boolean solid(int x, int y, int z){
        if(!inBounds(x, y, z)) return true;
        return tile(x, y, z).solid();
    }
    
    public MapObject mapObject(int x, int y, int z){
        if(!inBounds(0, 0, z)) return null;
        return level(z).mapObject(x, y);
    }
    
    public Point startPointAt(int z){
        if(!inBounds(0, 0, z)) return null;
        return levels[z].startPoint();
    }
    
    public Consumable pickMushroom(int x, int y, int z){
        if(!tile(x, y, z).name().equalsIgnoreCase("mushroom")) return null;
        level(z).setTile(x, y, Tile.getTile("grassSmallGreen").id);
        Consumable mushroom = (Consumable) Codex.items.get("mushroom").entity().newInstance();
        Pool<Effect> pool = new Pool<Effect>();
        pool.add(new Heal(10, 1f), 100);
        pool.add(new Poison(2, 5, 1f), 20);
        pool.add(new Blind(10, 1f), 5);
        pool.add(new Rage(4, 20, 1f), 1);
        mushroom.setEffect(pool.get());
        return mushroom;
    }
        
    /* Misc Methods */
    
    public boolean inBounds(int x, int y, int z){
        return x >= 0 && y >= 0  && z >= 0 && x < width && y < height && z < depth;
    }
    
    /* Getter Methods */
        
    public int exploredPercent(int z){
        return (int) (((float) level(z).exploredTiles() / (float) totalExplorableTiles[z]) * 100);
    }

    public DayNightController dayNightController(){
        return dayNightController;
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }
    
    public int depth(){
        return depth;
    }

}

package com.mac.rltut.game.world;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.FieldOfView;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
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
    
    private final int width, height, depth;
    private final long seed;
    
    private int entityId;
    
    private Level[] levels;
    private boolean[][][] explored;
    private boolean[][][] visible;
    private HashMap<Integer, List<Creature>> creatureList;
    private HashMap<Integer, List<Item>> itemList;
    private Creature[][][] creatureArray;
    private Item[][][] itemArray;
    
    private FieldOfView fov;
    
    private int[] exploredTiles;
    private int[] totalTiles;
    
    public World(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.levels = new Level[depth];
        this.explored = new boolean[width][height][depth];
        this.visible = new boolean[width][height][depth];
        this.creatureList = new HashMap<>();
        this.itemList = new HashMap<>();
        this.creatureArray = new Creature[width][height][depth];
        this.itemArray = new Item[width][height][depth];
                
        this.entityId = 0;
        
        this.fov = new FieldOfView(this);
        
        this.exploredTiles = new int[depth];
        this.totalTiles = new int[depth];

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
                    if(!tile(x, y, z).solid()) totalTiles[z]++;
                }
            }
        }
    }
    
    public void update(int z){
        List<Creature> toUpdate = new ArrayList<Creature>(creatureList.get(z));
        for(Creature c : toUpdate) c.update();
    }
    
    /* FOV Methods */
    public void computeFov(int x, int y, int z, int radius, FieldOfView.FOVType type){
        fov.clearFov();
        fov.compute(x, y, z, radius, type);
    }
    
    public void setExplored(int x, int y, int z, boolean value){
        if(!inBounds(x, y, z)) return;
        if(!tile(x, y, z).solid() && value && !explored[x][y][z]) exploredTiles[z]++;
        explored[x][y][z] = value;
    }
    
    public void setVisible(int x, int y, int z, boolean value){
        if(!inBounds(x, y, z)) return;
        visible[x][y][z] = value;
    }
    
    public boolean isExplored(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        return explored[x][y][z];
    }
    
    public boolean isVisible(int x, int y, int z){
        if(!inBounds(x, y, z)) return false;
        return visible[x][y][z];
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
        }while(tile(x, y, z).solid() || creature(x, y, z) != null);
        
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
            
        }while(tile(x, y, z).solid() || creature(x, y, z) != null || !nearType);

        return new Point(x, y, z);
    }

    public Point randomEmptyPointInRadius(Point point, int radius){
        int x, y;
        int half = radius / 2;
        do{
            x = MathUtil.range(point.x - half, point.x + half, new Random());
            y = MathUtil.range(point.y - half, point.y + half, new Random());
        }while (tile(x, y, point.z).solid() || creature(x, y, point.z) != null || MathUtil.distance(x, y, point.x, point.y) > half);
        
        return new Point(x, y, point.z);
    }

    /* Util Methods */
    
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
    
    public void remove(Creature creature){
        creatureList.get(creature.z).remove(creature);
        creatureArray[creature.x][creature.y][creature.z] = null;
    }
    
    public void remove(Item item){
        itemList.get(item.z).remove(item);
        itemArray[item.x][item.y][item.z] = null;
    }
    
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
    
    public Creature creature(int x, int y, int z){
        if(!inBounds(x, y, z)) return null;
        return creatureArray[x][y][z];
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
    
    //Tile Methods
    
    public Level level(int z){
        return levels[z];        
    }
    
    public Tile tile(int x, int y, int z){
        if(!inBounds(x, y, z)) return Tile.getTile("empty");
        return levels[z].tile(x, y);
    }

    public Point startPointAt(int z){
        if(!inBounds(0, 0, z)) return null;
        return levels[z].startPoint();
    }
    
    public void setLevel(int z, Level level){
        if(!inBounds(0, 0, z)) return;
        levels[z] = level;
    }
    
    //Misc
    
    public int exploredPercent(int z){
        return (int) (((float) exploredTiles[z] / (float) totalTiles[z]) * 100);
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
    
    public boolean inBounds(int x, int y, int z){
        return x >= 0 && y >= 0  && z >= 0 && x < width && y < height && z < depth;
    }
}

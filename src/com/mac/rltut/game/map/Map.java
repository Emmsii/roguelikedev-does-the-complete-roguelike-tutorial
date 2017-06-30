package com.mac.rltut.game.map;

import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.map.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public class Map {
    
    private final int width, height, depth;
    private final long seed;
    
    private HashMap<Integer, byte[][]> tiles;
    private HashMap<Integer, List<Creature>> creatureList;
    private HashMap<Integer, List<Item>> itemList;
    private Creature[][][] creatureArray;
    private Item[][][] itemArray;
    
    public Map(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.tiles = new HashMap<>();
        this.creatureList = new HashMap<>();
        this.itemList = new HashMap<>();
        this.creatureArray = new Creature[width][height][depth];
        this.itemArray = new Item[width][height][depth];
        
        for(int z = 0; z < depth; z++){
            tiles.put(z, new byte[width][height]);
            creatureList.put(z, new ArrayList<>());
            itemList.put(z, new ArrayList<>());
        }
        
    }    
    
    public void update(int z){
        List<Creature> toUpdate = new ArrayList<Creature>(creatureList.get(z));
        for(Creature c : toUpdate) c.update();
    }
    
    //Util Methods
    
    public Point randomEmptyPoint(int z){
        int x = 0, y = 0;
        
        do{
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        }while(tile(x, y, z).solid() || creature(x, y, z) != null);
        
        return new Point(x, y, z);
    }
    
    //Entity Methods
    
    public void add(int x, int y, int z, Creature creature){
        if(!inBounds(x, y, z)) return;
        creatureList.get(z).add(creature);
        creatureArray[x][y][z] = creature;
        creature.x = x;
        creature.y = y;
        creature.z = z;
        creature.init(this);
    }
    
    public void add(int x, int y, int z, Item item){
        if(!inBounds(x, y, z)) return;
        itemList.get(z).add(item);
        itemArray[x][y][z] = item;
        item.x = x;
        item.y = y;
        item.z = z;
        item.init(this);
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
        creatureArray[creature.x][creature.y][creature.z] = null;
        creatureArray[xp][yp][zp] = creature;
        if(creature.z != zp){
            creatureList.get(creature.z).remove(creature);
            creatureList.get(zp).add(creature);
        }
        creature.x = xp;
        creature.y = yp;
        creature.z = zp;
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
    
    public Tile tile(int x, int y, int z){
        if(!inBounds(x, y, z)) return Tile.empty;
        return Tile.getTile(tiles.get(z)[x][y]);
    }
    
    public void setTile(int x, int y, int z, Tile tile){
        if(!inBounds(x, y, z)) return;
        tiles.get(z)[x][y] = tile.id;
    }
    
    public void setTiles(int z, byte[][] tiles){
        if(!inBounds(0, 0, z)) return;
        this.tiles.put(z, tiles);
    }
    
    //Misc
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }
    
    public boolean inBounds(int x, int y, int z){
        return x >= 0 && y >= 0  && z >= 0 && x < width && y < height && z < depth;
    }
}

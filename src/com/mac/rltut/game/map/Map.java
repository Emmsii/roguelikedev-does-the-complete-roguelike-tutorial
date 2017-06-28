package com.mac.rltut.game.map;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.map.tile.Tile;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public class Map {
    
    private final int width, height, depth;
    private final long seed;
    private HashMap<Integer, byte[][]> tiles;

    public Map(int width, int height, int depth, long seed){
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.seed = seed;
        this.tiles = new HashMap<>();
        for(int i = 0; i < depth; i++) tiles.put(i, new byte[width][height]);
    }    
    
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

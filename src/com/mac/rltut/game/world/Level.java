package com.mac.rltut.game.world;

import com.mac.rltut.game.world.tile.Tile;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 02/07/2017 at 05:26 PM.
 */
public class Level {
    
    private final int width, height;
    private final int z;
    
    private byte[][] tiles;
    
    public Level(int width, int height, int z){
        this.width = width;
        this.height = height;
        this.z = z;
        this.tiles = new byte[width][height];
    }
      
    public Tile tile(int x, int y){
        if(!inBounds(x, y)) return Tile.empty;
        return Tile.getTile(tiles[x][y]);
    }
        
    public void setTiles(byte[][] tiles){
        this.tiles = tiles;
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }
    
    public int z(){
        return z;
    }
    
    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    } 
    
}

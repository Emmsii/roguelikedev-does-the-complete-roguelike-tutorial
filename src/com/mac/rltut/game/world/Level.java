package com.mac.rltut.game.world;

import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.world.objects.Chest;
import com.mac.rltut.game.world.objects.MapObject;
import com.mac.rltut.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 02/07/2017 at 05:26 PM.
 */
public class Level {
    
    private String type;
    private int width, height;
    private int z;
    
    private Point startPoint;
    private byte[][] tiles;
    private MapObject[][] mapObjects;
    private List<Chest> chests;
    private boolean[][] blood;
    
    private boolean[][] explored;
    private boolean[][] visible;
    private int exploredTiles;
    
    public Level() {}
    
    public Level(String type, int width, int height, int z){
        this.type = type;
        this.width = width;
        this.height = height;
        this.z = z;
        this.tiles = new byte[width][height];
        this.mapObjects = new MapObject[width][height];
        this.chests = new ArrayList<Chest>();
        this.blood = new boolean[width][height];
        this.explored = new boolean[width][height];
        this.visible = new boolean[width][height];
        this.exploredTiles = 0;
    }
      
    public Tile tile(int x, int y){
        if(!inBounds(x, y)) return Tile.getTile("empty");
        return Tile.getTile(tiles[x][y]);
    }
    
    public MapObject mapObject(int x, int y){
        if(!inBounds(x, y)) return null;
        return mapObjects[x][y];
    }
    
    public boolean blood(int x, int y){
        if(!inBounds(x, y)) return false;
        return blood[x][y];
    }

    public void setExplored(int x, int y, boolean value){
        if(!inBounds(x, y)) return;
        if(!tile(x, y).solid() && value && !explored[x][y]) exploredTiles++;
        explored[x][y] = value;
    }

    public void setVisible(int x, int y, boolean value){
        if(!inBounds(x, y)) return;
        visible[x][y] = value;
    }

    public boolean isExplored(int x, int y) {
        if(!inBounds(x, y)) return false;
        return explored[x][y];
    }

    public boolean isVisible(int x, int y){
        if(!inBounds(x, y)) return false;
        return visible[x][y];
    }
    
    public int exploredTiles(){
        return exploredTiles;
    }
    
    public void setTiles(byte[][] tiles){
        this.tiles = tiles;
    }
    
    public void setMapObjects(MapObject[][] mapObjects){
        this.mapObjects = mapObjects;
    }
    
    public void setChests(List<Chest> chests){
        this.chests = chests;
    }
    
    public void setBlood(int x, int y, boolean value){
        if(!inBounds(x, y)) return;
        blood[x][y] = value;
    }
    
    public void setStart(Point startPoint){
        this.startPoint = startPoint;
    }
    
    public Point startPoint(){
        return startPoint;
    }
    
    public String type(){
        return type;
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

    public List<Chest> chests(){
        return chests;
    }

    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }
    
}

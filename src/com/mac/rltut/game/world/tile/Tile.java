package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public class Tile {
    
    public static HashMap<String, Tile> tileNames = new HashMap<>();
    private static Tile[] tiles = new Tile[256]; 
    
    public byte id;
    private String name;
    private String description;
    private Sprite sprite;
    private String type;
    protected boolean solid;
    protected boolean canSee;
    protected boolean canFly;

    public Tile() {}
    
    public Tile(byte id, String name, String description, Sprite sprite, String type, boolean solid, boolean canSee, boolean canFly){
        this.id = id;
        this.name = name;
        this.description = description;
        this.sprite = sprite;
        this.type = type;
        this.solid = solid;
        this.canSee = canSee;
        this.canFly = canFly;
    }
    
    public String name(){
        return name;
    }
    
    public String description(){
        return description;
    }
    
    public Sprite sprite(){
        return sprite;
    }
    
    public String type(){
        return type;
    }
    
    public boolean isType(String check){
        return type.trim().equalsIgnoreCase(check.trim());
    }
    
    public boolean solid(){
        return solid;
    }
    
    public boolean canSee(){
        return canSee;
    }
    
    public boolean canFly(){
        return canFly;
    }
    
    public static void addTile(Tile tile){
        if(tile.id > tiles.length) throw new RuntimeException("Tile count reached [" + tiles.length + "]");
        if(tiles[tile.id] != null) throw new RuntimeException("Duplicate tile id [" + tile.id + "] " + tile.name() + " [" + tiles[tile.id].name() + "]");
        tiles[tile.id] = tile;
        tileNames.put(tile.name, tile);
    }
    
    public static Tile getTile(byte id){
        return tiles[id];
    }
    
    public static Tile getTile(String name){
        return tileNames.get(name);
    }
}

package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public abstract class Tile {
    
    private static Tile[] tiles = new Tile[256]; 
    private static int currentId = 0;

    public static final Tile empty = new EmptyTile(Sprite.get("empty"), "empty");
    public static final Tile floor = new EmptyTile(Sprite.get("empty"), "floor");

    //TILES
    public static final Tile wallTopRed = new SolidTile(Sprite.get("wallTopRed"), "wall");
    public static final Tile wallTopBlue = new SolidTile(Sprite.get("wallTopBlue"), "wall");
    public static final Tile wallSide = new SolidTile(Sprite.get("wallSide"), "wall");
    public static final Tile stairDown = new EmptyTile(Sprite.get("stairDown"), "stair");
    public static final Tile stairUp = new EmptyTile(Sprite.get("stairUp"), "stair");
    public static final Tile grassYellow = new EmptyTile(Sprite.get("grassYellow"), "grass");
    public static final Tile grassGreen = new EmptyTile(Sprite.get("grassGreen"), "grass");
    public static final Tile grassBlue = new EmptyTile(Sprite.get("grassBlue"), "grass");
    public static final Tile grassPurple = new EmptyTile(Sprite.get("grassPurple"), "grass");

    public static final Tile treeDeciduous = new SolidTile(Sprite.get("treeDeciduous"), "tree");
    public static final Tile treeConifer = new SolidTile(Sprite.get("treeConifer"), "tree");

    public static final Tile waterFoul = new ImpassableTile(Sprite.get("waterPurple"), "water");
    public static final Tile waterDirty = new ImpassableTile(Sprite.get("waterBrown"), "water");
    public static final Tile waterBlue = new ImpassableTile(Sprite.get("waterBlue"), "water");
    public static final Tile waterLilypad = new ImpassableTile(Sprite.get("waterLilypad"), "water");
    public static final Tile waterBonesFoul1 = new ImpassableTile(Sprite.get("waterBonesPurple1"), "water");
    public static final Tile waterBonesFoul2 = new ImpassableTile(Sprite.get("waterBonesPurple2"), "water");
    public static final Tile waterBonesDirty1 = new ImpassableTile(Sprite.get("waterBonesBrown1"), "water");
    public static final Tile waterBonesDirty2 = new ImpassableTile(Sprite.get("waterBonesBrown2"), "water");

    public static final Tile chestGold = new ChestTile(Sprite.get("chestGoldClosed"), Sprite.get("chestGoldOpen"), "chest");
    public static final Tile chestSilver = new ChestTile(Sprite.get("chestSilverClosed"), Sprite.get("chestSilverOpen"), "chest");

    public static final Tile mushroom = new EmptyTile(Sprite.get("mushroom"), "mushroom");
        
    public final byte id;
    private Sprite sprite;
    private String type;
    protected boolean solid;
    protected boolean see;
    protected boolean fly;
    
    public Tile(Sprite sprite, String type){
        this.id = (byte) Tile.currentId++;
        this.sprite = sprite;
        this.type = type;
        this.solid = false;
        this.see = false;
        this.fly = false;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tile id [" + id + "]");
        tiles[id] = this;
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
        return see;
    }
    
    public boolean fly(){
        return fly;
    }
    
    public static Tile getTile(byte id){
        if(id < 0 || id >= tiles.length) return Tile.empty;
        return tiles[id];
    }
}

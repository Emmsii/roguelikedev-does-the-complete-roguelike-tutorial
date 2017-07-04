package com.mac.rltut.game.world.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public abstract class Tile {
    
    private static Tile[] tiles = new Tile[256]; 
    private static int currentId = 0;
    
    public static final Tile empty = new EmptyTile(Sprite.empty, "empty");
    public static final Tile floor = new EmptyTile(Sprite.empty, "floor");
    
    //TILES
    public static final Tile wallTopRed = new SolidTile(Sprite.wallTopRed, "wall");
    public static final Tile wallTopBlue = new SolidTile(Sprite.wallTopBlue, "wall");
    public static final Tile wallSide = new SolidTile(Sprite.wallSide, "wall");
    public static final Tile stairDown = new EmptyTile(Sprite.stairDown, "stair");
    public static final Tile stairUp = new EmptyTile(Sprite.stairUp, "stair");
    public static final Tile grassYellow = new EmptyTile(Sprite.grassYellow, "grass");
    public static final Tile grassGreen = new EmptyTile(Sprite.grassGreen, "grass");
    public static final Tile grassBlue = new EmptyTile(Sprite.grassBlue, "grass");
    public static final Tile grassPurple = new EmptyTile(Sprite.grassPurple, "grass");
    
    public static final Tile treeDeciduous = new SolidTile(Sprite.treeDeciduous, "tree");
    public static final Tile treeConifer = new SolidTile(Sprite.treeConifer, "tree");

    public static final Tile lavaLight = new ImpassableTile(Sprite.lavaYellow, "lava");
    public static final Tile lavaDark = new ImpassableTile(Sprite.lavaBrown, "lava");
    
    public static final Tile waterFoul = new ImpassableTile(Sprite.waterPurple, "water");
    public static final Tile waterDirty = new ImpassableTile(Sprite.waterBrown, "water");
    public static final Tile waterBlue = new ImpassableTile(Sprite.waterBlue, "water");
    public static final Tile waterLilypad = new ImpassableTile(Sprite.waterLilypad, "water");
    public static final Tile waterBonesFoul1 = new ImpassableTile(Sprite.waterBonesPurple1, "water");
    public static final Tile waterBonesFoul2 = new ImpassableTile(Sprite.waterBonesPurple2, "water");
    public static final Tile waterBonesDirty1 = new ImpassableTile(Sprite.waterBonesBrown1, "water");
    public static final Tile waterBonesDirty2 = new ImpassableTile(Sprite.waterBonesBrown2, "water");
    
    public static final Tile blood = new EmptyTile(Sprite.blood, "blood");

    public static final Tile doorSilver = new EmptyTile(Sprite.doorSilver, "door");
    public static final Tile doorGold = new EmptyTile(Sprite.doorGold, "door");
    public static final Tile doorGreen = new EmptyTile(Sprite.doorGreen, "door");
    public static final Tile doorPurple = new EmptyTile(Sprite.doorPurple, "door");
    
    public static final Tile chestGold = new ChestTile(Sprite.chestGoldClosed, Sprite.chestGoldOpen, "chest");
    public static final Tile chestSilver = new ChestTile(Sprite.chestSilverClosed, Sprite.chestSilverOpen, "chest");
    
    public static final Tile mushroom = new EmptyTile(Sprite.mushroom, "mushroom");
    
    
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
    
    public boolean see(){
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

package com.mac.rltut.game.map.tile;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:12 AM.
 */
public abstract class Tile {
    
    public static Tile[] tiles = new Tile[256]; 
    
    public static final Tile empty = new EmptyTile(0, Sprite.empty);
    
    //TILES
    public static final Tile grassYellow = new EmptyTile(1, Sprite.grassYellow);
    public static final Tile grassGreen = new EmptyTile(2, Sprite.grassGreen);
    public static final Tile grassBlue = new EmptyTile(3, Sprite.grassBlue);
    public static final Tile grassPurple = new EmptyTile(4, Sprite.grassPurple);
    public static final Tile treeDeciduous = new SolidTile(5, Sprite.treeDeciduous);
    public static final Tile treeConifer = new SolidTile(6, Sprite.treeConifer);

    public static final Tile lavaYellow = new ImpassableTile(7, Sprite.lavaYellow);
    public static final Tile lavaBrown = new ImpassableTile(8, Sprite.lavaBrown);
    public static final Tile waterPurple = new ImpassableTile(9, Sprite.waterPurple);
    public static final Tile waterBrown = new ImpassableTile(10, Sprite.waterBrown);
    public static final Tile waterBlue = new ImpassableTile(11, Sprite.waterBlue);
    public static final Tile waterLilypad = new ImpassableTile(12, Sprite.waterLilypad);
    public static final Tile waterBonesPurple1 = new ImpassableTile(13, Sprite.waterBonesPurple1);
    public static final Tile waterBonesPurple2 = new ImpassableTile(14, Sprite.waterBonesPurple2);
    public static final Tile waterBonesBrown1 = new ImpassableTile(15, Sprite.waterBonesBrown1);
    public static final Tile waterBonesBrown2 = new ImpassableTile(16, Sprite.waterBonesBrown2);
    
    public final byte id;
    private Sprite sprite;
    protected boolean solid;
    protected boolean transparent;
    
    public Tile(int id, Sprite sprite){
        this.id = (byte) id;
        this.sprite = sprite;
        this.solid = false;
        this.transparent = false;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tile id [" + id + "]");
        tiles[id] = this;
    }
    
    public Sprite sprite(){
        return sprite;
    }
    
    public boolean solid(){
        return solid;
    }
    
    public boolean transparent(){
        return transparent;
    }
    
    public static Tile getTile(byte id){
        if(id < 0 || id >= tiles.length) return Tile.empty;
        return tiles[id];
    }
}

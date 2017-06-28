package com.mac.rltut.game.map.tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 12:30 PM.
 */
public class TileBuilder {
        
    private static final Tile[] GRASS_TILES = { Tile.grassGreen, Tile.grassBlue, Tile.grassPurple, Tile.grassYellow };
    private static final Tile[] TREE_TILES = { Tile.treeConifer, Tile.treeDeciduous };
    
    public static Tile grassTile(Random random){
        return GRASS_TILES[random.nextInt(GRASS_TILES.length)];
    }

    public static Tile treeTile(Random random){
        return TREE_TILES[random.nextInt(TREE_TILES.length)];
    }
}

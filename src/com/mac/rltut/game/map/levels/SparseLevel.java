package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class SparseLevel extends LevelBuilder{
    
    public SparseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Sparse", width, height, minLevel, maxLevel, chance, zMultiplier, random);

        addTreeType(Tile.treeConifer, 40);
        addTreeType(Tile.treeDeciduous, 50);
        addLiquidType(Tile.waterBlue, 100);
        addWallType(Tile.wallTopBlue, 10);
        addWallType(Tile.wallTopRed, 90);

        clearDecalTiles();
        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 70, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 2, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 20, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 20, Tile.empty);
        
        setProperty("tree_random_frequency", "0.325-0.35");
        setProperty("tree_smooth", "2-3");
        setProperty("liquid_random_frequency", "0.3-0.33");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", 3);
        setProperty("min_region_size", 80);
        setProperty("room_count", "5-10");
        setProperty("room_size_min", "7-8");
        setProperty("room_size_max", "10-13");
    }
}

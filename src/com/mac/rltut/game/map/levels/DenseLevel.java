package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class DenseLevel extends LevelBuilder{
    
    public DenseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Dense", width, height, minLevel, maxLevel, chance, zMultiplier, random);
        
        addTreeType(Tile.treeConifer, 50);
        addTreeType(Tile.treeDeciduous, 45);
        addLiquidType(Tile.waterBlue, 100);

        clearDecalTiles();
        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 4, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 50, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 52, Tile.empty);

        setProperty("tree_random_frequency", "0.455-0.475");
        setProperty("tree_smooth", "6-7");
        setProperty("liquid_random_frequency", "0.32-0.4");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", 4);
        setProperty("min_region_size", 80);
        setProperty("room_count", "2-3");
        setProperty("room_size_min", "5-6");
        setProperty("room_size_max", "8-9");
    }
}
package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class DarkLevel extends LevelBuilder{
    
    public DarkLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Dark", width, height, minLevel, maxLevel, chance, zMultiplier, random);

        addTreeType(Tile.treeConifer, 80);
        addTreeType(Tile.treeDeciduous, 10);
        addLiquidType(Tile.waterBlue, 90);
        addLiquidType(Tile.waterDirty, 5);
        addWallType(Tile.wallTopBlue, 100);

        clearDecalTiles();
        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.waterBonesDirty1, 2, Tile.waterDirty);
        addDecalTile(Tile.waterBonesDirty2, 3, Tile.waterDirty);
        addDecalTile(Tile.mushroom, 7, Tile.empty, Tile.floor);
        addDecalTile(Tile.grassPurple, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.grassBlue, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 80, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 10, Tile.empty);

        setProperty("tree_random_frequency", "0.4475-0.475");
        setProperty("tree_smooth", "3-4");
        setProperty("liquid_random_frequency", "0.31-0.35");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", 4);
        setProperty("min_region_size", 80);
        setProperty("room_count", "7-8");
        setProperty("room_size_min", "5-6");
        setProperty("room_size_max", "8-9");
    }
}

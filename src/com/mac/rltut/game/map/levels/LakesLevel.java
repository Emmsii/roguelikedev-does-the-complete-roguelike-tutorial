package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class LakesLevel extends LevelBuilder{
    
    public LakesLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Lakes", width, height, minLevel, maxLevel, chance, zMultiplier, random);

        addTreeType(Tile.treeConifer, 40);
        addTreeType(Tile.treeDeciduous, 50);
        addLiquidType(Tile.waterBlue, 100);

        clearDecalTiles();
        addDecalTile(Tile.waterLilypad, 6, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 50, Tile.empty);
        addDecalTile(Tile.mushroom, 6, Tile.empty);
        addDecalTile(Tile.treeConifer, 20, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 20, Tile.empty);

        setProperty("tree_random_frequency", "0.325-0.425");
        setProperty("tree_smooth", "6-7");
        setProperty("liquid_random_frequency", "0.425-0.455");
        setProperty("liquid_smooth", "5-8");
        setProperty("border_thickness", 4);
        setProperty("min_region_size", 80);
    }
}

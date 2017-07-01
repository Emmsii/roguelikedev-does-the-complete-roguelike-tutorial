package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class SwampLevel extends LevelBuilder{
    
    public SwampLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Swamp", width, height, minLevel, maxLevel, chance, zMultiplier, random);

        addTreeType(Tile.treeConifer, 50);
        addTreeType(Tile.treeDeciduous, 45);
        addLiquidType(Tile.waterDirty, 80);
        addLiquidType(Tile.waterFoul, 10);
        addLiquidType(Tile.waterBlue, 5);

        clearTilesToPopulate();
        addTileToPopulate(Tile.waterBonesDirty1, 2, Tile.waterDirty);
        addTileToPopulate(Tile.waterBonesDirty2, 2, Tile.waterDirty);
        addTileToPopulate(Tile.waterBonesFoul1, 2, Tile.waterFoul);
        addTileToPopulate(Tile.waterBonesFoul2, 2, Tile.waterFoul);
        addTileToPopulate(Tile.grassPurple, 20, Tile.empty);
        addTileToPopulate(Tile.grassGreen, 70, Tile.empty);
        addTileToPopulate(Tile.treeConifer, 25, Tile.empty);
        addTileToPopulate(Tile.treeDeciduous, 25, Tile.empty);

        setProperty("tree_random_frequency", "0.32-0.4");
        setProperty("tree_smooth", "6-7");
        setProperty("liquid_random_frequency", "0.425-0.45");
        setProperty("liquid_smooth", "1-2");
        setProperty("border_thickness", 4);
        setProperty("min_region_size", 80);
    }
}

package com.mac.rltut.game.world.levels;

import com.mac.rltut.game.world.builders.ForrestLevelBuilder;
import com.mac.rltut.game.world.builders.LevelBuilder;
import com.mac.rltut.game.world.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/07/2017 at 12:00 PM.
 */
public class SparseLevel extends ForrestLevelBuilder {
    
    public SparseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Sparse", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 40);
        addTileType(Tile.treeDeciduous, 50);
        addTileType(Tile.waterBlue, 100);
        addTileType(Tile.wallTopBlue, 10);
        addTileType(Tile.wallTopRed, 90);
        addTileType(Tile.chestSilver, 100);

        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 70, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 2, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 20, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 20, Tile.empty);
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.325-0.35");
        setProperty("tree_smooth", "2-3");
        setProperty("liquid_random_frequency", "0.3-0.33");
        setProperty("room_count", "5-10");
        setProperty("room_size_min", "7-8");
        setProperty("room_size_max", "10-13");
    }
}

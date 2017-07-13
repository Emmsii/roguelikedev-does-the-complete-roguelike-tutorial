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
    
    public SparseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super("Sparse", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 40);
        addTileType(Tile.getTile("treeDeciduous"), 50);
        addTileType(Tile.getTile("waterBlue"), 100);
        addTileType(Tile.getTile("wallTopRed"), 100);
        addTileType(Tile.getTile("chestSilver"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 4, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("grassGreen"), 70, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("mushroom"), 2, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 20, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 20, Tile.getTile("empty"));
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

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
public class DenseLevel extends ForrestLevelBuilder {
    
    public DenseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super("Dense", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 50);
        addTileType(Tile.getTile("treeDeciduous"), 45);
        addTileType(Tile.getTile("waterBlue"), 100);
        addTileType(Tile.getTile("wallTopRed"), 100);
        addTileType(Tile.getTile("grassMediumGreen"), 100);
        addTileType(Tile.getTile("grassSmallGreen"), 30);
        addTileType(Tile.getTile("chestSilver"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 4, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("grassGreen"), 70, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("mushroom"), 4, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 70, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 60, Tile.getTile("empty"));
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.465-0.485");
        setProperty("liquid_random_frequency", "0.32-0.4");
        setProperty("border_thickness", "4");
        setProperty("room_count", "2-3");
    }
}

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
public class LakeLevel extends ForrestLevelBuilder {
    
    public LakeLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super("Lake", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 40);
        addTileType(Tile.getTile("treeDeciduous"), 50);
        addTileType(Tile.getTile("waterBlue"), 100);
        addTileType(Tile.getTile("grassMediumGreen"), 100);
        addTileType(Tile.getTile("grassSmallGreen"), 50);
        addTileType(Tile.getTile("chestSilver"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 6, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("reeds1"), 7, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("reeds2"), 7, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("grassGreen"), 50, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("mushroom"), 6, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 20, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 20, Tile.getTile("empty"));
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.325-0.425");
        setProperty("liquid_random_frequency", "0.445-0.465");
        setProperty("border_thickness", "4");
        setProperty("room_count", "0");
    }
}

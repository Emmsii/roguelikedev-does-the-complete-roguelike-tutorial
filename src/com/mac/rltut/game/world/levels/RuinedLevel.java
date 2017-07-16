package com.mac.rltut.game.world.levels;

import com.mac.rltut.game.world.builders.ForrestLevelBuilder;
import com.mac.rltut.game.world.builders.LevelBuilder;
import com.mac.rltut.game.world.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 02/07/2017 at 12:34 PM.
 */
public class RuinedLevel extends ForrestLevelBuilder {

    public RuinedLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super("Ruined", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 40);
        addTileType(Tile.getTile("treeDeciduous"), 50);
        addTileType(Tile.getTile("waterBlue"), 100);
        addTileType(Tile.getTile("wallTopRed"), 100);
        addTileType(Tile.getTile("grassMediumGreen"), 90);
        addTileType(Tile.getTile("grassMediumYellow"), 10);
        addTileType(Tile.getTile("grassSmallGreen"), 50);
        addTileType(Tile.getTile("grassSmallYellow"), 10);
        addTileType(Tile.getTile("chestGold"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 4, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("grassGreen"), 70, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("mushroom"), 2, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 20, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 20, Tile.getTile("empty"));
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.3-0.33");
        setProperty("tree_smooth", "2-3");
        setProperty("liquid_random_frequency", "0.28-0.32");
        setProperty("room_count", "25-30");
        setProperty("room_size_max", "8-11");
        setProperty("chest_chance", "0.45");
    }
}

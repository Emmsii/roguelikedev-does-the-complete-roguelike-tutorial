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

    public RuinedLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("ruined", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 40);
        addTileType(Tile.treeDeciduous, 50);
        addTileType(Tile.waterBlue, 100);
        addTileType(Tile.wallTopBlue, 10);
        addTileType(Tile.wallTopRed, 90);
        addTileType(Tile.chestGold, 100);

        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 70, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 2, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 20, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 20, Tile.empty);
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.3-0.33");
        setProperty("tree_smooth", "2-3");
        setProperty("liquid_random_frequency", "0.28-0.32");
        setProperty("room_count", "20-25");
        setProperty("room_size_max", "8-11");
        setProperty("chest_chance", "0.45");
        setProperty("sublevel_chance", "0.175");

    }
}

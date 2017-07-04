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
public class LakesLevel extends ForrestLevelBuilder {
    
    public LakesLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Lakes", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 40);
        addTileType(Tile.treeDeciduous, 50);
        addTileType(Tile.waterBlue, 100);
        addTileType(Tile.chestSilver, 100);

        addDecalTile(Tile.waterLilypad, 6, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 50, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 6, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 20, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 20, Tile.empty);
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.325-0.425");
        setProperty("liquid_random_frequency", "0.425-0.455");
        setProperty("border_thickness", "4");
        setProperty("room_count", "0");
    }
}

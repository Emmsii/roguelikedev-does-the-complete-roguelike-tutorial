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
    
    public DenseLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Dense", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 50);
        addTileType(Tile.treeDeciduous, 45);
        addTileType(Tile.waterBlue, 100);
        addTileType(Tile.wallTopRed, 100);
        addTileType(Tile.chestSilver, 100);

        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.grassGreen, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 4, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 50, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 52, Tile.empty);
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

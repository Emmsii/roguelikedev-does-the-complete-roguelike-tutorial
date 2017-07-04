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
public class DarkLevel extends ForrestLevelBuilder{
    
    public DarkLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Dark", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 80);
        addTileType(Tile.treeDeciduous, 10);
        addTileType(Tile.waterBlue, 90);
        addTileType(Tile.waterDirty, 5);
        addTileType(Tile.wallTopBlue, 100);
        addTileType(Tile.chestGold, 100);

        addDecalTile(Tile.waterLilypad, 4, Tile.waterBlue);
        addDecalTile(Tile.waterBonesDirty1, 2, Tile.waterDirty);
        addDecalTile(Tile.waterBonesDirty2, 3, Tile.waterDirty);
        addDecalTile(Tile.mushroom, 7, Tile.empty, Tile.floor);
        addDecalTile(Tile.grassPurple, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.grassBlue, 60, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 80, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 10, Tile.empty);
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.4475-0.475");
        setProperty("tree_smooth", "3-4");
        setProperty("liquid_random_frequency", "0.31-0.35");
        setProperty("border_thickness", "4");
        setProperty("room_count", "7-8");
        setProperty("room_size_max", "8-9");
        setProperty("sublevel_chance", "0.225");
    }
    
}

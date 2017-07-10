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
        addTileType(Tile.getTile("treeConifer"), 80);
        addTileType(Tile.getTile("treeDeciduous"), 10);
        addTileType(Tile.getTile("waterBlue"), 90);
        addTileType(Tile.getTile("waterDirty"), 5);
        addTileType(Tile.getTile("wallTopBlue"), 100);
        addTileType(Tile.getTile("chestGold"), 100);

        addDecalTile(Tile.getTile("waterLilypad"), 4, Tile.getTile("waterBlue"));
        addDecalTile(Tile.getTile("waterBonesDirty1"), 2, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("waterBonesDirty2"), 3, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("mushroom"), 7, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("grassPurple"), 60, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("grassBlue"), 60, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 80, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 10, Tile.getTile("empty"));
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

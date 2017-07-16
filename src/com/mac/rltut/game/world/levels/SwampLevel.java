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
public class SwampLevel extends ForrestLevelBuilder {
    
    public SwampLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, Random random) {
        super("Swamp", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.getTile("treeConifer"), 50);
        addTileType(Tile.getTile("treeDeciduous"), 45);
        addTileType(Tile.getTile("waterDirty"), 80);
        addTileType(Tile.getTile("waterFoul"), 10);
        addTileType(Tile.getTile("grassMediumYellow"), 15);
        addTileType(Tile.getTile("grassMediumGreen"), 70);
        addTileType(Tile.getTile("grassSmallYellow"), 15);
        addTileType(Tile.getTile("grassSmallGreen"), 50);
        addTileType(Tile.getTile("waterBlue"), 5);

        addDecalTile(Tile.getTile("waterBonesDirty1"), 2, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("waterBonesDirty2"), 2, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("waterBonesFoul1"), 2, Tile.getTile("waterFoul"));
        addDecalTile(Tile.getTile("waterBonesFoul2"), 2, Tile.getTile("waterFoul"));
        addDecalTile(Tile.getTile("reeds1"), 9, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("reeds2"), 9, Tile.getTile("waterDirty"));
        addDecalTile(Tile.getTile("grassPurple"), 20, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("grassGreen"), 70, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("mushroom"), 15, Tile.getTile("empty"), Tile.getTile("floor"));
        addDecalTile(Tile.getTile("treeConifer"), 25, Tile.getTile("empty"));
        addDecalTile(Tile.getTile("treeDeciduous"), 25, Tile.getTile("empty"));
    }

    @Override
    protected void setProperties() {
        super.setProperties();
        setProperty("tree_random_frequency", "0.32-0.4");
        setProperty("liquid_random_frequency", "0.425-0.45");
        setProperty("liquid_smooth", "1-2");
        setProperty("border_thickness", "4");
        setProperty("room_count", "0");
    }
}

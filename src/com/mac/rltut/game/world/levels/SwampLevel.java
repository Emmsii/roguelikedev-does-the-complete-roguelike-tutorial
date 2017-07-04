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
    
    public SwampLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Swamp", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

    @Override
    protected void setTileTypes() {
        addTileType(Tile.treeConifer, 50);
        addTileType(Tile.treeDeciduous, 45);
        addTileType(Tile.waterDirty, 80);
        addTileType(Tile.waterFoul, 10);
        addTileType(Tile.waterBlue, 5);

        addDecalTile(Tile.waterBonesDirty1, 2, Tile.waterDirty);
        addDecalTile(Tile.waterBonesDirty2, 2, Tile.waterDirty);
        addDecalTile(Tile.waterBonesFoul1, 2, Tile.waterFoul);
        addDecalTile(Tile.waterBonesFoul2, 2, Tile.waterFoul);
        addDecalTile(Tile.grassPurple, 20, Tile.empty, Tile.floor);
        addDecalTile(Tile.grassGreen, 70, Tile.empty, Tile.floor);
        addDecalTile(Tile.mushroom, 15, Tile.empty, Tile.floor);
        addDecalTile(Tile.treeConifer, 25, Tile.empty);
        addDecalTile(Tile.treeDeciduous, 25, Tile.empty);

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

package com.mac.rltut.game.map.levels;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public class DefaultLevel extends LevelBuilder {

    public DefaultLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Default", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

}

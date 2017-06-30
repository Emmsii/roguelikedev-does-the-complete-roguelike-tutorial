package com.mac.rltut.game.map;

import com.mac.rltut.game.map.tile.Tile;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public class DefaultLevel extends LevelBuilder {

    public DefaultLevel(int width, int height, int z, int minLevel, int maxLevel, int chance) {
        super(width, height, z, minLevel, maxLevel, chance);
    }

    @Override
    public void init(Random random) {
        this.random = random;
    }

}
